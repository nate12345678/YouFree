package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.model.Interval;
import team.gif.friendscheduler.repository.IntervalRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@Service
public class IntervalService {
	
	private final IntervalRepository intervalRepository;
	
	@Autowired
	public IntervalService(IntervalRepository intervalRepository) {
		this.intervalRepository = intervalRepository;
	}
	
	
	public ArrayList<LinkedList<Interval>> getIntervals(Long userId) {
		List<Interval> allIntervals = intervalRepository.findAllByUserIdOrderByDayOfWeekAscStartMinAsc(userId);
		
		ArrayList<LinkedList<Interval>> arrangedIntervals = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			arrangedIntervals.add(i, new LinkedList<>());
		}
		
		for (Interval next : allIntervals) {
			arrangedIntervals.get(next.getDayOfWeek()).addLast(next);
		}
		
		return arrangedIntervals;
	}
	
	
	public void addInterval(Long userId, Interval interval) {
		List<Interval> intervals = intervalRepository.findAllByUserIdAndDayOfWeekOrderByStartMinAsc(userId, interval.getDayOfWeek());
		intervalRepository.deleteAllByUserIdAndDayOfWeek(userId, interval.getDayOfWeek());
		
		if (intervals.size() == 0) {
			intervalRepository.save(interval);
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
	
	
	public void removeInterval(Long intervalId) {
		intervalRepository.deleteById(intervalId);
	}
	
}
