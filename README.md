# Robot Application
A Spring Boot application that simulates a robot moving on a 6x6 table.

Quick Start with Docker
Prerequisites
Docker and Docker Compose installed

Running the Application
Build and run with Docker Compose:
docker compose up --build


The application will be available at http://localhost:8080

Testing the Robot
Send a POST request to /robot/move with commands:

curl -X POST http://localhost:8080/robot/move \
-H "Content-Type: application/json" \
-d '{
"commands": [
"PLACE 0,0,NORTH",
"MOVE",
"REPORT"
]
}'


Available Commands
PLACE X,Y,DIRECTION - Place robot on table (X,Y: 0-5, DIRECTION: NORTH/SOUTH/EAST/WEST)
MOVE - Move robot one unit forward
LEFT - Rotate robot 90° left
RIGHT - Rotate robot 90° right
REPORT - Output current position

Running without Docker
Prerequisites
Java 21
Gradle (or use included gradlew)

Build and Run
./gradlew bootRun


Build JAR
./gradlew bootJar
java -jar build/libs/robot-0.0.1-SNAPSHOT.jar


Running Tests
./gradlew test


Stopping the Application
docker compose down


Using Postman
Method: POST
URL: http://localhost:8080/robot/move
Headers:
Content-Type: application/json
Body (raw JSON):
{
"commands": [
"PLACE 0,0,NORTH",
"MOVE",
"REPORT"
]
}


Example Test Cases
Test Case 1: Basic Movement
{
"commands": ["PLACE 0,0,NORTH", "MOVE", "REPORT"]
}

Expected: Output: 0,1,NORTH

Test Case 2: Rotation
{
"commands": ["PLACE 1,2,EAST", "MOVE", "MOVE", "LEFT", "MOVE", "REPORT"]
}

Expected: Output: 3,3,NORTH

Test Case 3: Prevent Falling
{
"commands": ["PLACE 0,0,SOUTH", "MOVE", "REPORT"]
}

Expected: Output: 0,0,SOUTH (robot stays in place)