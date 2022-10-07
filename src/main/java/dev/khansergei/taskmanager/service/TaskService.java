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

    public Optional<?> getTasks(Authentication auth) {
        if(auth.getName().isEmpty())
            return Optional.empty();
        return Optional.of(taskDao.getTasks(auth.getName()));
    }

    public TaskDto addTask(String header, String description, LocalDate deadline) {
        return taskDao.addTask(TaskDto.builder()
                .header(header)
                .description(description)
                .deadline(deadline)
                .build());
    }

    public Optional<?> findTaskById(Long id, Authentication auth) {
        if (auth.getName().isEmpty() || !taskDao.isTaskExist(id) || !taskDao.isTaskOwner(id, auth))
            return Optional.of("No such task");
        return Optional.of(taskDao.findTaskById(id, auth));
    }

    public Optional<?> changeState(Long id, Authentication auth) {
        return Optional.of(taskDao.changeState(id, auth));
    }
}
