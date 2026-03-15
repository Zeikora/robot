package com.vani.robot.model.request;

import lombok.Data;

import java.util.List;

@Data
public class RobotRequest {
    private List<String> commands;
}
