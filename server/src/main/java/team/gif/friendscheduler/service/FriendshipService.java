package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.model.Friendship;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.repository.FriendshipRepository;

@Service
public class FriendshipService {
	
	private final FriendshipRepository friendshipRepository;
	
	@Autowired
	public FriendshipService(FriendshipRepository friendshipRepository) {
		this.friendshipRepository = friendshipRepository;
	}
	
	public void addFriendship(User user1, User user2) {
		Friendship friendship = (user1.getId() < user2.getId())
				? new Friendship(user1, user2)
				: new Friendship(user2, user1);
	}
	
}
