package cz.sassy.todo.repository;

import cz.sassy.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Task entity.
 * This interface extends JpaRepository to provide CRUD operations for Task.
 * It does not include any custom methods, but can be extended in the future if needed.
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
