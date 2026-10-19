package com.example.backend.service;

import com.example.backend.api.model.GuessResult;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class GuessService {


    public GuessResult getGuessResult(String team, String ground, String season, String playerName, int level) {
        String sql = """
                SELECT
                    m.date,
                    m.home_team,
                    m.away_team,
                    m.home_score || ' - ' || m.away_score          AS final_score,
                    m.ground,
                    m.season,
                    -- the specific player's goals in this match
                    GROUP_CONCAT(
                        CASE WHEN g.scorer LIKE ? AND g.team = ?
                             THEN g.minute || '''' ||
                                  CASE WHEN g.own_goal = 1 THEN ' (OG)'
                                       WHEN g.penalty = 1  THEN ' (P)'
                                       ELSE '' END
                        END, ', '
                    )                                               AS player_goals_at,
                    -- home team scorers only
                    GROUP_CONCAT(
                        CASE WHEN all_g.team = m.home_team
                             THEN all_g.scorer || ' ' || all_g.minute || '''' ||
                                  CASE WHEN all_g.own_goal = 1 THEN ' (OG)'
                                       WHEN all_g.penalty = 1  THEN ' (P)'
                                       ELSE '' END
                        END, ' | '
                    )                                               AS home_scorers,
                    -- away team scorers only
                    GROUP_CONCAT(
                        CASE WHEN all_g.team = m.away_team
                             THEN all_g.scorer || ' ' || all_g.minute || '''' ||
                                  CASE WHEN all_g.own_goal = 1 THEN ' (OG)'
                                       WHEN all_g.penalty = 1  THEN ' (P)'
                                       ELSE '' END
                        END, ' | '
                    )                                               AS away_scorers
                FROM goals g
                JOIN matches m   ON g.match_id = m.match_id
                JOIN goals all_g ON all_g.match_id = m.match_id
                WHERE g.scorer LIKE ?
                  AND g.team   = ?
                  AND m.season = ?
                  AND m.ground LIKE ?
                GROUP BY m.match_id
                ORDER BY m.date;
                                
                """;

        GuessResult guessResult = null;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spfl.sqlite");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + playerName + "%");   // CASE WHEN scorer
            stmt.setString(2, team);            // CASE WHEN team
            stmt.setString(3, "%" + playerName + "%");   // WHERE scorer
            stmt.setString(4, team);            // WHERE team
            stmt.setString(5, season);          // WHERE season
            stmt.setString(6, "%" + ground + "%"); // WHERE ground

            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                guessResult = new GuessResult(true, level, rs.getString("date"), rs.getString("home_team"), rs.getString("away_team"), rs.getString("final_score"), rs.getString("player_goals_at"), rs.getString("home_scorers"), rs.getString("away_scorers"));
            } else {
                guessResult = new GuessResult(false, 0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guessResult;
    }


    public ArrayList<String> getPlayerGuess(String player) {
        String sql = """
                SELECT full_name FROM player_names
                WHERE full_name LIKE ?;
                """;

        ArrayList<String> players = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spfl.sqlite");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                 stmt.setString(1, "%" + player + "%");

                 ResultSet rs = stmt.executeQuery();

                 while (rs.next()) {
                     players.add(rs.getString("full_name"));
                 }
        } catch (SQLException e) {
                 e.printStackTrace();
        }
        return players;
    }
}
