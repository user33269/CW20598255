package com.comp2042;


import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


/**
 * This class manages loading, storing and updating the highest score for the game.
 * It reads and updates highest score into a text file created at startup.
 */
public class HighestScoreManager {
    private final Path filePath;
    private int highestScore;

    /**
     * Creates a new HighestScoreManager for the given filename.
     * It initialises the file path so highest score would be automatically loaded.
     * @param filename the name of file used for storing highest score.
     */
    public HighestScoreManager(String filename){
        this.filePath= Path.of(filename);
        loadHighestScore();
    }


    private void loadHighestScore(){
        try{
            if (!Files.exists(filePath)){
                highestScore=0;
                return;
            }
            try(Scanner scanner= new Scanner(filePath.toFile())){
                if(scanner.hasNextInt()){
                    highestScore=scanner.nextInt();
                } else {
                    highestScore=0;
                }}
        }catch (Exception e){
            e.printStackTrace();
            highestScore=0;
        }
    }

    private void saveHighestScore(){
        try{
            FileWriter writer= new FileWriter(filePath.toFile());
            writer.write(Integer.toString(highestScore));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Compares if player's current score is higher than the stored highest score.
     * If current score is higher than existing highest store, the highest score will be updated and saved into score file.
     * @param currentScore player's current score during game.
     */
    public void checkHighestScore(int currentScore){
        if(currentScore>highestScore){
            highestScore= currentScore;
            saveHighestScore();

        }
    }

    /**
     * Returns highest score stored.
     * @return highest score saved in score file.
     */
    public int getHighestScore(){
        return highestScore;
    }

}
