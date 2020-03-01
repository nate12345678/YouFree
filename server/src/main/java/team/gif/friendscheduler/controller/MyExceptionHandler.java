package team.gif.friendscheduler.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.gif.friendscheduler.exception.AccessDeniedException;
import team.gif.friendscheduler.exception.FriendRequestException;
import team.gif.friendscheduler.exception.FriendshipNotFoundException;
import team.gif.friendscheduler.exception.IncorrectCredentialsException;
import team.gif.friendscheduler.exception.IntervalNotFoundException;
import team.gif.friendscheduler.exception.InvalidFieldException;
import team.gif.friendscheduler.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyExceptionHandler {
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(IncorrectCredentialsException.class)
	public ResponseEntity<String> handleIncorrectCredentialsException(IncorrectCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}
	
	
	// TODO: Ensure this never gets thrown. Should be eliminated by handleValidationException()
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handlePSQLException(ConstraintViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getConstraintName());
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	
	// TODO: Can this be eliminated using @Valid annotations?
	@ExceptionHandler(InvalidFieldException.class)
	public ResponseEntity<String> handleInvalidFieldException(InvalidFieldException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(IntervalNotFoundException.class)
	public ResponseEntity<String> handleIntervalNotFoundException(IntervalNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(FriendshipNotFoundException.class)
	public ResponseEntity<String> handleFriendshipNotFoundException(FriendshipNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(FriendRequestException.class)
	public ResponseEntity<String> handleFriendshipRequestException(FriendRequestException ex) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
	}

}
