package com.ssc.robot.service;

import com.ssc.robot.model.dto.RobotPosition;
import com.ssc.robot.model.request.RobotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotService {

    private static final Pattern PLACE_PATTERN =
            Pattern.compile("^PLACE\\s+([0-5]),([0-5]),(NORTH|EAST|SOUTH|WEST)$");

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

        List<String> commands = robotRequest.getCommands();

        for (String rawCommand : commands) {
            if (rawCommand == null || rawCommand.isBlank()) {
                continue;
            }

            String command = rawCommand.trim();

            Matcher matcher = PLACE_PATTERN.matcher(command);
            if (matcher.matches()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                String facing = matcher.group(3);

                position = new RobotPosition()
                        .setPosX(x)
                        .setPosY(y)
                        .setDirection(facing);

                output.append(command).append("\n");
                continue;
            }

            // ignore all commands before first valid PLACE
            if (position == null) {
                continue;
            }

            switch (command) {
                case MOVE:
                    output.append(MOVE).append("\n");
                    moveForward(position);
                    break;

                case LEFT:
                    output.append(LEFT).append("\n");
                    turnLeft(position);
                    break;

                case RIGHT:
                    output.append(RIGHT).append("\n");
                    turnRight(position);
                    break;

                case REPORT:
                    output.append(REPORT).append("\n");
                    output.append("Output: ")
                            .append(position.getPosX()).append(",")
                            .append(position.getPosY()).append(",")
                            .append(position.getDirection())
                            .append("\n");
                    break;

                default:
                    // ignore invalid command
                    break;
            }
        }

        if (output.isEmpty()) {
            return "Robot is not on the table.";
        }

        return output.toString().trim();
    }

    private void moveForward(RobotPosition position) {
        int nextX = position.getPosX();
        int nextY = position.getPosY();

        switch (position.getDirection()) {
            case NORTH:
                nextY++;
                break;
            case SOUTH:
                nextY--;
                break;
            case EAST:
                nextX++;
                break;
            case WEST:
                nextX--;
                break;
            default:
                return;
        }

        // ignore move that would make robot fall
        if (isValidPosition(nextX, nextY)) {
            position.setPosX(nextX).setPosY(nextY);
        }
    }

    private void turnLeft(RobotPosition position) {
        switch (position.getDirection()) {
            case NORTH:
                position.setDirection(WEST);
                break;
            case WEST:
                position.setDirection(SOUTH);
                break;
            case SOUTH:
                position.setDirection(EAST);
                break;
            case EAST:
                position.setDirection(NORTH);
                break;
            default:
                break;
        }
    }

    private void turnRight(RobotPosition position) {
        switch (position.getDirection()) {
            case NORTH:
                position.setDirection(EAST);
                break;
            case EAST:
                position.setDirection(SOUTH);
                break;
            case SOUTH:
                position.setDirection(WEST);
                break;
            case WEST:
                position.setDirection(NORTH);
                break;
            default:
                break;
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x <= 5 && y >= 0 && y <= 5;
    }
}
