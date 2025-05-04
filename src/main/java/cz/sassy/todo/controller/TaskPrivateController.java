package cz.sassy.todo.controller;

import cz.sassy.todo.model.Task;
import cz.sassy.todo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling task-related requests in private mode.
 */

@Controller
@RequestMapping("/private")
public class TaskPrivateController {

    private final TaskService taskService;

    public TaskPrivateController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getTasks(Model model) {
        List<Task> completedTasks = taskService.getCompletedTasks();
        List<Task> uncompletedTasks = taskService.getUncompletedTasks();

        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("uncompletedTasks", uncompletedTasks);
        model.addAttribute("privateMode", true);
        return "tasks";
    }

    @PostMapping("/create")
    public String createTask(@RequestParam String title) {
        taskService.createTask(title);
        return "redirect:/private";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/private";
    }

    @GetMapping("/{id}/toggle")
    public String toggleTask(@PathVariable Long id) {
        taskService.toggleTask(id);
        return "redirect:/private";
    }

    @GetMapping("/{id}/update")
    public String updateTasks(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        model.addAttribute("privateMode", true);
        return "update";
    }

    @PostMapping("/update")
    public String updateTask(@RequestParam Long id, @RequestParam String title) {
        taskService.updateTask(id, title);
        return "redirect:/private";
    }
}
