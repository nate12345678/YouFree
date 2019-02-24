package team.gif.friendscheduler.model;

import team.gif.friendscheduler.Globals;
import team.gif.friendscheduler.exception.InvalidFieldException;

public class TimeBlock {
	
	private int day;
	private int block;
	private int status;
	
	/**
	 * Specifies a 15 minute time interval within a week
	 *
	 * @param day The day of the week (0 = Monday, 6 = Sunday)
	 * @param block The 15 minute block of time within the day (0 = 0:00, 95 = 23:45)
	 * @param status An indicator of the status of the user during this time interval
	 */
	public TimeBlock(int day, int block, int status) {
		if (day < 0 || day > Globals.NUM_DAYS_IN_WEEK)
			throw new InvalidFieldException("Property <day> must be within [0, 6]; was " + day);
		
		if (block < 0 || block > Globals.NUM_BLOCKS_IN_DAY)
			throw new InvalidFieldException("Property <block> must be within [0, 95]; was " + block);
		
		this.day = day;
		this.block = block;
		this.status = status;
	}
	
	
	public int getDay() {
		return day;
	}
	
	
	public int getBlock() {
		return block;
	}
	
	
	public int getStatus() {
		return status;
	}
	
}
