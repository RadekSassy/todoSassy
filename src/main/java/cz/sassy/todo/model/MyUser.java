package cz.sassy.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * MyUser is an entity class that represents a user in the application.
 * It contains fields for id, username, password, and role.
 */

@Entity
@Data
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role; // ADMIN, USER

}
