package cz.sassy.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entity class representing a task.
 * This class is mapped to the "tasks" table in the database.
 * It contains fields for task ID, title, completion status, and user ID.
 */

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean completed;
    private Long userId; // Nullable for public tasks
}
