package team.gif.friendscheduler.service;

import team.gif.friendscheduler.exception.InvalidFieldException;
import team.gif.friendscheduler.model.User;

public class FieldValidator {

	public boolean validateUser(User user) throws InvalidFieldException {
		
		validateNonSpacedString(user.getUsername(), "username");
		validateNonSpacedString(user.getPassword(), "password");
		validateNonSpacedString(user.getEmail(), "email");
		validateNonSpacedString(user.getDisplayName(), "displayName");
		
		return true;
	}
	
	
	private void validateNonSpacedString(String field, String fieldName) throws InvalidFieldException {
		if (field == null || field.length() == 0)
			throw new InvalidFieldException("Field <" + fieldName + "> cannot be null/empty.");
		
		if (field.contains(" "))
			throw new InvalidFieldException("Field <" + fieldName + "> cannot contain spaces.");
	}
	
}
