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
	
	public List<User> getFriends(User user) {
		List<Friendship> friendships = friendshipRepository.getFriendshipsByFriendshipKey_LargerUserId_IdOrFriendshipKey_SmallerUserId_Id(user.getId(), user.getId());
		
		return friendships.parallelStream()
				.map(friendship -> friendship.getFriendshipKey().getSmallerUserId().getId().equals(user.getId())
						? friendship.getFriendshipKey().getLargerUserId()
						: friendship.getFriendshipKey().getSmallerUserId())
				.collect(Collectors.toList());
	}
	
	public void addFriendship(User requester, User target) {
		
		User smaller = (requester.getId() < target.getId()) ? requester : target;
		User larger = (requester.getId() < target.getId()) ? target : requester;
		
		Optional<Friendship> current = friendshipRepository.getFriendshipByFriendshipKey(new FriendshipKey(smaller, larger));
		
		// No relationship exists between the users
		if (current.isEmpty()) {
			FriendshipStatus status = (smaller == requester) ? FriendshipStatus.AWAITING_LARGER_ID_APPROVAL : FriendshipStatus.AWAITING_SMALLER_ID_APPROVAL;
			Friendship friendship = new Friendship(smaller, larger, status);
			friendshipRepository.save(friendship);
			return;
		}
		
		Friendship friendship = current.get();
		
		// Smaller ID is sending request
		if (smaller == requester) {
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
			
		} else {
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
	
}
