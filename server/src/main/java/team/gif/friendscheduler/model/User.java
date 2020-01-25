package team.gif.friendscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.ListIterator;

@Entity
@Table(name = "USERS")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// TODO: Pattern to disallow special characters
	@Column(unique = true, nullable = false)
	@Size(min = 1, max = 64)
	private String username;
	
	// TODO: Pattern to disallow special characters
	@Column(nullable = false)
	@Size(min = 1, max = 64)
	private String password;
	
	// TODO: validate using a pattern
//	EXAMPLE: @Pattern(regexp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
	@Column(unique = true, nullable = false)
	@Size(min = 5, max = 256) // Shortest email: a@b.c
	private String email;
	
	@Column
	private LinkedList<Interval> intervals; // Free times in their schedule
	
	
	public User() {
		this.intervals = new LinkedList<>();
	}
	
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.intervals = new LinkedList<>();
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	
	public String getEmail() {
		return email;
	}
	
	
	public LinkedList<Interval> getSchedule() {
		return intervals;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public void addInterval(Interval interval) {
	
	}
	
	
	public void removeInterval(Interval interval) {
	
	}
	
}
