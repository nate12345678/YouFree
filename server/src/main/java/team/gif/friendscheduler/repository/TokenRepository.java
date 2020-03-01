package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {

	Optional<Token> getTokenByUserId(Long userId);
	Optional<Token> getTokenByToken(String token);
	void deleteTokenByToken(String token);

}
