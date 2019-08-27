package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Long> {
	
	Optional<List<Friendship>> findFriendshipsByLargerUserIdOrSmallerUserId(@Param("largerUserId") User largerUserId,
	                                                                        @Param("smallerUserId") User smallerUserId);
	
}
