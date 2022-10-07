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
import java.util.Optional;
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
                "state text not null default 'new');";
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

    public List<TaskDto> getTasks(String email) {
        String sql = "select id, header, state, deadline from tasks where user_email = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TaskDto.class), email);
    }

    public List<TaskDto> findTaskById(Long id, Authentication auth) {
        String sql = "select * from tasks where id = ? and user_email = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TaskDto.class), id, auth.getName());
    }

    public ResponseEntity<?> changeState(Long id, Authentication auth) {
        if (!isTaskExist(id))
            return ResponseEntity.badRequest().body("Such task not exist");
        if (!isTaskOwner(id, auth))
            return ResponseEntity.badRequest().body("You cannot change state if task isn't yours");
        var taskDto = findTaskById(id, auth).get(0);
        var tableState = taskDto.getState();
        var state = findStateByValue(tableState);

        if (!taskDto.getState().equalsIgnoreCase("complete")) {
            String sql = "update tasks set state = ? where id = ?";
            var updateState = state.nextState().getName();
            taskDto.setState(updateState);
            return ResponseEntity.ok(jdbcTemplate.update(sql, updateState, id));
        }
        return ResponseEntity.badRequest().body("Task already complete");
    }

    public boolean isTaskOwner(Long id, Authentication auth) {
        String query = "select count(id) from tasks where id = ? and user_email = ?;";
        var result = jdbcTemplate.queryForObject(query, Integer.class, id, auth.getName());
        return result == 1;
    }

    public State findStateByValue(String str) {
        if (str.equalsIgnoreCase("new")) return State.NEW;
        if (str.equalsIgnoreCase("active")) return State.ACTIVE;
        return State.COMPLETE;
    }

    public boolean isTaskExist(Long id) {
        String query = "select count(*) from tasks where id = ?";
        var count = jdbcTemplate.queryForObject(query, Integer.class, id);
        return count == 1;
    }
}
