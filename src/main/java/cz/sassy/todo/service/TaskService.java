package cz.sassy.todo.service;

import cz.sassy.todo.model.MyUser;
import cz.sassy.todo.model.Task;
import cz.sassy.todo.repository.MyUserRepository;
import cz.sassy.todo.repository.TaskRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service class for managing tasks.
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final MyUserRepository myUserRepository;

    /**
     * Constructor for TaskService.
     *
     * @param taskRepository the repository for managing tasks
     */
    public TaskService(TaskRepository taskRepository, MyUserRepository myUserRepository) {
        this.taskRepository = taskRepository;
        this.myUserRepository = myUserRepository;
    }

    /**
     * Retrieves the ID of the currently authenticated user.
     *
     * @return the ID of the current user, or null if no user is authenticated
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // Uživatel není přihlášen
        }

        String username = authentication.getName();
        return myUserRepository.findByUsername(username)
                .map(MyUser::getId)
                .orElse(null);
    }

    /**
     * Retrieves all completed tasks, sorted by title.
     * If the user is not authenticated, it retrieves tasks that are not assigned to any user.
     *
     * @return a list of completed tasks, sorted by title
     */
    public List<Task> getCompletedTasks() {
        Collator collator = Collator.getInstance(new Locale("cs", "CZ"));

        if (getCurrentUserId() == null) {
            return taskRepository.findByUserIdIsNull().stream()
                    .filter(Task::isCompleted)
                    .sorted((task1, task2) -> collator.compare(task1.getTitle(), task2.getTitle()))
                    .collect(Collectors.toList());
        } else {
            return taskRepository.findByUserIdAndCompletedTrue(getCurrentUserId()).stream()
                    .sorted((task1, task2) -> collator.compare(task1.getTitle(), task2.getTitle()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Retrieves all uncompleted tasks for the current user.
     * If the user is not authenticated, it retrieves tasks that are not assigned to any user.
     *
     * @return a list of uncompleted tasks
     */
    public List<Task> getUncompletedTasks() {

        if (getCurrentUserId() == null) {
            return taskRepository.findByUserIdIsNull().stream()
                    .filter(task -> !task.isCompleted())
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>(taskRepository.findByUserIdAndCompletedFalse(getCurrentUserId()));
        }
    }

    /**
     * Creates a new task with the given title.
     * If the title is null, empty, or if there are already 10 tasks, it returns the original title.
     * The title is sanitized and validated to ensure it does not exceed 255 characters.
     *
     * @param title the title of the task to be created
     * @return the validated title of the created task, or the original title if creation fails
     */
    public String createTask(String title) {

        String trimmedTitle = trimTitle(title);

        if (trimmedTitle == null || trimmedTitle.isEmpty() || taskRepository.count() >= 10) {
            return null;
        }

        String validatedTitle = validateLongOfTitle(sanitizeInput(trimmedTitle));

        if (validatedTitle == null || validatedTitle.isEmpty()) {
            return null;
        }

        Task task = new Task();
        task.setCompleted(false);
        task.setTitle(validatedTitle);
        task.setUserId(getCurrentUserId());
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
     * Updates a task's title by its ID.
     * If the title is null or empty, the task will not be updated.
     *
     * @param id    the ID of the task to be updated
     * @param title the new title for the task
     */
    public void updateTask(Long id, String title) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        String trimmedTitle = trimTitle(title);

        if (trimmedTitle == null || trimmedTitle.isEmpty()) {
            return;
        }

        String validatedTitle = validateLongOfTitle(sanitizeInput(trimmedTitle));

        if (validatedTitle == null || validatedTitle.isEmpty()) {
            return;
        }

        task.setTitle(validatedTitle);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    /**
     * Trims the title to remove leading and trailing whitespace.
     * If the title is null or empty, it returns null.
     * If the title exceeds 255 characters, it truncates it to 255 characters.
     *
     * @param title the title string to be trimmed
     * @return the trimmed title string, or null if the input is null or empty
     */
    private String trimTitle(String title) {
        String input = title != null ? title.trim() : null;
        if (input == null || input.isEmpty()) {
            return null;
        }
        return input.length() > 255 ? input.substring(0, 255) : input;
    }

    /**
     * Validates the length of the title.
     * If the title exceeds 255 characters, it truncates it to 255 characters.
     *
     * @param input the input string to be validated
     * @return the validated title string, truncated if necessary
     */
    private String validateLongOfTitle(String input) {
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
