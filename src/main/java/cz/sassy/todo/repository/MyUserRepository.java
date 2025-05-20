package cz.sassy.todo.repository;

import cz.sassy.todo.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for MyUser entity.
 * This interface extends JpaRepository to provide CRUD operations for MyUser.
 * It also includes a custom method to find a user by their username.
 */

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);

}
