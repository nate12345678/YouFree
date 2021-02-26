package team.gif.friendscheduler.model.response;

import team.gif.friendscheduler.model.User;

public class UserResponse {
	
	public static UserResponse convert(User user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
	}
	
	
	private Long id;
	private String username;
	private String email;
	
	
	public UserResponse() {}
	
	public UserResponse(Long id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	
	public String getEmail() {
		return email;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}
