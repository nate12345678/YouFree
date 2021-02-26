package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.FriendshipKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, FriendshipKey> {
	
	List<Friendship> getFriendshipsByFriendshipKey_LargerUserIdOrFriendshipKey_SmallerUserId(Long userId1, Long userId2);
	List<Friendship> getFriendshipsByFriendshipKey_LargerUserId(Long userId);
	List<Friendship> getFriendshipsByFriendshipKey_SmallerUserId(Long userId);
	Optional<Friendship> getFriendshipByFriendshipKey(FriendshipKey key);
	void deleteAllByFriendshipKey_LargerUserIdOrFriendshipKey_SmallerUserId(Long userId1, Long userId2);
	
}
