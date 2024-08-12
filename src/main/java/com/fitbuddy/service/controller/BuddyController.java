package com.fitbuddy.service.controller;


import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.service.BuddyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/buddy")
@RequiredArgsConstructor
public class BuddyController {
    private final BuddyService service;

    @GetMapping(value = "/{userUuid}")
    public ResponseEntity myBuddies(@PathVariable(name = "userUuid") String userUuid) {
        return ResponseEntity.ok(service.myBuddies(userUuid));
    }

    @PostMapping(value = "/make-friends")
    public ResponseEntity makeFriend(@RequestBody MyBuddyDto myBuddy) {
        return ResponseEntity.ok(service.makeFriend(myBuddy));
    }

    @PatchMapping(value = "/earn-exp")
    public ResponseEntity earnExp(@RequestBody MyBuddyDto myBuddy) {
        return ResponseEntity.ok(service.earnExp(myBuddy));
    }

    @PatchMapping(value = "/see-ya")
    public ResponseEntity changePrimaryBuddy( @RequestBody MyBuddyDto myBuddy) {
            return ResponseEntity.ok(service.changePrimaryBuddy(myBuddy));
    }

    @GetMapping(value = "/{userUuid}/dictionary")
    public ResponseEntity myDictionary(@PathVariable(name = "userUuid") String userUuid) {
        return ResponseEntity.ok(service.dictionary(userUuid));
    }
}
