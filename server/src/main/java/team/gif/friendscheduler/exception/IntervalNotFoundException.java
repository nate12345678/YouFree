package team.gif.friendscheduler.exception;


public class IntervalNotFoundException extends RuntimeException {
	
	public IntervalNotFoundException(Long id) {
		super(String.format("Could not find intervalID '%d'", id));
	}
	
}
