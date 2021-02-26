package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.gif.friendscheduler.model.TokenListing;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<TokenListing, Long> {

	Optional<TokenListing> getTokenByUserId(Long userId);
	Optional<TokenListing> getTokenByToken(String token);
	void deleteTokenByToken(String token);

}
