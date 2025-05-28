package cz.sassy.todo.service;

import cz.sassy.todo.model.Task;
import cz.sassy.todo.repository.TaskRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service class for managing tasks.
 */

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves all tasks that are not assigned to any user.
     *
     * @return a list of tasks that are not assigned to any user
     */
    public List<Task> getCompletedTasks() {
        Collator collator = Collator.getInstance(new Locale("cs", "CZ"));
        return taskRepository.findByUserIdIsNull().stream()
                .filter(Task::isCompleted)
                .sorted((task1, task2) -> collator.compare(task1.getTitle(), task2.getTitle()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all tasks that are not completed and not assigned to any user.
     *
     * @return a list of uncompleted tasks that are not assigned to any user
     */
    public List<Task> getUncompletedTasks() {
        return taskRepository.findByUserIdIsNull().stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }

    /**
     * Creates a new task with the given title.
     * The title is sanitized and validated before saving.
     *
     * @param title the title of the task to be created
     * @return the validated and sanitized title of the created task, or null if invalid
     */
    public String createTask(String title) {

        String validatedTitle = validateAndTrimTitle(sanitizeInput(title));

        if (validatedTitle == null || taskRepository.count() >= 10) {
            return validatedTitle;
        }

        Task task = new Task();
        task.setCompleted(false);
        task.setTitle(validatedTitle);
        taskRepository.save(task);
        return validatedTitle;
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to be deleted
     */
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Toggles the completion status of a task by its ID.
     *
     * @param id the ID of the task whose completion status is to be toggled
     */
    public void toggleTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to be retrieved
     * @return the task with the specified ID, or null if not found
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    /**
     * Updates the title of a task by its ID.
     * The title is sanitized and validated before updating.
     *
     * @param id    the ID of the task to be updated
     * @param title the new title for the task
     */
    public void updateTask(Long id, String title) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        String validatedTitle = validateAndTrimTitle(sanitizeInput(title));

        if (validatedTitle == null) {
            return;
        }

        String sanitizedTitle = sanitizeInput(validatedTitle);
        task.setTitle(sanitizedTitle);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    /**
     * Validates and trims the title of a task.
     * If the title is null or empty, it returns null.
     * If the title exceeds 255 characters, it trims it to 255 characters.
     *
     * @param title the title to be validated and trimmed
     * @return the validated and trimmed title, or null if invalid
     */
    private String validateAndTrimTitle(String title) {

        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        String input = title.trim();
        return input.length() > 255 ? input.substring(0, 255) : input;
    }

    /**
     * Sanitizes the input to prevent XSS attacks and other malicious inputs.
     * It uses Jsoup to clean the input based on a basic safelist.
     *
     * @param input the input string to be sanitized
     * @return the sanitized input string
     */
    private String sanitizeInput(String input) {
        return Jsoup.clean(input, Safelist.basic());
    }
}
