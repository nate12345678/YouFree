package team.gif.friendscheduler.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.TEXT_PLAIN_VALUE)
public class HelloWorldController {
	
	@GetMapping(value = "/hello")
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello world");
	}
	
}
