package net.spotv.bxl.websocket;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class RoomState {
	public enum GameStatus {
	    SCHEDULED,
	    WARMUP,
	    RUNNING,
	    FINISHED,
	    CANCELED;
	}
	
	public enum PacketType { 
		MODIFY
	}
	
	public enum ModifyType { 
		modify_time,
		game_status,
	}
	
    private final String roomKey;
    private final BxlWebsocketApplication owner;
    private final Sinks.Many<String> sink;
    public final Flux<String> broadcastFlux;
    private final ConcurrentLinkedQueue<String> sessionIds = new ConcurrentLinkedQueue<>();
    
    private final ScheduledExecutorService decrementScheduler;
    private final ScheduledExecutorService broadcastScheduler;
    
    private volatile int warmUp = 120;
    private volatile int matchTime = 600;
    private volatile int breakTime = 60;    
    private volatile GameStatus  gameStatus = GameStatus.SCHEDULED; 
    private volatile boolean paused = false;

    public RoomState( String roomKey, BxlWebsocketApplication owner ) {
    	this.roomKey = roomKey;
    	this.owner = owner;
    	
    	this.sink = Sinks.many().multicast().directBestEffort();
    	this.broadcastFlux = sink.asFlux();
    
    	this.decrementScheduler = Executors.newSingleThreadScheduledExecutor();
    	this.broadcastScheduler = Executors.newSingleThreadScheduledExecutor();
    	
        startDecrementTask();   // 1초 주기
        startBroadcastTask();   // 0.1초 주기
    	
    }
    
    private void startDecrementTask() {
        // 1초마다 warmUp-- (if not paused)
        decrementScheduler.scheduleAtFixedRate(() -> {
            if (!paused) {
                switch (gameStatus) {
                    case WARMUP:
                        if (warmUp > 0) {
                            warmUp--;
                        } else {
                            // warmUp이 0이 되면 RUNNING으로 전환
                            gameStatus = GameStatus.RUNNING;
                        }
                        break;

                    case RUNNING:
                        if (matchTime > 0) {
                            matchTime--;
                        } else {
                            // match 0 => FINISHED
                            gameStatus = GameStatus.FINISHED;
                        }
                        break;

                    case FINISHED:
                        if (breakTime > 0) {
                            breakTime--;
                        } else {
                            // break 0 => 다시 SCHEDULED
                            gameStatus = GameStatus.SCHEDULED;
                            // warmUp, match, break 초기화
                            warmUp = 120;
                            matchTime = 600;
                            breakTime = 60;
                        }
                        break;

                    case SCHEDULED:
                        // SCHEDULED는 클라이언트가 game_status를 WARMUP으로 바꿔야 시작
                        break;

                    case CANCELED:
                        // 필요 시 다른 로직
                        break;
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    
    private void startBroadcastTask() {
        // 0.1초마다 broadcast
        broadcastScheduler.scheduleAtFixedRate(() -> {
            String json = getStateJson();
            sink.tryEmitNext(json);
        }, 0, 100, TimeUnit.MILLISECONDS);
    }    
    
    
    /**
     * 클라이언트 세션이 새로 join할 때 호출할 수도 있음.
     */
    public void onJoin(String sessionId) {
        sessionIds.add(sessionId);
        
        sink.tryEmitNext("JOIN: session " + sessionId + " => " + getStateJson());
    }
    
    public String getStateJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("game_state", this.gameStatus);
        result.put("warmup", this.warmUp);
        result.put("match", this.matchTime);
        result.put("break", this.breakTime);
        
        String[] parts = this.roomKey.split("-");        
        String tieNo = parts[parts.length - 1];
        String[] dateParts = new String[parts.length - 1];
        System.arraycopy(parts, 0, dateParts, 0, parts.length - 1);
        String gameDate = String.join("-", dateParts);
        
        result.put("gameDate", gameDate);
        result.put("tieNo", tieNo);
    	
        try {
            return new ObjectMapper().writeValueAsString(result);
        } catch (Exception e) {
            return "{\"error\":\"json\"}";
        }    	
    }
    
    //warm up 변경 
    public void pauseWarmUp() { this.paused = true; }
    public void resumeWarmUp() { this.paused = false; }
    // onClose
    public void stopAll() {
        decrementScheduler.shutdown();
        broadcastScheduler.shutdown();
    }    

    /**
     * 클라이언트가 떠났을 때
     */
    public void onLeave(String sessionId) {
        sessionIds.remove(sessionId);
        
        if( sessionIds.isEmpty() ) {
        	scheduleRoomDeletion();
        }
    }

    /**
     * 클라이언트 메시지를 받았을 때 처리하는 로직
     *
     * @param sessionId  메시지를 보낸 세션
     * @param payload    메시지 내용(JSON 등)
     */
    public void handleIncomingMessage(String sessionId, String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> msg = mapper.readValue(payload, Map.class);
            String packetTypeStr = (String) msg.get("packet_type");
            PacketType packetType = PacketType.valueOf(packetTypeStr);
            
            String modifyTypeStr = (String) msg.get("modify_type");
            ModifyType modifyType = ModifyType.valueOf(modifyTypeStr);
            
            Map<String, Object> data = (Map<String, Object>) msg.get("data");

            //패킷 티입에 의한 분기 
            switch( packetType) {
            	case MODIFY:
                    switch (modifyType) {
                    case modify_time:
                        handleTimeUpdate(data);
                        break;
                    case game_status:
                        handleGameStatusUpdate(data);
                        break;
                    }
            		break;
            	default:
            		break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sink.tryEmitNext("Error: " + e.getMessage());
        }
    }
    
    private void handleGameStatusUpdate(Map<String, Object> data ) { 
    	String gameStatus = (String) data.get("game_status");
    	
    	if ( "WARMUP".equals(gameStatus)) {
    		this.gameStatus = GameStatus.WARMUP;
    	}
    }
    
    private void handleTimeUpdate(Map<String, Object> msg) {
        // "game_type":"warm-up", "modify_sec": 3, "add_type": "plus"
        String gameType = (String) msg.get("time_type");
        Integer modifySec = (Integer) msg.get("modify_sec");
        String addType = (String) msg.get("add_type");

        // 1) plus/minus 판단
        int delta = ("plus".equals(addType)) ? modifySec : -modifySec;

        // 2) gameType에 따라 수정 (warm-up, match, break 등)
        if ("warm-up".equals(gameType)) {
            this.warmUp += delta;  // RoomState 예시
        } 

        // 3) 그 후 즉시 방 전체 브로드캐스트
        sink.tryEmitNext(getStateJson());
    }
        
    private void scheduleRoomDeletion() {
    	Mono.delay( Duration.ofMinutes(5))
    	.subscribe(_unused -> {
    		if ( sessionIds.isEmpty() ) {
                // roomMap에서 제거
                // 이때 roomMap을 참조하기 위해선, 소유 객체(예: BxlWebsocketApplication)에서 메서드 호출 허용 필요
                // 혹은 RoomState에 callback을 주입    			
                owner.removeRoom(roomKey);
    		}
    	});
    }
    
}