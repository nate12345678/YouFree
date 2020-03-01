package team.gif.friendscheduler.exception;

public class FriendshipNotFoundException extends RuntimeException {
	
	public FriendshipNotFoundException(Long userId) {
		super(String.format("No friendship found with user %d", userId));
	}
	
}
