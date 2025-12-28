package com.company.office_portal_api.controller;

import com.company.office_portal_api.model.Task;
import com.company.office_portal_api.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 🔹 Assign task (ADMIN / TL)
    @PostMapping("/assign")
    public Task assignTask(@RequestBody Task task) {
        task.setStatus("PENDING");
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    // 🔹 Get tasks for logged-in user
    @GetMapping("/my")
    public List<Task> myTasks(@RequestParam String email) {
        return taskRepository.findByAssignedTo(email);
    }

    // 🔹 Update task status
    @PostMapping("/update/{id}")
    public Task updateStatus(@PathVariable Long id, @RequestParam String status) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(status);
        return taskRepository.save(task);
    
    }

    @GetMapping("/all")
public List<Task> allTasks() {
    return taskRepository.findAll();
}
}
