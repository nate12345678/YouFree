package team.gif.friendscheduler.exception;

public class UnauthorizedException extends RuntimeException {
	
	public UnauthorizedException() {
		super("Invalid token");
	}
	
}
