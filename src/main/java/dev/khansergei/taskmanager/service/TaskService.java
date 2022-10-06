package dev.khansergei.taskmanager.service;

import dev.khansergei.taskmanager.dao.TaskDao;
import dev.khansergei.taskmanager.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskDao taskDao;

    public List<TaskDto> getTasksByEmail(String email) {
        return taskDao.getTasksByEmail(email);
    }

    public List<TaskDto> getAllTasks() {
        return taskDao.getAllTasks();
    }

}
