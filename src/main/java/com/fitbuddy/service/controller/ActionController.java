package com.fitbuddy.service.controller;

import com.fitbuddy.service.service.ActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/fitness")
@RequiredArgsConstructor
@Slf4j
public class ActionController {
    private final ActionService service;


}
