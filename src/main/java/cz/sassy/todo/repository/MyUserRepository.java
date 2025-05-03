package cz.sassy.todo.repository;

import cz.sassy.todo.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * MyUserRepository is a Spring Data JPA repository interface for the MyUser entity.
 * It provides methods to perform CRUD operations and custom queries on the MyUser table.
 */

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
}
