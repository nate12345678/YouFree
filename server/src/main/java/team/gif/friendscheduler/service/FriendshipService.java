package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.exception.FriendRequestException;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.FriendshipKey;
import team.gif.friendscheduler.model.FriendshipStatus;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.model.response.Relationship;
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
	
	
	public Relationship getRelationship(Long requesterId, Long targetId) {
		long smaller = Math.min(requesterId, targetId);
		long larger = Math.max(requesterId, targetId);
		
		FriendshipKey key = new FriendshipKey(smaller, larger);
		Optional<Friendship> optional = friendshipRepository.getFriendshipByFriendshipKey(key);
		
		// No relation exists
		if (optional.isEmpty()) {
			return Relationship.NONE;
		}
		
		Friendship friendship = optional.get();
		switch (friendship.getStatus()) {
			case FRIENDS:
				return Relationship.FRIENDS;
			case AWAITING_SMALLER_ID_APPROVAL:
				return (requesterId == smaller) ? Relationship.PENDING : Relationship.SENT;
			case AWAITING_LARGER_ID_APPROVAL:
				return (requesterId == smaller) ? Relationship.SENT : Relationship.PENDING;
			case BOTH_BLOCKED:
				return Relationship.MUTUAL_BLOCK;
			case SMALLER_BLOCKED_LARGER:
				return (requesterId == smaller) ? Relationship.BLOCKED : Relationship.HIDDEN;
			case LARGER_BLOCKED_SMALLER:
				return (requesterId == smaller) ? Relationship.HIDDEN : Relationship.BLOCKED;
		}
		
		return Relationship.NONE; // Should never get here as long as above switch covers every case
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
	
	
	public List<Long> getPendingRequests(Long userId) {
		
		// When user is larger ID, get requests awaiting approval of larger ID
		List<Friendship> firstList = friendshipRepository.getFriendshipsByFriendshipKey_LargerUserId(userId);
		List<Long> result = firstList.stream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.AWAITING_LARGER_ID_APPROVAL)
				.map(friendship -> friendship.getFriendshipKey().getSmallerUserId())
				.collect(Collectors.toList());
		
		// When user is smaller ID, get requests awaiting approval of smaller ID
		List<Friendship> secondList = friendshipRepository.getFriendshipsByFriendshipKey_SmallerUserId(userId);
		result.addAll(
				secondList.stream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.AWAITING_SMALLER_ID_APPROVAL)
				.map(friendship -> friendship.getFriendshipKey().getLargerUserId())
				.collect(Collectors.toList())
		);
		
		return result;
	}
	
	
	public List<Long> getSentRequests(Long userId) {
		
		// When user is larger ID, get requests awaiting approval of smaller ID
		List<Friendship> firstList = friendshipRepository.getFriendshipsByFriendshipKey_LargerUserId(userId);
		List<Long> result = firstList.stream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.AWAITING_SMALLER_ID_APPROVAL)
				.map(friendship -> friendship.getFriendshipKey().getSmallerUserId())
				.collect(Collectors.toList());
		
		// When user is smaller ID, get requests awaiting approval of smaller ID
		List<Friendship> secondList = friendshipRepository.getFriendshipsByFriendshipKey_SmallerUserId(userId);
		result.addAll(
				secondList.stream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.AWAITING_LARGER_ID_APPROVAL)
				.map(friendship -> friendship.getFriendshipKey().getLargerUserId())
				.collect(Collectors.toList())
		);
		
		return result;
	}
	
	
	public List<Long> getBlockedUsers(Long userId) {
		
		// When user is larger ID, get relations in which they've blocked the smaller user ID
		List<Friendship> firstList = friendshipRepository.getFriendshipsByFriendshipKey_LargerUserId(userId);
		List<Long> result = firstList.stream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.LARGER_BLOCKED_SMALLER
						|| friendship.getStatus() == FriendshipStatus.BOTH_BLOCKED)
				.map(friendship -> friendship.getFriendshipKey().getSmallerUserId())
				.collect(Collectors.toList());
		
		// When user is smaller ID, get relations in which they've blocked the larger user ID
		List<Friendship> secondList = friendshipRepository.getFriendshipsByFriendshipKey_SmallerUserId(userId);
		result.addAll(
				secondList.stream()
				.filter(friendship -> friendship.getStatus() == FriendshipStatus.SMALLER_BLOCKED_LARGER
						|| friendship.getStatus() == FriendshipStatus.BOTH_BLOCKED)
				.map(friendship -> friendship.getFriendshipKey().getLargerUserId())
				.collect(Collectors.toList())
		);
		
		return result;
	}
	
	
	public void addFriendship(Long requesterId, Long targetId) {
		
		long smaller = Math.min(requesterId, targetId);
		long larger = Math.max(requesterId, targetId);
		
		// If no relationship exists between the users
		Optional<Friendship> current = friendshipRepository.getFriendshipByFriendshipKey(new FriendshipKey(smaller, larger));
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
				case SMALLER_BLOCKED_LARGER:
					// Unblocking and sending request
					friendship.setStatus(FriendshipStatus.AWAITING_LARGER_ID_APPROVAL);
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
				case LARGER_BLOCKED_SMALLER:
					// Unblocking and sending request
					friendship.setStatus(FriendshipStatus.AWAITING_SMALLER_ID_APPROVAL);
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
	}
	
	
	public void block(Long requesterId, Long targetId) {
		
		long smaller = Math.min(requesterId, targetId);
		long larger = Math.max(requesterId, targetId);
		
		// If no relationship exists between the users
		Optional<Friendship> current = friendshipRepository.getFriendshipByFriendshipKey(new FriendshipKey(smaller, larger));
		if (current.isEmpty()) {
			FriendshipStatus status = (smaller == requesterId) ? FriendshipStatus.SMALLER_BLOCKED_LARGER : FriendshipStatus.LARGER_BLOCKED_SMALLER;
			Friendship friendship = new Friendship(smaller, larger, status);
			friendshipRepository.save(friendship);
			return;
		}
		
		Friendship friendship = current.get();
		
		// Smaller ID is sending request
		if (smaller == requesterId) {
			switch (friendship.getStatus()) {
				case FRIENDS: // Fallthrough
				case AWAITING_LARGER_ID_APPROVAL: // Fallthrough
				case AWAITING_SMALLER_ID_APPROVAL:
					friendship.setStatus(FriendshipStatus.SMALLER_BLOCKED_LARGER);
					friendshipRepository.save(friendship);
					break;
				case LARGER_BLOCKED_SMALLER:
					// Mutually blocking
					friendship.setStatus(FriendshipStatus.BOTH_BLOCKED);
					friendshipRepository.save(friendship);
					break;
				case SMALLER_BLOCKED_LARGER: // Fallthrough
				case BOTH_BLOCKED:
					// Double-sending request
					throw new FriendRequestException("Target user is already blocked");
			}
		} else { // Larger ID is sending request
			switch (friendship.getStatus()) {
				case FRIENDS: // Fallthrough
				case AWAITING_LARGER_ID_APPROVAL: // Fallthrough
				case AWAITING_SMALLER_ID_APPROVAL:
					friendship.setStatus(FriendshipStatus.LARGER_BLOCKED_SMALLER);
					friendshipRepository.save(friendship);
					break;
				case SMALLER_BLOCKED_LARGER:
					// Mutually blocking
					friendship.setStatus(FriendshipStatus.BOTH_BLOCKED);
					friendshipRepository.save(friendship);
					break;
				case LARGER_BLOCKED_SMALLER: // Fallthrough
				case BOTH_BLOCKED:
					// Double-sending request
					throw new FriendRequestException("Target user is already blocked");
			}
		}
	}
	
	
	public void deleteFriendship(Long requesterId, Long targetId) {
		
		long smaller = Math.min(requesterId, targetId);
		long larger = Math.max(requesterId, targetId);
		
		// If no relationship exists between the users
		Optional<Friendship> current = friendshipRepository.getFriendshipByFriendshipKey(new FriendshipKey(smaller, larger));
		if (current.isEmpty()) {
			throw new FriendRequestException("Target user is not a friend");
		}
		
		Friendship friendship = current.get();
		switch (friendship.getStatus()) {
			case AWAITING_LARGER_ID_APPROVAL: // Fallthrough
			case AWAITING_SMALLER_ID_APPROVAL: // Fallthrough
			case FRIENDS:
				// Revoke/reject request or delete friendship
				friendshipRepository.deleteById(friendship.getFriendshipKey());
				break;
			case LARGER_BLOCKED_SMALLER:
				throw new FriendRequestException("Blocked by target user");
			case SMALLER_BLOCKED_LARGER: // Fallthrough
			case BOTH_BLOCKED:
				throw new FriendRequestException("Target user is already blocked");
		}
	}
	
	
	public void unblock(Long requesterId, Long targetId) {
		
		long smaller = Math.min(requesterId, targetId);
		long larger = Math.max(requesterId, targetId);
		
		// If no relationship exists between the users
		Optional<Friendship> current = friendshipRepository.getFriendshipByFriendshipKey(new FriendshipKey(smaller, larger));
		if (current.isEmpty()) {
			throw new FriendRequestException("Target user is not blocked");
		}
		
		Friendship friendship = current.get();
		if (smaller == requesterId) {
			switch (friendship.getStatus()) {
				case FRIENDS: // Fallthrough
				case AWAITING_LARGER_ID_APPROVAL: // Fallthrough
				case AWAITING_SMALLER_ID_APPROVAL: // Fallthrough
				case LARGER_BLOCKED_SMALLER: // Fallthrough
					throw new FriendRequestException("Target user is not blocked");
				case SMALLER_BLOCKED_LARGER:
					friendshipRepository.deleteById(friendship.getFriendshipKey());
					break;
				case BOTH_BLOCKED:
					friendship.setStatus(FriendshipStatus.LARGER_BLOCKED_SMALLER);
					friendshipRepository.save(friendship);
					break;
			}
		} else {
			switch (friendship.getStatus()) {
				case FRIENDS: // Fallthrough
				case AWAITING_LARGER_ID_APPROVAL: // Fallthrough
				case AWAITING_SMALLER_ID_APPROVAL: // Fallthrough
				case SMALLER_BLOCKED_LARGER: // Fallthrough
					throw new FriendRequestException("Target user is not blocked");
				case LARGER_BLOCKED_SMALLER:
					friendshipRepository.deleteById(friendship.getFriendshipKey());
					break;
				case BOTH_BLOCKED:
					friendship.setStatus(FriendshipStatus.SMALLER_BLOCKED_LARGER);
					friendshipRepository.save(friendship);
					break;
			}
		}
	}
	
	
	public void removeUser(Long userId) {
		friendshipRepository.deleteAllByFriendshipKey_LargerUserIdOrFriendshipKey_SmallerUserId(userId, userId);
	}
	
}
