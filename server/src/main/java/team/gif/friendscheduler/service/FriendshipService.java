package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.repository.FriendshipRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
	
	private final FriendshipRepository friendshipRepository;
	
	@Autowired
	public FriendshipService(FriendshipRepository friendshipRepository) {
		this.friendshipRepository = friendshipRepository;
	}
	
	public List<User> getFriends(User user) {
		List<Friendship> friendships = friendshipRepository.findFriendshipsByLargerUserIdOrSmallerUserId(user, user)
				.orElseThrow(() -> new RuntimeException("Couldn't get friends list for " + user.getId()));
		
		return friendships.parallelStream()
				.map(friendship -> friendship.getSmallerUserId().getId().equals(user.getId())
						? friendship.getLargerUserId()
						: friendship.getSmallerUserId())
				.collect(Collectors.toList());
	}
	
	public void addFriendship(User user1, User user2) {
		Friendship friendship = (user1.getId() < user2.getId())
				? new Friendship(user1, user2)
				: new Friendship(user2, user1);
		
		friendshipRepository.save(friendship);
	}
	
}
