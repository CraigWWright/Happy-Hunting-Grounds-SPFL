package com.example.backend.service;

import com.example.backend.api.model.Level;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class LevelService {

    private List<Level> levelList;

    public LevelService() {
        levelList = new ArrayList<>();

        levelList = loadLevels("ground_levels.csv");
    }

    public Level getLevel(Integer userSelectedLevel) {
        List<Level> availableLevels = new ArrayList<>();

        for (Level level : levelList) {
            if (level.getLevel() == userSelectedLevel) {
                availableLevels.add(level);
            }
        }
        int random = new Random().nextInt(availableLevels.size());
        return availableLevels.get(random);
    }

    private List<Level> loadLevels(String filePath) {
        List<Level> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String team = parts[0].trim();
                String ground = parts[1].trim();
                int goals = Integer.parseInt(parts[2].trim());
                int level = Integer.parseInt(parts[3].trim());
                list.add(new Level(team, ground, goals, level));
            }

        } catch (IOException e) {
            System.err.println("Count not read file: " + e.getMessage());
        }

        return list;
    }
}
