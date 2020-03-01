package team.gif.friendscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Tokens")
public class Token {
	
	@Id
	private Long userId; // This should be unique, as a user can only have one token?
	
	@Column(unique = true)
	@Size(min = 1)
	private String token;
	
	
	public Token() {}
	
	
	public Token(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}
	
	
	public String getToken() {
		return token;
	}
	
	
	public Long getUserId() {
		return userId;
	}
	
	
	public void setToken(String token) {
		this.token = token;
	}
	
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
