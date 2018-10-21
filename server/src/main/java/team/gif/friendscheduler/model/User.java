package team.gif.friendscheduler.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;

@Data
@Entity
@Table(name = "USERS")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String displayName;
	
	@Column(nullable = false)
	private Integer[][] schedule;
	
	
	public User() {}
	
	
	public User(String username, String password, String email, String displayName) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.displayName = displayName;
		this.schedule = new Integer[7][96];
		
		for (int i = 0; i < schedule.length; i++) {
			for (int j = 0; j < schedule[i].length; j++) {
				schedule[i][j] = 0;
			}
		}
	}
	
	
	public void updateSchedule(int day, int block, Integer status) {
		this.schedule[day][block] = status;
	}
	
}
