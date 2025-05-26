package cz.sassy.todo;

import cz.sassy.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class TodoApplicationTests {

    /**
     * Tests whether the application context loads correctly.
     */
    @Test
    void contextLoads() {
    }

    /**
     * Service for managing tasks.
     */
    @Autowired
    private TaskService taskService;

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

}
