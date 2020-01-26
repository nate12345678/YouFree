<<<<<<< HEAD
package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.Interval;

import java.util.List;

@Repository
public interface IntervalRepository extends CrudRepository<Interval, Long> {
	
	List<Interval> findAllByUserIdAndDayOfWeekOrderByStartMinAsc(Long userId, int dayOfWeek);
	List<Interval> findAllByUserIdOrderByDayOfWeekAscStartMinAsc(Long userId);
	void deleteAllByUserIdAndDayOfWeek(Long userId, int dayOfWeek);
	void deleteAllByUserId(Long userId);

}
=======
package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.Interval;

import java.util.List;

@Repository
public interface IntervalRepository extends CrudRepository<Interval, Long> {
	
	List<Interval> findAllByUserIdAndDayOfWeekOrderByStartMinAsc(Long userId, int dayOfWeek);
	List<Interval> findAllByUserIdOrderByDayOfWeekAscStartMinAsc(Long userId);
	void deleteAllByUserIdAndDayOfWeek(Long userId, int dayOfWeek);
	void deleteByUserIdAndId(Long userId, Long intervalId);
	void deleteAllByUserId(Long userId);

}
>>>>>>> 9430a22d329f7ef0ca0f45c78d8b486aa01e88a0
