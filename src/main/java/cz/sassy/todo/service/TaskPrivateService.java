package cz.sassy.todo.service;

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
 * Service class for managing tasks in private mode.
 * This class provides methods to create, update, delete, and retrieve tasks.
 * It also includes methods to get completed and uncompleted tasks for the current user.
 */

@Service
public class TaskPrivateService {
    private final TaskRepository taskRepository;
    private final MyUserRepository myUserRepository;

    public TaskPrivateService(TaskRepository taskRepository, MyUserRepository myUserRepository) {
        this.taskRepository = taskRepository;
        this.myUserRepository = myUserRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return myUserRepository.findByUsername(username).orElseThrow().getId();
    }


    public List<Task> getCompletedTasks() {

        Collator collator = Collator.getInstance(new Locale("cs", "CZ"));

        return taskRepository.findByUserIdAndCompletedTrue(getCurrentUserId()).stream()
                .sorted((task1, task2) -> collator.compare(task1.getTitle(), task2.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Task> getUncompletedTasks() {

        return new ArrayList<>(taskRepository.findByUserIdAndCompletedFalse(getCurrentUserId()));
    }

    public void createTask(String title) {

        String validatedTitle = validateAndTrimTitle(sanitizeInput(title));

        if (validatedTitle == null) {
            return;
        }

        Task task = new Task();
        task.setCompleted(false);
        task.setTitle(validatedTitle);
        task.setUserId(getCurrentUserId());
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void toggleTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public void updateTask(Long id, String title) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        String validatedTitle = validateAndTrimTitle(sanitizeInput(title));

        if (validatedTitle == null) {
            return;
        }

        task.setTitle(validatedTitle);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    private String validateAndTrimTitle(String title) {

        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        String input = title.trim();
        return input.length() > 255 ? input.substring(0, 255) : input;
    }

    private String sanitizeInput(String input) {
        return Jsoup.clean(input, Safelist.basic());
    }
}
