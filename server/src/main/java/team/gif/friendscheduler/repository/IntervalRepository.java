package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.Interval;

import java.util.List;

@Repository
public interface IntervalRepository extends CrudRepository<Interval, Long> {
	
	List<Interval> findAllByUser_IdAndDayOfWeekOrderByStartMinAsc(Long userId, Integer dayOfWeek);
	List<Interval> findAllByUser_IdOrderByDayOfWeekAscStartMinAsc(Long userId);
	void deleteAllByUser_IdAndDayOfWeek(Long userId, int dayOfWeek);
	void deleteByUser_IdAndId(Long userId, Long intervalId);
	void deleteAllByUser_Id(Long userId);

}
