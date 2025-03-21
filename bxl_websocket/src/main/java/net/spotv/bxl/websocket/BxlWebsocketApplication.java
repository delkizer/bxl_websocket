package net.spotv.bxl.websocket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class BxlWebsocketApplication {
    private final HandlerMapping websocketMapping;
    private final ConcurrentHashMap<String, RoomState> roomMap = new ConcurrentHashMap<>();

    BxlWebsocketApplication(HandlerMapping websocketMapping) {
        this.websocketMapping = websocketMapping;
    }

	public static void main(String[] args) {
		SpringApplication.run(BxlWebsocketApplication.class, args);
	}
	
    @Bean
    public HandlerMapping websocketMapping() {
        return new SimpleUrlHandlerMapping(
                Map.of(
                		"/ws/timestamp", timestampHandler(), 
                		"/ws/{gameDate}/{tieNo}", dynamicHandler() ),
                1
        );
    }
    
    @Bean
    public WebSocketHandler dynamicHandler() {
    	return session -> {
    		String fullPath = session.getHandshakeInfo().getUri().getPath();
    		String[] parts = fullPath.split("/");
    		
    		String gameDate = (parts.length > 2) ? parts[2] : null;
    		String tieNo =  (parts.length > 3) ? parts[3] : null;
    		
    		String roomKey = gameDate + "-" + tieNo;
            RoomState room = roomMap.computeIfAbsent(roomKey, key -> new RoomState());
            
            Mono<Void> output = session.send(
                    room.broadcastFlux.map(session::textMessage)
                );
            
            Mono<Void> input = session.receive()
                    .doOnNext(msg -> {
                        String payload = msg.getPayloadAsText();
                        room.handleIncomingMessage(session.getId(), payload);
                    })
                    .then();

                return Mono.zip(output, input).then();            
    	};
    }
    

    @Bean
    public WebSocketHandler timestampHandler() {
    	ObjectMapper mapper = new ObjectMapper();
    	
        return session -> session.send(
        		Flux.interval(Duration.ofSeconds(1))
        		.map(seq -> {
        			Map<String, String> json = Map.of(
        					"type", "timestamp",
        					"serverTime", LocalDateTime.now().toString()
        					);
        			try {
        				return session.textMessage(mapper.writeValueAsString(json));
        			} catch (JsonProcessingException e ){
                        return session.textMessage("{\"error\":\"JSON 변환 오류\"}");
        			}
        		})
        ).and(session.receive().then());
    }

}
