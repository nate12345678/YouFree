package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.exception.FriendRequestException;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.FriendshipKey;
import team.gif.friendscheduler.model.FriendshipStatus;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.repository.FriendshipRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
	
	private final FriendshipRepository friendshipRepository;
	
	@Autowired
	public FriendshipService(FriendshipRepository friendshipRepository) {
		this.friendshipRepository = friendshipRepository;
	}
	
	public List<Long> getFriends(User user) {
		List<Friendship> friendships = friendshipRepository.getFriendshipsByFriendshipKey_LargerUserIdOrFriendshipKey_SmallerUserId(user.getId(), user.getId());
		
		return friendships.parallelStream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.FRIENDS)
				.map(friendship -> friendship.getFriendshipKey().getSmallerUserId().equals(user.getId())
						? friendship.getFriendshipKey().getLargerUserId()
						: friendship.getFriendshipKey().getSmallerUserId())
				.collect(Collectors.toList());
	}
	
	public void addFriendship(Long requesterId, Long targetId) {
		
		long smaller = Math.min(requesterId, targetId);
		long larger = Math.max(requesterId, targetId);
		
		Optional<Friendship> current = friendshipRepository.getFriendshipByFriendshipKey(new FriendshipKey(smaller, larger));
		
		// No relationship exists between the users
		if (current.isEmpty()) {
			FriendshipStatus status = (smaller == requesterId) ? FriendshipStatus.AWAITING_LARGER_ID_APPROVAL : FriendshipStatus.AWAITING_SMALLER_ID_APPROVAL;
			Friendship friendship = new Friendship(smaller, larger, status);
			friendshipRepository.save(friendship);
			return;
		}
		
		Friendship friendship = current.get();
		
		// Smaller ID is sending request
		if (smaller == requesterId) {
			switch (friendship.getStatus()) {
				case AWAITING_SMALLER_ID_APPROVAL:
					// Accepting request
					friendship.setStatus(FriendshipStatus.FRIENDS);
					friendshipRepository.save(friendship);
					break;
				case AWAITING_LARGER_ID_APPROVAL:
					// Double-sending request
					throw new FriendRequestException("Pending friend request to target user already exists");
				case FRIENDS:
					// Double-sending request
					throw new FriendRequestException("Target user is already a friend");
				default:
					throw new FriendRequestException("Target user has blocked all requests from sender");
			}
			
		} else { // Larger ID is sending request
			switch (friendship.getStatus()) {
				case AWAITING_LARGER_ID_APPROVAL:
					// Accepting request
					friendship.setStatus(FriendshipStatus.FRIENDS);
					friendshipRepository.save(friendship);
					break;
				case AWAITING_SMALLER_ID_APPROVAL:
					// Double-sending request
					throw new FriendRequestException("Pending friend request to target user already exists");
				case FRIENDS:
					// Double-sending request
					throw new FriendRequestException("Target user is already a friend");
				default:
					throw new FriendRequestException("Target user has blocked all requests from sender");
			}
		}
		
		/* TODO: Friendship request with blocked user status logic */
		
	}
	
	
	public void deleteFriendship(Long user1, Long user2) {
		
		// TODO: don't allow this if no friendship exists OR if one user has blocked the other
		
		FriendshipKey key = (user1 < user2)
				? new FriendshipKey(user1, user2)
				: new FriendshipKey(user2, user1);
		
		friendshipRepository.deleteById(key);
	}
	
	
	public void removeUser(Long userId) {
		friendshipRepository.deleteAllByFriendshipKey_LargerUserIdOrFriendshipKey_SmallerUserId(userId, userId);
	}
	
}
