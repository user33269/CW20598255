package com.comp2042;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HighestScoreManager {
    private final Path filePath;
    private int highestScore;

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
    public void checkHighestScore(int currentScore){
        if(currentScore>highestScore){
            highestScore= currentScore;
            saveHighestScore();

        }
    }

    public int getHighestScore(){
        return highestScore;
    }

}
