package cz.sassy.todo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

/**
 * Main application class for the Todo application.
 * This class serves as the entry point for the Spring Boot application.
 */

@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {
        run(TodoApplication.class, args);
    }

}
