package cz.sassy.todo.controller;

import cz.sassy.todo.model.Task;
import cz.sassy.todo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
//@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getTasks(Model model) {

        List<Task> completedTasks = taskService.getCompletedTasks();
        List<Task> uncompletedTasks = taskService.getUncompletedTasks();

        // Přidání obou seznamů do modelu
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("uncompletedTasks", uncompletedTasks);
        return "tasks";
    }

    @PostMapping
    public String createTask(@RequestParam String title) {
        taskService.createTask(title);
        return "redirect:/";
    }
    @GetMapping("/login")
    public String loginTasks() {
        return "custom_login";
    }

    @GetMapping("/{id}/delete")
    public String deleteTasks(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/toggle")
    public String toggleTasks(@PathVariable Long id) {
        taskService.toggleTask(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/update")
    public String updateTasks(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "update";
    }

    @PostMapping("/update")
    public String updateTask(@RequestParam Long id, @RequestParam String title) {
        taskService.updateTask(id, title);
        return "redirect:/";
    }

}
