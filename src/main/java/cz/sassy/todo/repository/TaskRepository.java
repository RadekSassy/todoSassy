package cz.sassy.todo.repository;

import cz.sassy.todo.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Task entity.
 * This interface extends JpaRepository to provide CRUD operations for Task.
 * It does not include any custom methods, but can be extended in the future if needed.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds all tasks that are not assigned to any user.
     * This method is useful for retrieving public tasks that are not associated with a specific user.
     *
     * @return a list of tasks with userId as null
     */
    List<Task> findByUserIdIsNull();

    /**
     * Finds all tasks that are assigned to a specific user and are completed.
     *
     * @param userId the ID of the user
     * @return a list of completed tasks for the specified user
     */
    List<Task> findByUserIdAndCompletedTrue(Long userId);

    /**
     * Finds all tasks that are assigned to a specific user and are not completed.
     *
     * @param userId the ID of the user
     * @return a list of uncompleted tasks for the specified user
     */
    List<Task> findByUserIdAndCompletedFalse(Long userId);

    /**
     * Deletes all tasks that are not assigned to any user.
     * This method is transactional to ensure that the deletion is performed atomically.
     * This is useful for cleaning up tasks that are not associated with any user for testing purposes.
     */
    @Transactional
    void deleteByUserIdIsNull();

}
