package com.example.todo.controllers;

import com.example.todo.dto.TaskRequest;
import com.example.todo.entities.Task;
import com.example.todo.entities.User;
import com.example.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/list")
    public List<Task> getTasks(@AuthenticationPrincipal User user) {
        return taskService.getTasks(user.getId());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@AuthenticationPrincipal User user, @RequestBody @Validated TaskRequest taskRequest, BindingResult validationResult) {
        return taskService.createTask(user.getId(), taskRequest);
    }

    @PutMapping("/{id_task}")
    public Task updateTask(@AuthenticationPrincipal User user, @PathVariable Long id_task, @RequestBody @Validated TaskRequest taskRequest, BindingResult validationResult) {
        return taskService.updateTask(user.getId(), id_task, taskRequest);
    }

    @DeleteMapping("/{id_task}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@AuthenticationPrincipal User user, @PathVariable Long id_task) {
        taskService.deleteTask(user.getId(), id_task);
    }
}
