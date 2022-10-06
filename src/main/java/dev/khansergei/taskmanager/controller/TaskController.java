package dev.khansergei.taskmanager.controller;

import dev.khansergei.taskmanager.dto.TaskDto;
import dev.khansergei.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{user_email}")
    public ResponseEntity<List<TaskDto>> getTasksByEmail(@PathVariable String user_email) {
        return new ResponseEntity<>(taskService.getTasksByEmail(user_email), HttpStatus.OK);
    }


}
