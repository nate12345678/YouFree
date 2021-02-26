package team.gif.friendscheduler.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.exception.ServerConfigurationException;
import team.gif.friendscheduler.exception.UnauthorizedException;
import team.gif.friendscheduler.model.Token;
import team.gif.friendscheduler.model.TokenListing;
import team.gif.friendscheduler.repository.TokenRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// TODO: Can greatly improve efficiency by storing just the signature, and not the whole token string
// The token itself contains all the information you need (basically just the UserID)
// This would reduce the amount of memory in the database and would improve string comparison times
// IF THIS IS DONE, WOULD HAVE TO HASH RETURNED TOKEN TO VERIFY THE USERID MATCHES THE SIGNATURE
@Service
@Transactional
public class AuthService {
	
	private static final Logger logger = LogManager.getLogger(AuthService.class);
	
	@Value("${jwt.secret}")
	private String secret;
	
	private final TokenRepository tokenRepository;
	
	@Autowired
	public AuthService(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
	
	public void validateTokenString(String token) {
		// Ensure token string follows proper format
		String[] tokenParts = token.split("\\.");
		if (tokenParts.length != 3) {
			throw new UnauthorizedException("Malformed token");
		}
		
		// Ensure signature matches content
		String header = tokenParts[0];
		String payload = tokenParts[1];
		String givenSignature = tokenParts[2];
		String actualSignature = generateSignature(header + "." + payload);
		if (!givenSignature.equals(actualSignature)) {
			throw new UnauthorizedException(); // We give generic message so nobody can brute force secret
		}
		
		// Ensure TokenListing exists in table
		tokenRepository.getTokenByToken(token).orElseThrow(UnauthorizedException::new);
	}
	
	
	public Long getUserIdFromToken(String token) {
		TokenListing tokenListing = tokenRepository.getTokenByToken(token).orElseThrow(UnauthorizedException::new);
		return tokenListing.getUserId();
	}
	
	
	// TODO: Fail if a token already exists? -> should be able to log in from multiple devices
	public String generateSessionToken(Long userId) {
		// Generate token with default fields
		Token token = new Token(userId);
		
		String unsigned = Base64.getUrlEncoder().encodeToString(token.getHeader().getBytes())
				+ "."
				+ Base64.getUrlEncoder().encodeToString(token.getPayload().getBytes());
		
		String signedToken = unsigned + "." + generateSignature(unsigned);
		
		TokenListing tokenListing = new TokenListing(userId, signedToken);
		tokenRepository.save(tokenListing);
		
		return signedToken;
	}
	
	
	/**
	 * Generates a Base64 URL-encoded signature with HMAC-SHA256
	 *
	 * @return An HMAC-SHA256 signature
	 */
	private String generateSignature(String message) {
		String signature = "";
		
		try {
			Mac hmac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
//			SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), hmac.getAlgorithm());
			hmac.init(secretKey);
			
			signature = Base64.getUrlEncoder().encodeToString(hmac.doFinal(message.getBytes()));
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("Invalid HMAC algorithm for token signing", e);
			throw new ServerConfigurationException(e);
		} catch (InvalidKeyException e) {
			logger.error("Invalid secret key for token signing", e);
			throw new ServerConfigurationException(e);
		}
		
		return signature;
	}
	
	
	public void revokeToken(String token) {
		tokenRepository.deleteTokenByToken(token);
	}
	
}
