package dev.khansergei.taskmanager.util;

import dev.khansergei.taskmanager.dto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.khansergei.taskmanager.dao.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitDB {
    @Bean
    public String init(UserDao userDao, TaskDao taskDao) {
        taskDao.dropTable();
        userDao.dropTableAuth();
        userDao.dropTableUsers();

        userDao.createTableUsers();
        userDao.createTableAuth();
        taskDao.createTable();

        userDao.addData(addUsers());
        taskDao.addData(addTasks());
        return "init...";
    }

    private List<UserDto> addUsers() {
        List<UserDto> users = new ArrayList<>();
        users.add(new UserDto("username1", "email1@email", "qwe1", true));
        users.add(new UserDto("username2", "email2@email", "qwe2", true));
        users.add(new UserDto("username3", "email3@email", "qwe3", true));
        users.add(new UserDto("username4", "email4@email", "qwe4", true));
        users.add(new UserDto("username5", "email5@email", "qwe5", true));
        return users;
    }

    private List<TaskDto> addTasks() {
        List<TaskDto> tasks = new ArrayList<>();
        tasks.add(new TaskDto("task 1", "description", LocalDate.now(), "email1@email"));
        tasks.add(new TaskDto("task 2", "description", LocalDate.now(), "email1@email"));
        tasks.add(new TaskDto("task 3", "description", LocalDate.now(), "email1@email"));
        tasks.add(new TaskDto("task 4", "description", LocalDate.now(), "email4@email"));
        tasks.add(new TaskDto("task 5", "description", LocalDate.now(), "email5@email"));
        return tasks;
    }
}


