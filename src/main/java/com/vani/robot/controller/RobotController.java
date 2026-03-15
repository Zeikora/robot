package com.vani.robot.controller;

import com.vani.robot.model.request.RobotRequest;
import com.vani.robot.service.RobotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/robot")
public class RobotController {

    private final RobotService robotService;

    @PostMapping("/move")
    public ResponseEntity<String> move(@Valid
                                         @RequestBody RobotRequest robotRequest) {
        return ResponseEntity.ok(robotService.move(robotRequest));
    }
}
