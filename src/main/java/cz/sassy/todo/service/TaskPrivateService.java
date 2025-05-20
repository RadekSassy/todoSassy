package cz.sassy.todo.service;

import cz.sassy.todo.model.Task;
import cz.sassy.todo.repository.TaskRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service class for managing private tasks.
 */

@Service
public class TaskPrivateService {
    private final TaskRepository taskRepository;

    public TaskPrivateService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getCompletedTasks(String username) {
        System.out.println("Přihlášený uživatel: " + username);

        Collator collator = Collator.getInstance(new Locale("cs", "CZ"));

        return taskRepository.findByUserIdAndCompletedTrue(1L).stream()
                .sorted((task1, task2) -> collator.compare(task1.getTitle(), task2.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Task> getUncompletedTasks(String username) {
        return new ArrayList<>(taskRepository.findByUserIdAndCompletedFalse(1L));
    }

    public void createTask(String title) {

        String ValidatedTitle = validateAndTrimTitle(title);

        if (ValidatedTitle == null || taskRepository.count() >= 10000) {
            return;
        }

        String sanitizedTitle = sanitizeInput(ValidatedTitle);

        Task task = new Task();
        task.setCompleted(false);
        task.setTitle(sanitizedTitle);
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

        String validatedTitle = validateAndTrimTitle(title);

        if (validatedTitle == null) {
            return;
        }

        String sanitizedTitle = sanitizeInput(validatedTitle);
        task.setTitle(sanitizedTitle);
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
