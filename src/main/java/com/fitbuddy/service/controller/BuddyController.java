package com.fitbuddy.service.controller;


import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.service.BuddyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.fitbuddy.service.etc.validations.MyBuddy.*;
@RestController
@RequestMapping(value = "/v1/buddy")
@RequiredArgsConstructor
public class BuddyController {
    private final BuddyService service;

    @GetMapping(value = "/{userUuid}")
    @Operation( summary ="내 버디 리스트" )
    public ResponseEntity myBuddies(@PathVariable(name = "userUuid") String userUuid) {
        return ResponseEntity.ok(service.myBuddies(userUuid));
    }


    @PostMapping(value = "/make-friends")
    @Operation( summary ="버디 추가" )
    public ResponseEntity makeFriend( @Validated(value = MakeFriends.class) @RequestBody MyBuddyDto myBuddy) {
        return ResponseEntity.ok(service.makeFriend(myBuddy));
    }

    @PatchMapping(value = "/earn-exp")
    @Operation( summary ="경험치 조정" )
    public ResponseEntity earnExp( @Validated(value = EarnExp.class) @RequestBody MyBuddyDto myBuddy) {
        return ResponseEntity.ok(service.earnExp(myBuddy));
    }

    @PatchMapping(value = "/see-ya")
    @Operation( summary ="메인 버디 변경" )
    public ResponseEntity changePrimaryBuddy( @Validated(value = ChangePrimary.class) @RequestBody MyBuddyDto myBuddy) {
            return ResponseEntity.ok(service.changePrimaryBuddy(myBuddy));
    }

    @GetMapping(value = "/{userUuid}/dictionary")
    @Operation( summary ="버디 도감" )
    public ResponseEntity myDictionary(@PathVariable(name = "userUuid") String userUuid) {
        return ResponseEntity.ok(service.dictionary(userUuid));
    }
}
