package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.FriendshipKey;

import java.util.List;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, FriendshipKey> {
	
	List<Friendship> getFriendshipsByFriendshipKey_LargerUserId_IdOrFriendshipKey_SmallerUserId_Id(Long userId1, Long userId2);
	
}
