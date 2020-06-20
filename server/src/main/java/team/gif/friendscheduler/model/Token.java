package team.gif.friendscheduler.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import team.gif.friendscheduler.exception.TokenCreationException;

public class Token {
	
	private Header header;
	private Payload payload;
	
	public Token(Long userId) {
		header = new Header();
		payload = new Payload(userId);
	}
	
	
	public String getHeader() {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(header);
		} catch (JsonProcessingException e) {
			throw new TokenCreationException(e);
		}
	}
	
	
	public String getPayload() {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(payload);
		} catch (JsonProcessingException e) {
			throw new TokenCreationException(e);
		}
	}
	
	
	private static class Header {
		private final String typ; // Type of the token
		private final String alg; // Hash algorithm used for signature
		
		private Header() {
			typ = "JWT";
			alg = "HS512";
		}
		
		
		public String getTyp() {
			return typ;
		}
		
		
		public String getAlg() {
			return alg;
		}
	}
	
	private static class Payload {
		private final String iss; // Application issuing the token
		private final String sub; // User ID
		
		private Payload(Long userId) {
			iss = "YouFree";
			sub = userId + "";
		}
		
		
		public String getIss() {
			return iss;
		}
		
		
		public String getSub() {
			return sub;
		}
	}
	
}
