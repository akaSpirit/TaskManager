package dev.khansergei.taskmanager.dao;

import dev.khansergei.taskmanager.dto.UserDto;
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
                "id bigserial not null, " +
                "username varchar not null, " +
                "email varchar not null, " +
                "password varchar not null, " +
                "enabled boolean not null, " +
                "primary key (email) );";
        jdbcTemplate.update(sql);
    }

    public void createTableAuth() {
        String sql = "create table authorities ( " +
                "id bigserial primary key not null, " +
                "username varchar not null references users(email), " +
                "authority text not null );";
        jdbcTemplate.update(sql);
    }

    private boolean ifExists(UserDto user) {
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
//        var userId = getIdByEmail(email);
        String sql = "insert into authorities(username, authority) values(?, 'USER')";
        jdbcTemplate.update(sql, email);
    }

    public Long getIdByEmail(String email) {
        String sql = "select id from users where email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, email);
    }

    public String addUser(UserDto user) {
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
        String sqlAuth = "insert into authorities(username, authority) values(?, ?)";
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
            jdbcTemplate.update(sqlAuth, users.get(i).getEmail(), "USER");
        }
    }
}
