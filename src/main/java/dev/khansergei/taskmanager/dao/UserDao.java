package dev.khansergei.taskmanager.dao;

import dev.khansergei.taskmanager.dto.UserDto;
import dev.khansergei.taskmanager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final String temp = "select u.id, u.username, u.email, u.password ";

    public void dropTableUsers() {
        String sql = "drop table if exists users";
        jdbcTemplate.execute(sql);
    }

    public void dropTableAuth() {
        String sql = "drop table if exists authorities";
        jdbcTemplate.execute(sql);
    }

    public void createTableUsers() {
        String sql = "create table users ( " +
                "id bigserial primary key not null, " +
                "username varchar(50) not null, " +
                "email varchar(50) not null, " +
                "password varchar not null, " +
                "enabled boolean not null);";
        jdbcTemplate.update(sql);
    }

    public void createTableAuth() {
        String sql = "create table authorities ( " +
                "id bigserial primary key not null, " +
                "user_id integer not null references users(id), " +
                "authority text not null);";
        jdbcTemplate.update(sql);
    }

    private boolean ifExists(User user) {
        if (existsEmail(user.getEmail()))
            return true;
        return false;
    }

    public boolean existsEmail(String email) {
        String sql = "select count(id) from users " +
                "where email = ?";
        var result = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return result == 1;
    }

    private void createAuthority(String email) {
        var userId = getIdByEmail(email);
        String sql = "insert into authorities(user_id, authority) values(?, 'USER')";
        jdbcTemplate.update(sql, userId);
    }

    public Long getIdByEmail(String email) {
        String sql = "select id from users where email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, email);
    }

    public String addUser(User user) {
        if (!ifExists(user)) {
            String sql = "insert into users(username, email, password, enabled) values(?, ?, ?, true)";
            var sm = jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), encoder.encode(user.getPassword()));
            createAuthority(user.getUsername());
            return "Success";
        }
        return "Try again";
    }

    public void addData(List<UserDto> users) {
        String sql = "insert into users(username, email, password, enabled) values(?, ?, ?, ?)";
        String sqlAuth = "insert into authorities(user_id, authority) values(?, ?)";
        for (int i = 0; i < users.size(); i++) {
            int finalI = i;
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, users.get(finalI).getUsername());
                ps.setString(2, users.get(finalI).getEmail());
                ps.setString(3, encoder.encode(users.get(finalI).getPassword()));
                ps.setBoolean(4, users.get(finalI).isEnabled());
                return ps;
            });
            jdbcTemplate.update(sqlAuth, i + 1, "USER");
        }
    }
}
