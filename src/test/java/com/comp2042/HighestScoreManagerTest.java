package com.comp2042;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HighestScoreManagerTest {
    private static Path tempFile;
    private HighestScoreManager highestscoremanager;

    @BeforeAll
    static void setupALL() throws IOException {
        tempFile = Files.createTempFile("highestscore", ".txt");
    }

    @AfterAll
    static void cleanup()throws IOException{
        Files.deleteIfExists(tempFile);
    }

    @BeforeEach
    void setup(){
        highestscoremanager= new HighestScoreManager(tempFile.toString());
    }

    @Test
    void testCheckHighestScore(){
        highestscoremanager.checkHighestScore(1500);
        highestscoremanager.checkHighestScore(1200);
        assertEquals(1500,highestscoremanager.getHighestScore());
    }

    @Test
    void testHighestScoreSaved(){
        highestscoremanager.checkHighestScore(2000);

        HighestScoreManager newManager= new HighestScoreManager(tempFile.toString());
        assertEquals(2000, newManager.getHighestScore());
    }
}