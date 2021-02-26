package team.gif.friendscheduler.model.response;

public class UserSearchResponse {
	
	private final UserResponse user;
	private final Relationship relationship;
	
	public UserSearchResponse(UserResponse user, Relationship relationship) {
		this.user = user;
		this.relationship = relationship;
	}
	
	
	public UserResponse getUser() {
		return user;
	}
	
	
	public Relationship getRelationship() {
		return relationship;
	}
	
}
