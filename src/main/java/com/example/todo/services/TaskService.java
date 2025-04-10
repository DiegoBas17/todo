package com.example.todo.services;

import com.example.todo.dto.TaskRequest;
import com.example.todo.entities.Task;
import com.example.todo.entities.User;
import com.example.todo.exceptions.BadRequestException;
import com.example.todo.exceptions.NotFoundException;
import com.example.todo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public List<Task> getTasks(Long id_user) {
        return taskRepository.findByUserId(id_user);
    }

    public Task createTask(Long id_user, TaskRequest task) {
        User user = userService.findById(id_user);
        Task newTask = new Task();
        newTask.setTitle(task.title());
        newTask.setDescription(task.description());
        try {
            newTask.setCompleted(controlloBoolean(task.completed()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Completed puo essere solo true o false");
        }
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setUser(user);
        return taskRepository.save(newTask);
    }

    public Task updateTask(Long id_user, Long id_task, TaskRequest taskRequest) {
        User user = userService.findById(id_user);
        Task task = taskRepository.findById(id_task)
                .orElseThrow(() -> new NotFoundException("Task con id: " + id_task + " non trovato."));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Non autorizzato a modificare questo task");
        }
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        try {
            task.setCompleted(controlloBoolean(taskRequest.completed()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Completed puo essere solo true o false");
        }
        return taskRepository.save(task);
    }

    public void deleteTask(Long id_user, Long id_task) {
        User user = userService.findById(id_user);
        Task task = taskRepository.findById(id_task)
                .orElseThrow(() -> new NotFoundException("Task con ID: " + id_task + " non trovato"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Non autorizzato a eliminare questo task");
        }
        taskRepository.delete(task);
    }

    private boolean controlloBoolean(String string) {
        if ("true".equalsIgnoreCase(string)) return true;
        if ("false".equalsIgnoreCase(string)) return false;
        throw new BadRequestException("Il campo 'completed' può essere solo 'true' o 'false'");
    }

}
