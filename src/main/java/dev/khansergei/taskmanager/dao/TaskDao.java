package dev.khansergei.taskmanager.dao;

import dev.khansergei.taskmanager.dto.TaskDto;
import dev.khansergei.taskmanager.entity.State;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
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
                "user_email varchar references users (email), " +
                "state text default 'new' );";
        jdbcTemplate.update(sql);
    }

    public TaskDto addTask(TaskDto task) {
        String sql = "insert into tasks(header, description, deadline) values(?, ?, ?)";
        jdbcTemplate.update(sql, task.getHeader(), task.getDescription(), Timestamp.valueOf(task.getDeadline().atStartOfDay()));

        return TaskDto.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .deadline(task.getDeadline())
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
                ps.setTimestamp(3, Timestamp.valueOf(t.getDeadline().plusDays(r).atStartOfDay()));
                ps.setString(4, t.getUserEmail());
                return ps;
            });
        }
    }

    public List<TaskDto> getTasksByEmail(String email) {
        String sql = "select id, header, description, deadline from tasks where user_email = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TaskDto.class), email);
    }

    public TaskDto findTaskById(Long id, Authentication auth) {
        var email = auth.getName();
        String sql = "select id, header, description, deadline from tasks where id = ? and user_email = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TaskDto.class), id, email);
    }

    public ResponseEntity<?> changeState(Long id, String str, Authentication auth) {
        String sql = "update tasks set state = ? where id = ?";
        var taskDto = findTaskById(id, auth);

        if (taskDto.getState() != State.COMPLETE) {
            State state = State.valueOf(str.toUpperCase());
            taskDto.setState(state.nextState());
            return ResponseEntity.ok(jdbcTemplate.update(sql, str, id));
        }
        return ResponseEntity.badRequest().body("Task already complete");
    }
}
