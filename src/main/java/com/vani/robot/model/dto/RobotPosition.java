package com.vani.robot.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RobotPosition {
    private int posX = 0;
    private int posY = 0;
    private String direction;
}
