package net.spotv.bxl.websocket;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class RoomState {
    private final String roomKey;
    private final BxlWebsocketApplication owner;
    private final Sinks.Many<String> sink;
    public final Flux<String> broadcastFlux;
    private final ConcurrentLinkedQueue<String> sessionIds = new ConcurrentLinkedQueue<>();

    // 시계 상태
    private Map<String, Integer> clockState = new HashMap<>();
    
    public RoomState( String roomKey, BxlWebsocketApplication owner ) {
    	this.roomKey = roomKey;
    	this.owner = owner;
    	
    	this.sink = Sinks.many().multicast().directBestEffort();
    	this.broadcastFlux = sink.asFlux();
        
        //기본값 
        clockState.put("warmup", 120);
        clockState.put("match", 600);
        clockState.put("break", 60);        
    }
    
    /**
     * 클라이언트 세션이 새로 join할 때 호출할 수도 있음.
     */
    public void onJoin(String sessionId) {
        sessionIds.add(sessionId);
        
        sink.tryEmitNext("JOIN: session " + sessionId + " => " + getStateJson());
    }
    
    public String getStateJson() {
        try {
            return new ObjectMapper().writeValueAsString(clockState);
        } catch (Exception e) {
            return "{\"error\":\"json\"}";
        }    	
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
        sink.tryEmitNext("Echo from server: " + payload);
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
    
    public void removeRoom(String roomKey) {
        roomMap.remove(roomKey);
        System.out.println("Room removed: " + roomKey);
    }    
    
}
