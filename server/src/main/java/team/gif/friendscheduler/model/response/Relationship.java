package team.gif.friendscheduler.model.response;

public enum Relationship {
	
	FRIENDS("friends"),
	SENT("sent"),
	PENDING("pending"),
	NONE("none"),
	BLOCKED("blocked"), // A user can see others that they've blocked,
	HIDDEN("hidden"), // If target user has blocked requester
	MUTUAL_BLOCK("mutual_block")
	;
	
	public String value;
	
	Relationship(String value) {
		this.value = value;
	}
	
}
