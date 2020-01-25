package team.gif.friendscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;
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
	
	@OneToMany
	@OrderBy("start ASC")
	private List<Interval> intervals; // Free times in their schedule
	
	
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
	
	
	public List<Interval> getSchedule() {
		return intervals;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public void addInterval(Interval interval) {
		if (intervals.size() == 0) {
			intervals.add(interval);
			return;
		}
		
		ListIterator<Interval> iterator = intervals.listIterator();
		
		// Find position in list (based on start time)
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			if (interval.getStart() <= next.getStart()) {
				// Insert before current
				iterator.previous();
				iterator.add(interval);
				break;
			}
		}
		
		// In this case, we reached the end of the list without insert. Insert now.
		if (!iterator.hasNext()) {
			iterator.add(interval);
		}
		
		// Check if previous should merge
		iterator.previous(); // Now we're pointing to inserted element
		Interval previous = iterator.previous();
		if (previous.getEnd() > interval.getStart()) {
			interval.setStart(previous.getStart());
			interval.setEnd(Math.max(previous.getEnd(), interval.getEnd()));
			iterator.remove();
		}
		
		// While applicable, merge with next interval
		iterator.next(); // Now calling next() again returns element after inserted
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			
			if (next.getStart() > interval.getEnd()) {
				// No overlap, so stop looking
				break;
			}
			
			interval.setEnd(Math.max(interval.getEnd(), next.getEnd()));
			iterator.remove();
		}
	}
	
	
	public boolean removeInterval(Interval interval) {
		ListIterator<Interval> iterator = intervals.listIterator();
		
		boolean removed = false;
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			if (next.getStart() == interval.getStart()) {
				iterator.remove();
				removed = true;
			}
		}
		
		return removed;
	}
	
}
