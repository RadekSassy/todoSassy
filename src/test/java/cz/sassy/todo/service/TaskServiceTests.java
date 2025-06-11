package cz.sassy.todo.service;

import cz.sassy.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * TaskServiceTests is a test class for testing the TaskService functionality.
 * It includes tests for creating tasks with various conditions.
 */
@SpringBootTest
class TaskServiceTests {

    /**
     * Service for managing tasks.
     */
    @Autowired
    private TaskService taskService;

    /**
     * Repository for managing task data.
     * This is used to interact with the database for task-related operations.
     */
    @Autowired
    private TaskRepository taskRepository;

    /**
     * Sets up the test environment by clearing tasks that are not assigned to any user.
     * This ensures that each test starts with a clean slate.
     */
    @BeforeEach
    void setUp() {
        taskRepository.deleteByUserIdIsNull();
    }

    /**
     * Tests the task creation functionality.
     * It checks if a task can be created with a valid title.
     */
    @Test
    void testTaskCreation() {
        String task = taskService.createTask("New Task Created");
        assertEquals("New Task Created", task);
    }

    /**
     * Tests the task creation functionality.
     * Ensures that an empty or null task is not created.
     */
    @Test
    void testEmptyTaskCreation() {
        String task = taskService.createTask("");
        assertNull(task);

        String nullTask = taskService.createTask(null);
        assertNull(nullTask);
    }

    /**
     * Tests the task creation functionality.
     * Ensures that a task is correctly trimmed.
     */
    @Test
    void testTrimmedTaskTitle() {
        String task = taskService.createTask("   New Task Trimmed   ");
        assertEquals("New Task Trimmed", task);
    }

    /**
     * Tests the task creation functionality.
     * Ensures that a task with a title containing special characters is sanitized.
     */
    @Test
    void testMaliciousScriptInjection() {
        String task = taskService.createTask("<script>alert('Hacked!');</script>");
        assertNull(task, "Sanitization should return null if the input is malicious.");
    }

    /**
     * Tests the task creation functionality.
     * Ensures that a task with a title exceeding the character limit is not created.
     */
    @Test
    void testSQLInjectionAttempt() {
        String task = taskService.createTask("Robert'); DROP TABLE Tasks; --");
        assertEquals("Robert'); DROP TABLE Tasks; --", task, "SQL injection attempt should not alter the task title.");
    }
}
