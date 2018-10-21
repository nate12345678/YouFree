package team.gif.friendscheduler.exception;

import lombok.Getter;

public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException(Long id) {
		super(String.format("Could not find userID '%d'", id));
	}
	
	public UserNotFoundException(String name) {
		super(String.format("Could not find user '%s'", name));
	}
	
}
