package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.model.request.NewUser;
import team.gif.friendscheduler.repository.UserRepository;
import team.gif.friendscheduler.exception.IncorrectCredentialsException;
import team.gif.friendscheduler.exception.UserNotFoundException;
import team.gif.friendscheduler.model.User;

import java.util.List;

@Service
public class UserService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	
	@Autowired
	UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	
	/**
	 * Checks if there exists a username with the given password.
	 * Returns the User associated with these credentials.
	 *
	 * @param email The email of the user to authenticate.
	 * @param password The password of the user to authenticate.
	 * @return The User associated with the given credentials.
	 * @throws UserNotFoundException If no user is found with the given username.
	 * @throws IncorrectCredentialsException If the given password does not match the password associated with the user.
	 */
	public User authenticate(String email, String password)
			throws UserNotFoundException, IncorrectCredentialsException {
		
		User target = userRepository
				.findUserByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(email));
		
		if (!passwordEncoder.matches(password, target.getPassword())) {
			throw new IncorrectCredentialsException("Authentication failed; incorrect password.");
		}
		
		return target;
	}
	
	
	/**
	 * Saves a new User to the repository.
	 *
	 * @param newUser The user to be saved.
	 */
	public User createUser(NewUser newUser) {
		User user = new User(newUser.getUsername(), passwordEncoder.encode(newUser.getPassword()), newUser.getEmail());
		userRepository.save(user);
		return user;
	}
	
	
	/**
	 * Gets the user with the specified ID
	 *
	 * @param id The ID of the user to retrieve.
	 * @return The specified user.
	 * @throws UserNotFoundException If no user is associated with the given ID.
	 */
	public User getUser(Long id) throws UserNotFoundException {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}
	
	
	/**
	 * Attempts to find a user with one of the given attributes. If multiple are specified, the
	 * first attribute to match (as given in the ordering below) is the one which returns the match.
	 *
	 * If an argument is non-null and no match is found, UserNotFoundException is thrown.
	 *
	 * If all arguments are null, a null User is returned.
	 *
	 * @param id The ID of the user to retrieve. Processed first.
	 * @param username The username of the user to retrieve. Processed third.
	 * @param email The email of the user to retrieve. Processed last.
	 * @return A User corresponding to the first matched attribute, or null if all given attributes are null.
	 * @throws UserNotFoundException If an attribute is non-null and no matching user is found.
	 */
	public User queryUsers(Long id, String username, String email) throws UserNotFoundException {
		
		if (id != null) {
			return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		}
		
		if (username != null) {
			return userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		}
		
		if (email != null) {
			return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
		}
		
		return null;
	}
	
	
	/**
	 * Searches for users whose usernames contain the specified substring.
	 *
	 * @param query The substring that a username should contain.
	 * @return All users whose usernames contain the substring.
	 */
	public List<User> searchUsers(String query) {
		return userRepository.searchUsers(query);
	}
	
	
	/**
	 * Updates the user (of specified ID) with the information contained in newInfo.
	 * Only password and email are eligible to be updated.
	 * Any field in newInfo that contains a null value remains unaffected.
	 *
	 * @param id The ID of the user to update.
	 * @param newInfo The new information to assign to the user.
	 * @return The User after updated information is applied.
	 * @throws UserNotFoundException If no user is associated with the given ID.
	 */
	public User updateUser(Long id, NewUser newInfo) throws UserNotFoundException {
		User target = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		
		if (newInfo.getUsername() != null && !newInfo.getUsername().isBlank()) {
			target.setUsername(newInfo.getUsername());
		}
		
		if (newInfo.getPassword() != null && !newInfo.getPassword().isBlank()) {
			target.setPassword(passwordEncoder.encode(newInfo.getPassword()));
		}
		
		if (newInfo.getEmail() != null && !newInfo.getPassword().isBlank()) {
			target.setEmail(newInfo.getEmail());
		}
		
		userRepository.save(target);
		
		return target;
	}
	
	
	/**
	 * Deletes a user from the database.
	 *
	 * @param id The ID of the user to delete.
	 * @throws UserNotFoundException If no user is associated with the given ID.
	 */
	public void deleteUser(Long id) throws UserNotFoundException {
		if (!userRepository.existsById(id)) throw new UserNotFoundException(id);
		userRepository.deleteById(id);
	}

}
