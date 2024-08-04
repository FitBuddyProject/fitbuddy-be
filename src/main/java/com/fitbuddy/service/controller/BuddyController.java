package com.fitbuddy.service.controller;


import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.repository.entity.MyBuddy;
import com.fitbuddy.service.service.BuddyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/buddy")
@RequiredArgsConstructor
public class BuddyController {
    private final BuddyService service;

    @GetMapping(value = "/{uuid}")
    public ResponseEntity myBuddies(@PathVariable String uuid) {
        return ResponseEntity.ok(service.myBuddies(uuid));
    }

    @PostMapping(value = "/make-friends")
    public ResponseEntity makeFriend(@RequestBody MyBuddyDto myBuddy) {
        return ResponseEntity.ok(service.makeFriend(myBuddy));
    }

    @PatchMapping(value = "/see-ya")
    public ResponseEntity changePrimaryBuddy( @RequestBody MyBuddyDto myBuddy) {
            return ResponseEntity.ok(service.changePrimaryBuddy(myBuddy));
    }
}
