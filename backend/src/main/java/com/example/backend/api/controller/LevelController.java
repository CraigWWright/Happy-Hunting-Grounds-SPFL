package com.example.backend.api.controller;

import com.example.backend.api.model.Level;
import com.example.backend.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LevelController {

    private LevelService levelService;
    @Autowired
    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }
    @GetMapping("/level")
    public Level getLevel(@RequestParam Integer userSelectedLevel) {
        return levelService.getLevel(userSelectedLevel);
    }
}
