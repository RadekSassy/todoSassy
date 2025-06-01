package cz.sassy.todo.controller;

import cz.sassy.todo.model.Task;
import cz.sassy.todo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller for handling private task-related requests.
 * This controller is used when the user is logged in and wants to manage their tasks privately.
 */
@Controller
@RequestMapping("/private")
public class TaskPrivateController {

    private final TaskService taskService;

    public TaskPrivateController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getTasks(Model model, Principal principal) {

        // Retrieving a list of completed and incomplete tasks
        List<Task> completedTasks = taskService.getCompletedTasks();
        List<Task> uncompletedTasks = taskService.getUncompletedTasks();

        // Adding both lists to the model and setting private mode
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
        // Retrieve the task by ID and add it to the model
        Task task = taskService.getTaskById(id);

        // If the task is not found, redirect to the private tasks page
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
