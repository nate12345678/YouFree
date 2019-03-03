package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.UserRepository;
import team.gif.friendscheduler.exception.IncorrectCredentialsException;
import team.gif.friendscheduler.exception.UserNotFoundException;
import team.gif.friendscheduler.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	
	public Long getIdFromToken(Long token) {
		return token;
	}
	
	
	public Long generateSessionToken(Long id) {
		return id;
	}
	
	
	/**
	 * Checks if there exists a username with the given password.
	 * Returns the User associated with these credentials.
	 *
	 * @param username The username of the user to authenticate.
	 * @param password The password of the user to authenticate.
	 * @return The User associated with the given credentials.
	 * @throws UserNotFoundException If no user is found with the given username.
	 * @throws IncorrectCredentialsException If the given password does not match the password associated with the user.
	 */
	public User authenticate(String username, String password)
			throws UserNotFoundException, IncorrectCredentialsException {
		
		User target = userRepository
				.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
		
		if (!password.equals(target.getPassword())) {
			throw new IncorrectCredentialsException("Authentication failed; incorrect password.");
		}
		
		return target;
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
	 * @param snowflake The Discord snowflake of the user to retrieve. Processed second.
	 * @param username The username of the user to retrieve. Processed third.
	 * @param email The email of the user to retrieve. Processed last.
	 * @return A User corresponding to the first matched attribute, or null if all given attributes are null.
	 * @throws UserNotFoundException If an attribute is non-null and no matching user is found.
	 */
	public User queryUsers(Long id, Long snowflake, String username, String email) throws UserNotFoundException {
		
		if (id != null) {
			return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		}
		
		if (snowflake != null) {
			return userRepository.findUserByDiscordSnowflake(snowflake).orElseThrow(() -> new UserNotFoundException(snowflake));
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
	 * Updates the user (of specified ID) with the information contained in newInfo.
	 * Only password, discordSnowflake, and email are eligible to be updated. Any
	 * field in newInfo that contains a null value remains unaffected.
	 *
	 * @param id The ID of the user to update.
	 * @param newInfo The new information to assign to the user.
	 * @return The User after updated information is applied.
	 * @throws UserNotFoundException If no user is associated with the given ID.
	 */
	public User updateUser(Long id, User newInfo) throws UserNotFoundException {
		User target = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		
		if (newInfo.getDiscordSnowflake() != null)
			target.setDiscordSnowflake(newInfo.getDiscordSnowflake());
		
		if (newInfo.getPassword() != null) {
			target.setPassword(newInfo.getPassword());
		}
		
		if (newInfo.getEmail() != null)
			target.setEmail(newInfo.getEmail());
		
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
