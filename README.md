# COMP 2042 Tetris Game
## Github 
https://github.com/user33269/CW20598255.git
## How to compile 
1. Ensure that Java JDK11 or above and Maven 3.6+ have been installed. 
2. Ensure that JavaFX Maven plugin configured in 'pom.xml'
3. Compile project by running: mvn clean compile 
4. Through JavaFX Maven plugin, launch application by using: 
   mvn javafx: run  

## Implemented and working properly 
1. Hold Brick feature: player able to hold current brick.
2. Next Brick feature: player able to view upcoming brick shape in next brick pane.
3. New Game mode: Added new Time-Blitz mode where there is a 120s countdown timer.
4. Highest Score scoreboard: Scoreboard that store highest score scored. 
5. QuickDrop feature: Player able to move brick downwards instantly to lowest position.
6. Restart, pause, resume and exit buttons. 
7. Ghost brick feature: players able to see where bricks would fall onto . 

## Implemented but not working properly 
None 

## Features not implemented 
1. Floor kick: Players able to rotate a piece when it is at the last row. 
2. Longer next brick queue: Allow players to view next four upcoming bricks instead of one. 

## New Java classes 
1. TimeManager: to manage timer operations that is used in Tetris TimeBlitz mode. 
2. ScoreManager: to store and update highest score scored throughout different game modes. 
3. HomeController: manages interactions and UI elements of homescreen for Tetris game. 
4. HowToPlayController: manages interactions and UI elements of HowToPlay pop up window at game homescreen. 
5. SceneLoader: handle switching of scene and FXML in the application. 
6. GameControllerTimeBlitz and GuiControllerTimeBlitz: handles logic and presentation for TimeBlitz game mode. 
7. QuickDropData: stores relevant data for QuickDrop event. 

## Modified Java Classes 
1. SimpleGameBoard:
- Implemented holdbrick and quickdrop methods to support new features added. 
- Refactored by extracting repeated code blocks into separate helper methods to improve readability and reduce duplication.  
2. GameControllers:
- Implemented QuickDrop making gameplay faster and more dynamic.
3. GuiControllers: 
- Implemented display of score, next brick and held brick. This increases player engagement by showing progress.
- Display ghost brick position to improve players game accuracy. 
## Unexpected Problems 
1. Pane Layout Issues on Stage Resize: 
While I resize my the application window, I faced an issue where my GameBoard, Ghostbrick, GamePanel panes shifted out of alignment, causing the game layout to display incorrectly. 
I resolved this issue by carefully adjusting the pane positions and layout settings. 