package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.model.Interval;
import team.gif.friendscheduler.model.request.NewInterval;
import team.gif.friendscheduler.repository.IntervalRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Service
@Transactional
public class IntervalService {
	
	private final IntervalRepository intervalRepository;
	
	@Autowired
	public IntervalService(IntervalRepository intervalRepository) {
		this.intervalRepository = intervalRepository;
	}
	
	
	public Optional<Interval> getInterval(Long intervalId) {
		return intervalRepository.findById(intervalId);
	}
	
	
	public ArrayList<LinkedList<Interval>> getIntervals(Long userId) {
		List<Interval> allIntervals = intervalRepository.findAllByUserIdOrderByDayOfWeekAscStartMinAsc(userId);
		
		ArrayList<LinkedList<Interval>> arrangedIntervals = new ArrayList<>();
		
		// Add empty list for each day of the week
		for (int i = 0; i < 7; i++) {
			arrangedIntervals.add(i, new LinkedList<>());
		}
		
		// Sort each of the intervals into their respective days
		for (Interval next : allIntervals) {
			arrangedIntervals.get(next.getDayOfWeek()).addLast(next);
		}
		
		return arrangedIntervals;
	}
	
	
	public void addInterval(Long userId, NewInterval intervalRequest) {
		List<Interval> intervals = intervalRepository.findAllByUserIdAndDayOfWeekOrderByStartMinAsc(userId, intervalRequest.getDayOfWeek());
		Interval interval = new Interval(userId, intervalRequest);
		
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
		
		iterator.previous(); // Now we're pointing just before the inserted element
		if (iterator.hasPrevious()) { // Check if previous should merge (if previous exists)
			Interval previous = iterator.previous();
			if (previous.getEndMin() >= interval.getStartMin()) {
				interval.setStartMin(previous.getStartMin());
				interval.setEndMin(Math.max(previous.getEndMin(), interval.getEndMin()));
				intervalRepository.deleteById(previous.getId());
				iterator.remove(); // We're now pointing just before the inserted element again
			} else {
				iterator.next(); // We're now pointing just before the inserted element again
			}
		}
		
		// While applicable, merge with next interval
		iterator.next(); // Now we're pointing just after the inserted element
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			
			if (next.getStartMin() > interval.getEndMin()) {
				// No overlap, so stop looking
				break;
			}
			
			interval.setEndMin(Math.max(interval.getEndMin(), next.getEndMin()));
			intervalRepository.deleteById(next.getId());
			iterator.remove();
		}
		
		intervalRepository.save(interval);
	}
	
	
	public void removeInterval(Long userId, Long intervalId) {
		intervalRepository.deleteByUserIdAndId(userId, intervalId);
	}
	
	
	public void removeAllIntervals(Long userId) {
		intervalRepository.deleteAllByUserId(userId);
	}
	
}
