package team.gif.friendscheduler.model;


import team.gif.friendscheduler.Globals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USERS")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	@PositiveOrZero
	private Long discordSnowflake;
	
	@Column(unique = true, nullable = false)
	@Size(min = 1, max = 64)
	private String username;
	
	@Column(nullable = false)
	@Size(min = 1, max = 64)
	private String password;
	
	// TODO: validate using a pattern
//	EXAMPLE: @Pattern(regexp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
	@Column(unique = true, nullable = false)
	@Size(min = 5, max = 256) // Shortest email: a@b.c
	private String email;
	
	@Column
	private int[][] schedule;
	
	
	public User() {
		this.schedule = new int[Globals.NUM_DAYS_IN_WEEK][Globals.NUM_BLOCKS_IN_DAY];
		
		for (int i = 0; i < schedule.length; i++) {
			for (int j = 0; j < schedule[i].length; j++) {
				schedule[i][j] = 0;
			}
		}
	}
	
	public User(String username, String password, String email) {
		this.discordSnowflake = null;
		this.username = username;
		this.password = password;
		this.email = email;
		this.schedule = new int[Globals.NUM_DAYS_IN_WEEK][Globals.NUM_BLOCKS_IN_DAY];
		
		for (int i = 0; i < schedule.length; i++) {
			for (int j = 0; j < schedule[i].length; j++) {
				schedule[i][j] = 0;
			}
		}
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public Long getDiscordSnowflake() {
		return discordSnowflake;
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
	
	
	public int[][] getSchedule() {
		return schedule;
	}
	
	
	public void setDiscordSnowflake(Long snowflake) {
		this.discordSnowflake = snowflake;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public void updateSchedule(TimeBlock interval) {
		this.schedule[interval.getDay()][interval.getBlock()] = interval.getStatus();
	}
	
}
