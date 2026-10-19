package com.example.backend.api.controller;

import com.example.backend.api.model.GuessResult;
import com.example.backend.service.GuessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class GuessController {

    private GuessService guessService;

    @Autowired
    public GuessController(GuessService guessService) {this.guessService = guessService;}

    @GetMapping("/guess")
    public GuessResult getGuess(@RequestParam String team,@RequestParam String ground,@RequestParam String season,@RequestParam String playerName,@RequestParam int level) {
        return guessService.getGuessResult(team, ground, season, playerName, level);
    }

    @GetMapping("/players")
    public ArrayList<String> getPlayer(@RequestParam String player) {
        return guessService.getPlayerGuess(player);
    }
}
