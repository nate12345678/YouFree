package team.gif.friendscheduler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}
	
	/*
	 * Enables SockJS fallback options so that alternate transports can be used
	 * if WebSocket is not available. The SockJS client will attempt to connect
	 * to /gs-guide-websocket and use the best available transport
	 * (websocket, xhr-streaming, xhr-polling, and so on)
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/notifications");
		registry.addEndpoint("/notifications").withSockJS();
		registry.addEndpoint("/hello");
		registry.addEndpoint("/hello").withSockJS();
	}
	
}
