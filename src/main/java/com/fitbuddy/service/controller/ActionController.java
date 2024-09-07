package com.fitbuddy.service.controller;

import com.fitbuddy.service.repository.dto.ActionDto;
import com.fitbuddy.service.repository.dto.AthleteDto;
import com.fitbuddy.service.repository.dto.request.ActionRequest;
import com.fitbuddy.service.service.ActionService;
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
    public ResponseEntity calendar(ActionRequest request) {
        return ResponseEntity.ok(service.calendar(request));
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity detail(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity.ok(service.detail(uuid));
    }

    @PostMapping(value = "/start")
    public ResponseEntity doAction(@RequestBody ActionDto actionDto) {
        return ResponseEntity.ok(service.doAction(actionDto));
    }

    @PatchMapping(value = "/done")
    public ResponseEntity doneAction(@RequestBody ActionDto actionDto) {
        return ResponseEntity.ok(service.doneAction(actionDto));
    }


    @DeleteMapping(value = "/cancel")
    public ResponseEntity cancelAction(@RequestBody ActionDto actionDto) {
        return ResponseEntity.ok(service.cancelAction(actionDto));
    }

    @GetMapping(value = "/histories")
    public ResponseEntity histories(ActionRequest request) {
        return ResponseEntity.ok(service.histories(request));
    }
}
