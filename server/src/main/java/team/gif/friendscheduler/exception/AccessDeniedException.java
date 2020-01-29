package team.gif.friendscheduler.exception;

public class AccessDeniedException extends RuntimeException {
	
	public AccessDeniedException(Long requesterId, Long targetId) {
		super(String.format("User '%d' does not have access to user '%d'", requesterId, targetId));
	}
	
}
