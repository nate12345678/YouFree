package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.exception.UnauthorizedException;
import team.gif.friendscheduler.model.Token;
import team.gif.friendscheduler.repository.TokenRepository;

@Service
public class AuthService {
	
	private final TokenRepository tokenRepository;
	
	@Autowired
	public AuthService(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
	
	public Long getUserIdFromToken(String token) {
		Token sessionToken = tokenRepository.getTokenByToken(token).orElseThrow(UnauthorizedException::new);
		return sessionToken.getUserId();
	}
	
	
	public String generateSessionToken(Long userId) {
		Token sessionToken = new Token(userId, userId + "");
		tokenRepository.save(sessionToken);
		
		return sessionToken.getToken();
	}
	
	
	public void revokeToken(String token) {
		tokenRepository.deleteTokenByToken(token);
	}
	
}
