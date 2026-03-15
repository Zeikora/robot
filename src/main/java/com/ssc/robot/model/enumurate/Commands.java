package com.ssc.robot.model.enumurate;

import lombok.Getter;

@Getter
public enum Commands {
    MOVE("MOVE"), LEFT("LEFT"), RIGHT("RIGHT"), REPORT("REPORT");

    private final String value;

    Commands(String value) {
        this.value = value;
    }

}
