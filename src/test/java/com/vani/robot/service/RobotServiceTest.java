package com.vani.robot.service;

import com.vani.robot.model.request.RobotRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RobotService Tests")
@ExtendWith(SpringExtension.class)
class RobotServiceTest {

    private RobotService robotService;

    @BeforeEach
    void setUp() {
        robotService = new RobotService();
    }

    @Test
    @DisplayName("Should return error message when request is null")
    void testNullRequest() {
        String result = robotService.move(null);
        assertEquals("No commands received.", result);
    }

    @Test
    @DisplayName("Should return error message when commands list is null")
    void testNullCommandsList() {
        RobotRequest request = new RobotRequest();
        request.setCommands(null);

        String result = robotService.move(request);
        assertEquals("No commands received.", result);
    }

    @Test
    @DisplayName("Should return error message when commands list is empty")
    void testEmptyCommandsList() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Collections.emptyList());

        String result = robotService.move(request);
        assertEquals("No commands received.", result);
    }

    @Test
    @DisplayName("Should place robot and report position")
    void testPlaceAndReport() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTH", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("PLACE 0,0,NORTH"));
        assertTrue(result.contains("REPORT"));
        assertTrue(result.contains("Output: 0,0,NORTH"));
    }

    @Test
    @DisplayName("Should move robot north")
    void testMoveNorth() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTH", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,1,NORTH"));
    }

    @Test
    @DisplayName("Should move robot east")
    void testMoveEast() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,EAST", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 1,0,EAST"));
    }

    @Test
    @DisplayName("Should move robot south")
    void testMoveSouth() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 1,1,SOUTH", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 1,0,SOUTH"));
    }

    @Test
    @DisplayName("Should move robot west")
    void testMoveWest() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 1,1,WEST", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,1,WEST"));
    }

    @Test
    @DisplayName("Should turn left from north to west")
    void testTurnLeftFromNorth() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTH", "LEFT", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,0,WEST"));
    }

    @Test
    @DisplayName("Should turn right from north to east")
    void testTurnRightFromNorth() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTH", "RIGHT", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,0,EAST"));
    }

    @Test
    @DisplayName("Should complete full rotation left")
    void testFullRotationLeft() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList(
                "PLACE 0,0,NORTH",
                "LEFT", "LEFT", "LEFT", "LEFT",
                "REPORT"
        ));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,0,NORTH"));
    }

    @Test
    @DisplayName("Should complete full rotation right")
    void testFullRotationRight() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList(
                "PLACE 0,0,NORTH",
                "RIGHT", "RIGHT", "RIGHT", "RIGHT",
                "REPORT"
        ));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,0,NORTH"));
    }

    @Test
    @DisplayName("Should prevent robot from falling off north edge")
    void testPreventFallingNorth() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,5,NORTH", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,5,NORTH"));
    }

    @Test
    @DisplayName("Should prevent robot from falling off south edge")
    void testPreventFallingSouth() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,SOUTH", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,0,SOUTH"));
    }

    @Test
    @DisplayName("Should prevent robot from falling off east edge")
    void testPreventFallingEast() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 5,0,EAST", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 5,0,EAST"));
    }

    @Test
    @DisplayName("Should prevent robot from falling off west edge")
    void testPreventFallingWest() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,WEST", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,0,WEST"));
    }

    @Test
    @DisplayName("Should ignore commands before first PLACE")
    void testIgnoreCommandsBeforePlace() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("MOVE", "LEFT", "RIGHT", "REPORT", "PLACE 1,2,EAST", "REPORT"));

        String result = robotService.move(request);
        assertFalse(result.contains("Robot is not on the table"));
        assertTrue(result.contains("Output: 1,2,EAST"));
    }

    @Test
    @DisplayName("Should handle multiple PLACE commands")
    void testMultiplePlaceCommands() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList(
                "PLACE 0,0,NORTH",
                "MOVE",
                "PLACE 3,3,SOUTH",
                "REPORT"
        ));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 3,3,SOUTH"));
    }

    @Test
    @DisplayName("Should handle complex movement sequence")
    void testComplexMovementSequence() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList(
                "PLACE 1,2,EAST",
                "MOVE",
                "MOVE",
                "LEFT",
                "MOVE",
                "REPORT"
        ));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 3,3,NORTH"));
    }

    @Test
    @DisplayName("Should ignore invalid commands")
    void testIgnoreInvalidCommands() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList(
                "PLACE 0,0,NORTH",
                "INVALID",
                "JUMP",
                "MOVE",
                "REPORT"
        ));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,1,NORTH"));
    }

    @Test
    @DisplayName("Should ignore null commands in list")
    void testIgnoreNullCommandsInList() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTH", null, "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,1,NORTH"));
    }

    @Test
    @DisplayName("Should ignore blank commands in list")
    void testIgnoreBlankCommandsInList() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTH", "  ", "", "MOVE", "REPORT"));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 0,1,NORTH"));
    }

    @Test
    @DisplayName("Should handle PLACE with invalid coordinates")
    void testPlaceWithInvalidCoordinates() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 6,6,NORTH", "REPORT"));

        String result = robotService.move(request);
        assertEquals("Robot is not on the table.", result);
    }

    @Test
    @DisplayName("Should handle PLACE with invalid direction")
    void testPlaceWithInvalidDirection() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTHEAST", "REPORT"));

        String result = robotService.move(request);
        assertEquals("Robot is not on the table.", result);
    }

    @Test
    @DisplayName("Should handle square movement pattern")
    void testSquareMovementPattern() {
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList(
                "PLACE 1,1,NORTH",
                "MOVE",
                "RIGHT",
                "MOVE",
                "RIGHT",
                "MOVE",
                "RIGHT",
                "MOVE",
                "REPORT"
        ));

        String result = robotService.move(request);
        assertTrue(result.contains("Output: 1,1,WEST"));
    }

    @Test
    @DisplayName("Should handle all corners of the table")
    void testAllCorners() {
        RobotRequest request1 = new RobotRequest();
        request1.setCommands(Arrays.asList("PLACE 0,0,NORTH", "REPORT"));
        assertTrue(robotService.move(request1).contains("Output: 0,0,NORTH"));

        RobotRequest request2 = new RobotRequest();
        request2.setCommands(Arrays.asList("PLACE 5,0,NORTH", "REPORT"));
        assertTrue(robotService.move(request2).contains("Output: 5,0,NORTH"));

        RobotRequest request3 = new RobotRequest();
        request3.setCommands(Arrays.asList("PLACE 0,5,NORTH", "REPORT"));
        assertTrue(robotService.move(request3).contains("Output: 0,5,NORTH"));

        RobotRequest request4 = new RobotRequest();
        request4.setCommands(Arrays.asList("PLACE 5,5,NORTH", "REPORT"));
        assertTrue(robotService.move(request4).contains("Output: 5,5,NORTH"));
    }

    @Test
    @DisplayName("Debug test - test buildFinalOutput behavior")
    void testDebugBuildFinalOutput() {
        // Test with valid commands
        RobotRequest request = new RobotRequest();
        request.setCommands(Arrays.asList("PLACE 0,0,NORTH", "MOVE", "REPORT"));

        String result = robotService.move(request);

        // Set breakpoint on next line to inspect result
        System.out.println("Result: " + result);
        assertTrue(result.contains("Output: 0,1,NORTH"));
    }
}
