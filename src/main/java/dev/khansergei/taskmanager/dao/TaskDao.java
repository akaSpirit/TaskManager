package dev.khansergei.taskmanager.dao;

import dev.khansergei.taskmanager.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class TaskDao {
    private final JdbcTemplate jdbcTemplate;

    public void dropTable() {
        String sql = "drop table if exists tasks";
        jdbcTemplate.execute(sql);
    }

    public void createTable() {
        String sql = "create table tasks ( " +
                "id bigserial primary key not null, " +
                "header varchar(100) not null, " +
                "description text default 'default description', " +
                "deadline timestamp without time zone not null, " +
                "user_email varchar not null references users (email), " +
                "state text default 'new' );";
        jdbcTemplate.update(sql);
    }

    public TaskDto addTask(TaskDto task) {
        long r = new Random().nextLong(5, 15);
        LocalDateTime ldt = LocalDateTime.now().plusDays(r);
        String sql = "insert into tasks(header, description, deadline) values(?, ?, ?)";
        jdbcTemplate.update(sql, task.getHeader(), task.getDescription(), Timestamp.valueOf(ldt));

        return TaskDto.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .deadline(ldt)
                .build();
    }

    public void addData(List<TaskDto> tasks) {
        long r = new Random().nextLong(10, 30);
        String sql = "insert into tasks(header, description, deadline, user_email) values(?, ?, ?, ?)";
        for (TaskDto t : tasks) {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, t.getHeader());
                ps.setString(2, t.getDescription());
                ps.setTimestamp(3, Timestamp.valueOf(t.getDeadline().plusDays(r)));
                ps.setString(4, t.getUserEmail());
                return ps;
            });
        }
    }

    public List<TaskDto> getTasksByEmail(String email) {
        String sql = "select * from tasks where user_email = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TaskDto.class), email);
    }

    public List<TaskDto> getAllTasks() {
        String sql = "select * from tasks";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TaskDto.class));
    }
}
