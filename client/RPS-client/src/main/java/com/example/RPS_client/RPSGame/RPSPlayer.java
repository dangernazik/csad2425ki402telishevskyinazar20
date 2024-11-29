package com.example.RPS_client.RPSGame;

import lombok.Getter;
import lombok.Setter;

/**
 * @brief Represents a player in the Rock, Paper, Scissors game.
 * @details This class holds the player's name and the move they choose during the game.
 *          The player can choose between the moves defined in the `Move` enum.
 */
@Getter
@Setter
public class RPSPlayer {
    private String name;
    private Move move;

    /**
     * @brief Enum representing the possible moves in the Rock, Paper, Scissors game.
     * @details The available moves are:
     *          - ROCK
     *          - PAPER
     *          - SCISSORS
     */
    public enum Move {
        ROCK,
        PAPER,
        SCISSORS
    }
}
