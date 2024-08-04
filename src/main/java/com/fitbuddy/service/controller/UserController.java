package com.fitbuddy.service.controller;

import com.fitbuddy.service.etc.validations.User;
import com.fitbuddy.service.repository.dto.UserDto;
import com.fitbuddy.service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping(value = "/verify/{phone}")
    public ResponseEntity verify(@PathVariable(required = true, name = "phone") String phone) {
        return ResponseEntity.ok(service.verify(phone));
    }

    @PostMapping(value = "/sign/up")
    public ResponseEntity signUp(HttpServletResponse response, @Validated(value = {User.SignUp.class}) @RequestBody UserDto user) {
        return ResponseEntity.ok(service.signUp(response, user));
    }

    @PatchMapping(value = "/sign/in")
    public ResponseEntity signIn(HttpServletResponse response, @Validated(value = {User.SignIn.class}) @RequestBody UserDto user) {
        return ResponseEntity.ok(service.signIn(response, user));
    }

    @PatchMapping(value = "/sign/out")
    public ResponseEntity signOut(HttpServletResponse response, @Validated(value = {User.SignOut.class}) @RequestBody UserDto user) {
        return ResponseEntity.ok(service.signOut(response, user));
    }

}
