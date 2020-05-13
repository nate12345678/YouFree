package team.gif.friendscheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.exception.UnauthorizedException;
import team.gif.friendscheduler.model.Token;
import team.gif.friendscheduler.model.TokenListing;
import team.gif.friendscheduler.repository.TokenRepository;

import java.util.Base64;

@Service
public class AuthService {
	
	@Value("${jwt.secret}")
	private String secret;
	
	private final PasswordEncoder tokenEncoder;
	private final TokenRepository tokenRepository;
	
	@Autowired
	public AuthService(PasswordEncoder tokenEncoder, TokenRepository tokenRepository) {
		this.tokenEncoder = tokenEncoder;
		this.tokenRepository = tokenRepository;
	}
	
	
	public Long getUserIdFromToken(String token) {
		TokenListing sessionToken = tokenRepository.getTokenByToken(token).orElseThrow(UnauthorizedException::new);
		return sessionToken.getUserId();
	}
	
	
	public String generateSessionToken(Long userId) {
		TokenListing sessionToken = new TokenListing(userId, userId + "");
		tokenRepository.save(sessionToken);
		
		return sessionToken.getToken();
	}
	
	
	public String generateSessionToken2(Long userId) {
		Token token = new Token(userId);
		
		String unsigned = Base64.getUrlEncoder().encodeToString(token.getHeader().getBytes())
				+ "."
				+ Base64.getUrlEncoder().encodeToString(token.getPayload().getBytes());
		
		String signature = Base64.getUrlEncoder().encodeToString(tokenEncoder.encode(unsigned).getBytes());
		String signedToken = unsigned + "." + signature;
		
		TokenListing tokenListing = new TokenListing(userId, signedToken);
		tokenRepository.save(tokenListing);
		
		return signedToken;
	}
	
	
	public void revokeToken(String token) {
		tokenRepository.deleteTokenByToken(token);
	}
	
}
