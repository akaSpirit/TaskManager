package dev.khansergei.taskmanager.service;

import dev.khansergei.taskmanager.dao.TaskDao;
import dev.khansergei.taskmanager.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskDao taskDao;

    public Optional getTasksByEmail(String email, Authentication auth) {
        if (!auth.getName().equals(email))  //check is task(s) owner
            return Optional.empty();
        return Optional.of(taskDao.getTasksByEmail(email));
    }

    public TaskDto addTask(String header, String description, LocalDate deadline) {
        return taskDao.addTask(TaskDto.builder()
                .header(header)
                .description(description)
                .deadline(deadline)
                .build());
    }

    public Optional findTaskById(Long id, Authentication auth) {
        if (auth.getName().isEmpty())
            return Optional.empty();
        return Optional.of(taskDao.findTaskById(id, auth));
    }

    public Optional changeState(Long id, String state, Authentication auth) {
        return Optional.of(taskDao.changeState(id, state, auth));
//        return "changed state";
    }
}
