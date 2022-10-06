package dev.khansergei.taskmanager.service;

import dev.khansergei.taskmanager.dao.UserDao;
import dev.khansergei.taskmanager.dto.UserDto;
import dev.khansergei.taskmanager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    public String addUser(UserDto userDto) {
        if (userDto.getUsername() == null)
            return "Username cannot be null";
        if (userDto.getEmail() == null)
            return "Email cannot be null";
        if (userDto.getPassword() == null)
            return "Password cannot be null";

        return userDao.addUser(
                UserDto.builder()
                        .username(userDto.getUsername())
                        .email(userDto.getEmail())
                        .password(userDto.getPassword())
                        .build());
    }
}
