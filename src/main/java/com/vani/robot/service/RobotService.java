package com.vani.robot.service;

import com.vani.robot.model.dto.RobotPosition;
import com.vani.robot.model.request.RobotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotService {

    private static final Pattern PLACE_PATTERN = Pattern.compile("^PLACE\\s+([0-5]),([0-5]),(NORTH|EAST|SOUTH|WEST)$");

    private static final String MOVE = "MOVE";
    private static final String LEFT = "LEFT";
    private static final String RIGHT = "RIGHT";
    private static final String REPORT = "REPORT";

    private static final String NORTH = "NORTH";
    private static final String EAST = "EAST";
    private static final String SOUTH = "SOUTH";
    private static final String WEST = "WEST";

    public String move(RobotRequest robotRequest) {
        if (robotRequest == null || robotRequest.getCommands() == null || robotRequest.getCommands().isEmpty()) {
            return "No commands received.";
        }

        RobotPosition position = null;
        StringBuilder output = new StringBuilder();

        for (String rawCommand : robotRequest.getCommands()) {
            if (isValidCommand(rawCommand)) {
                String command = rawCommand.trim();
                position = processCommand(command, position, output);
            }
        }

        return buildFinalOutput(output);
    }

    private boolean isValidCommand(String command) {
        return command != null && !command.isBlank();
    }

    private RobotPosition processCommand(String command, RobotPosition position, StringBuilder output) {
        Matcher matcher = PLACE_PATTERN.matcher(command);
        if (matcher.matches()) {
            return handlePlaceCommand(matcher, command, output);
        }

        if (position != null) {
            handleMovementCommand(command, position, output);
        }

        return position;
    }

    private RobotPosition handlePlaceCommand(Matcher matcher, String command, StringBuilder output) {
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        String facing = matcher.group(3);

        output.append(command).append("\n");

        return new RobotPosition()
                .setPosX(x)
                .setPosY(y)
                .setDirection(facing);
    }

    private void handleMovementCommand(String command, RobotPosition position, StringBuilder output) {
        if (MOVE.equals(command)) {
            output.append(MOVE)
                    .append("\n");
            moveForward(position);
        } else if (LEFT.equals(command)) {
            output.append(LEFT)
                    .append("\n");
            turnLeft(position);
        }
        else if (RIGHT.equals(command)) {
            output.append(RIGHT)
                    .append("\n");
            turnRight(position);
        } else if (REPORT.equals(command)) {
            output.append(REPORT)
                    .append("\n");
            appendReportOutput(position, output);
        }
        // Invalid commands are silently ignored
    }

    private void appendReportOutput(RobotPosition position, StringBuilder output) {
        output.append("Output: ")
                .append(position.getPosX()).append(",")
                .append(position.getPosY()).append(",")
                .append(position.getDirection())
                .append("\n");
    }

    private String buildFinalOutput(StringBuilder output) {
        log.debug("buildFinalOutput called with output: '{}'", output);
        log.debug("output.isEmpty(): {}", output.isEmpty());

        if (output.isEmpty()) {
            log.debug("Returning: Robot is not on the table.");
            return "Robot is not on the table.";
        }

        String result = output.toString().trim();
        log.debug("Returning result: '{}'", result);
        return result;
    }

    private void moveForward(RobotPosition position) {
        int nextX = position.getPosX();
        int nextY = position.getPosY();

        String direction = position.getDirection();
        switch (direction) {
            case NORTH -> nextY++;
            case SOUTH -> nextY--;
            case EAST -> nextX++;
            case WEST -> nextX--;
            case null, default -> {
                return;
            }
        }

        // ignore move that would make robot fall
        if (isValidPosition(nextX, nextY)) {
            position.setPosX(nextX).setPosY(nextY);
        }
    }

    private void turnLeft(RobotPosition position) {
        String direction = position.getDirection();
        if (NORTH.equals(direction)) {
            position.setDirection(WEST);
        } else if (WEST.equals(direction)) {
            position.setDirection(SOUTH);
        } else if (SOUTH.equals(direction)) {
            position.setDirection(EAST);
        } else if (EAST.equals(direction)) {
            position.setDirection(NORTH);
        }
    }

    private void turnRight(RobotPosition position) {
        String direction = position.getDirection();
        if (NORTH.equals(direction)) {
            position.setDirection(EAST);
        } else if (EAST.equals(direction)) {
            position.setDirection(SOUTH);
        } else if (SOUTH.equals(direction)) {
            position.setDirection(WEST);
        } else if (WEST.equals(direction)) {
            position.setDirection(NORTH);
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x <= 5 && y >= 0 && y <= 5;
    }
}
