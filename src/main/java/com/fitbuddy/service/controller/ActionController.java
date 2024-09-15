package com.fitbuddy.service.controller;

import com.fitbuddy.service.repository.dto.ActionDto;
import com.fitbuddy.service.repository.dto.request.ActionRequest;
import com.fitbuddy.service.service.ActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/action")
@RequiredArgsConstructor
@Slf4j
public class ActionController {
    private final ActionService service;

    @GetMapping(value = "/calendar")
    @Operation( summary ="캘린더" )
    public ResponseEntity calendar(ActionRequest request) {
        return ResponseEntity.ok(service.calendar(request));
    }

    @GetMapping(value = "/{uuid}")
    @Operation( summary ="상세" )
    public ResponseEntity detail(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity.ok(service.detail(uuid));
    }

    @PostMapping(value = "/start")
    @Operation( summary ="액션/운동 시작" )
    public ResponseEntity doAction(@RequestBody ActionDto actionDto) {
        return ResponseEntity.ok(service.doAction(actionDto));
    }

    @PatchMapping(value = "/done")
    @Operation( summary ="액션/운동 종료" )
    public ResponseEntity doneAction(@RequestBody ActionDto actionDto) {
        return ResponseEntity.ok(service.doneAction(actionDto));
    }


    @DeleteMapping(value = "/cancel")
    @Operation( summary ="액션/운동 취소" )
    public ResponseEntity cancelAction(@RequestBody ActionDto actionDto) {
        return ResponseEntity.ok(service.cancelAction(actionDto));
    }

    @GetMapping(value = "/histories")
    @Operation( summary ="액션 히스토리" )
    public ResponseEntity histories(ActionRequest request) {
        return ResponseEntity.ok(service.histories(request));
    }
}
