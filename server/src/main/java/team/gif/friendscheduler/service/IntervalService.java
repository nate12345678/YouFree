package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.model.Interval;
import team.gif.friendscheduler.repository.IntervalRepository;

import java.util.List;
import java.util.ListIterator;

@Service
public class IntervalService {
	
	private final IntervalRepository intervalRepository;
	
	@Autowired
	public IntervalService(IntervalRepository intervalRepository) {
		this.intervalRepository = intervalRepository;
	}
	
	
	public List<Interval> getIntervals(Long userId) {
		return intervalRepository.findAllByUserIdOrderByStartMinAsc(userId);
	}
	
	
	public void addInterval(Long userId, Interval interval) {
		List<Interval> intervals = intervalRepository.findAllByUserIdOrderByStartMinAsc(userId);
		intervalRepository.deleteAllByUserId(userId);
		
		if (intervals.size() == 0) {
			intervals.add(interval);
			return;
		}
		
		ListIterator<Interval> iterator = intervals.listIterator();
		
		// Find position in list (based on start time)
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			if (interval.getStartMin() <= next.getStartMin()) {
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
		if (previous.getEndMin() > interval.getStartMin()) {
			interval.setStartMin(previous.getStartMin());
			interval.setEndMin(Math.max(previous.getEndMin(), interval.getEndMin()));
			iterator.remove();
		}
		
		// While applicable, merge with next interval
		iterator.next(); // Now calling next() again returns element after inserted
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			
			if (next.getStartMin() > interval.getEndMin()) {
				// No overlap, so stop looking
				break;
			}
			
			interval.setEndMin(Math.max(interval.getEndMin(), next.getEndMin()));
			iterator.remove();
		}
		
		intervalRepository.saveAll(intervals);
	}
	
	
	public void removeInterval(Interval interval) {
		intervalRepository.deleteById(interval.getId());
	}
	
}
