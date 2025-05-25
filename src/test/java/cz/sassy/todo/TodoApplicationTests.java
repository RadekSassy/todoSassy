package cz.sassy.todo;

import cz.sassy.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TodoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private TaskService taskService;

    @Test
    void testTaskCreation() {
        String task = taskService.createTask("Nový úkol");
        assertEquals("Nový úkol", task); // Ověříme, že úkol byl správně vytvořen
    }

}
