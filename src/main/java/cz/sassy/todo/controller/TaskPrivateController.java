package cz.sassy.todo.controller;

import cz.sassy.todo.model.Task;
import cz.sassy.todo.service.TaskPrivateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller for handling task-related requests in private mode.
 */

@Controller
@RequestMapping("/private")
public class TaskPrivateController {

    private final TaskPrivateService taskPrivateService;

    public TaskPrivateController(TaskPrivateService taskPrivateService) {
        this.taskPrivateService = taskPrivateService;
    }

    @GetMapping
    public String getTasks(Model model, Principal principal) {

        String username = principal.getName();

        List<Task> completedTasks = taskPrivateService.getCompletedTasks();
        List<Task> uncompletedTasks = taskPrivateService.getUncompletedTasks();

        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("uncompletedTasks", uncompletedTasks);
        model.addAttribute("privateMode", true);
        return "tasks";
    }

    @PostMapping("/create")
    public String createTask(@RequestParam String title) {
        taskPrivateService.createTask(title);
        return "redirect:/private";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskPrivateService.deleteTask(id);
        return "redirect:/private";
    }

    @GetMapping("/{id}/toggle")
    public String toggleTask(@PathVariable Long id) {
        taskPrivateService.toggleTask(id);
        return "redirect:/private";
    }

    @GetMapping("/{id}/update")
    public String updateTasks(@PathVariable Long id, Model model) {
        Task task = taskPrivateService.getTaskById(id);
        model.addAttribute("task", task);
        model.addAttribute("privateMode", true);
        return "update";
    }

    @PostMapping("/update")
    public String updateTask(@RequestParam Long id, @RequestParam String title) {
        taskPrivateService.updateTask(id, title);
        return "redirect:/private";
    }
}
