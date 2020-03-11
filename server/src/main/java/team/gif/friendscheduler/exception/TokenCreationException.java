package team.gif.friendscheduler.exception;

public class TokenCreationException extends RuntimeException {
	
	public TokenCreationException(Exception cause) {
		super("Failed to create token", cause);
	}
	
}
