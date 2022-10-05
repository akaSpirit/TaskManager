package dev.khansergei.taskmanager.util;

import dev.khansergei.taskmanager.dto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.khansergei.taskmanager.dao.*;

import java.time.LocalDateTime;
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
        tasks.add(new TaskDto("header1", "description1", LocalDateTime.now(), 1L));
        tasks.add(new TaskDto("header2", "description2", LocalDateTime.now(), 2L));
        tasks.add(new TaskDto("header3", "description3", LocalDateTime.now(), 3L));
        tasks.add(new TaskDto("header4", "description4", LocalDateTime.now(), 4L));
        tasks.add(new TaskDto("header5", "description5", LocalDateTime.now(), 5L));
        return tasks;
    }
}


