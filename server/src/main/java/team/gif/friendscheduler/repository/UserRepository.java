package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import team.gif.friendscheduler.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findUserByUsername(@Param("username") String username);
	Optional<User> findUserByEmail(@Param("email") String email);

}
