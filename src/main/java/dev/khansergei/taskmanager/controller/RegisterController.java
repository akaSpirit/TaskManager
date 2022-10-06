package dev.khansergei.taskmanager.controller;

import dev.khansergei.taskmanager.dto.UserDto;
import dev.khansergei.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;


    /*
        Example register json
    {
        "id": 0,
            "username": "new",
            "email": "new",
            "password": "new",
            "enabled": true
    }
          */
    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.OK);
    }
}
