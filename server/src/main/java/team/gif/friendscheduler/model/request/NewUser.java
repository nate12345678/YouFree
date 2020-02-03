package team.gif.friendscheduler.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class NewUser {
	
	@Size(min = 1, max = 64)
	private String username;
	
	@Size(min = 1, max = 64)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	@Size(min = 5, max = 256) // Shortest email: a@b.c
	@Email
	private String email;
	
	
	public NewUser() {}
	
	
	public String getUsername() {
		return username;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	
	public String getEmail() {
		return email;
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}
