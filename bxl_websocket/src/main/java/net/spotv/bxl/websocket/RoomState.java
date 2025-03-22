package net.spotv.bxl.websocket;

import java.util.concurrent.ConcurrentLinkedQueue;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class RoomState {
    private final Sinks.Many<String> sink;
    public final Flux<String> broadcastFlux;

    private final ConcurrentLinkedQueue<String> sessionIds;

    public RoomState() {
        // 1) Sinks.many().multicast() 등의 방식으로 broadcast sink 생성
        this.sink = Sinks.many().multicast().directBestEffort();
        // 2) sink를 Flux로 노출, 구독하는 쪽(session.send)에서 이 Flux를 활용
        this.broadcastFlux = sink.asFlux();
        // 3) 세션ID 보관 (참고용)
        this.sessionIds = new ConcurrentLinkedQueue<>();
    }
    
    /**
     * 클라이언트 세션이 새로 join할 때 호출할 수도 있음.
     */
    public void onJoin(String sessionId) {
        sessionIds.add(sessionId);
        // 필요하다면 기존 상태 정보 send
    }

    /**
     * 클라이언트가 떠났을 때
     */
    public void onLeave(String sessionId) {
        sessionIds.remove(sessionId);
    }

    /**
     * 클라이언트 메시지를 받았을 때 처리하는 로직
     *
     * @param sessionId  메시지를 보낸 세션
     * @param payload    메시지 내용(JSON 등)
     */
    public void handleIncomingMessage(String sessionId, String payload) {
        // 예: 간단히 broadcast
        // 실제로는 JSON 파싱 등 비즈니스 로직 가능
        sink.tryEmitNext("Echo from server: " + payload);
    }
    
}
