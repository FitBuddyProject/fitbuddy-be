package com.fitbuddy.service.controller;

import com.fitbuddy.service.repository.dto.UserDto;
import com.fitbuddy.service.repository.entity.User;
import com.fitbuddy.service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping(value = "/verify/{phone:010\\d{7,8}}")
    public ResponseEntity verify(@PathVariable String phone) {
        return ResponseEntity.ok(service.verify(phone));
    }

    @PostMapping(value = "/sign/up")
    public ResponseEntity signUp(HttpServletResponse response,  @RequestBody UserDto user) {
        return ResponseEntity.ok(service.signUp(response, user));
    }

    @PatchMapping(value = "/sign/in")
    public ResponseEntity signIn(@RequestBody User user) {
        return ResponseEntity.ok(service.signIn(user));
    }
    @PatchMapping(value = "/sign/out")
    public ResponseEntity signOut(@RequestBody User user) {
        return ResponseEntity.ok(service.signOut(user));
    }

}
