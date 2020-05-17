package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.exception.FriendRequestException;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.FriendshipKey;
import team.gif.friendscheduler.model.FriendshipStatus;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.repository.FriendshipRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendshipService {
	
	private final FriendshipRepository friendshipRepository;
	
	
	@Autowired
	public FriendshipService(FriendshipRepository friendshipRepository) {
		this.friendshipRepository = friendshipRepository;
	}
	
	
	/**
	 * Checks to see if two users are friends.
	 * This is intended to only look up one pair of users; if it is desired to check
	 * if multiple users are friends with one target, it is likely more efficient
	 * to get a list of the target's friends, sort it, and search through it.
	 *
	 * @param userId An ID of one of the users in the relationship
	 * @param otherUserId An ID of the other user in the relationship
	 * @return Whether these two users are friends
	 */
	public boolean hasFriendship(Long userId, Long otherUserId) {
		long smaller = Math.min(userId, otherUserId);
		long larger = Math.max(userId, otherUserId);
		
		FriendshipKey key = new FriendshipKey(smaller, larger);
		Optional<Friendship> optional = friendshipRepository.getFriendshipByFriendshipKey(key);
		
		return optional.isPresent() && optional.get().getStatus() == FriendshipStatus.FRIENDS;
	}
	
	
	public List<Long> getFriends(Long userId) {
		List<Friendship> friendships = friendshipRepository.getFriendshipsByFriendshipKey_LargerUserIdOrFriendshipKey_SmallerUserId(userId, userId);
		
		return friendships.parallelStream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.FRIENDS)
				.map(friendship -> friendship.getFriendshipKey().getSmallerUserId().equals(userId)
						? friendship.getFriendshipKey().getLargerUserId()
						: friendship.getFriendshipKey().getSmallerUserId())
				.collect(Collectors.toList());
	}
	
	
	public List<Long> getFriends(User user) {
		return getFriends(user.getId());
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
