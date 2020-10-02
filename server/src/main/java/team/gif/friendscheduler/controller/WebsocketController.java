package team.gif.friendscheduler.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
	
	@MessageMapping("/hello")
	@SendTo("/topic/hello")
	public String sayHello(String message) {
		return "Hello world!";
	}
	
}
