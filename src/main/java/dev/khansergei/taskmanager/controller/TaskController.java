package dev.khansergei.taskmanager.controller;

import dev.khansergei.taskmanager.dto.TaskDto;
import dev.khansergei.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{user_email}")
    public ResponseEntity<?> getTasksByEmail(@PathVariable String user_email, Authentication auth) {
        return new ResponseEntity<>(taskService.getTasksByEmail(user_email, auth), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<TaskDto> addTask(@RequestParam String header,
                                           @RequestParam String description,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        return new ResponseEntity<>(taskService.addTask(header, description, deadline), HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<?> findTaskById(@PathVariable Long id, Authentication auth) {
        return new ResponseEntity<>(taskService.findTaskById(id, auth), HttpStatus.OK);
    }

    @GetMapping("/{id}/{state}")
    public ResponseEntity<?> changeState(@PathVariable Long id, @PathVariable String state, Authentication auth) {
        return new ResponseEntity<>(taskService.changeState(id, state, auth), HttpStatus.OK);
    }
}
