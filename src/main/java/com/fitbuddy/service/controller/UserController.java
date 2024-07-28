package com.fitbuddy.service.controller;

import com.fitbuddy.service.etc.validations.User;
import com.fitbuddy.service.repository.dto.UserDto;
import com.fitbuddy.service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity signUp(HttpServletResponse response,
                                 @Valid @Validated(value = {User.SignUp.class}) @RequestBody UserDto user) {
        return ResponseEntity.ok(service.signUp(response, user));
    }

    @PatchMapping(value = "/sign/in")
    public ResponseEntity signIn(HttpServletResponse response,
                                 @Valid @Validated(value = {User.SignIn.class}) @RequestBody UserDto user) {
        return ResponseEntity.ok(service.signIn(response, user));
    }

    @PatchMapping(value = "/sign/out")
    public ResponseEntity signOut(HttpServletResponse response,
                                  @Valid @Validated(value = {User.SignOut.class}) @RequestBody UserDto user) {
        return ResponseEntity.ok(service.signOut(response, user));
    }

}
