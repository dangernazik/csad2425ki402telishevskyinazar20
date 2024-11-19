package com.example.RPS_client.DTO;

import com.example.RPS_client.RPSGame.RPSPlayer;
/**
 * @record GameDTO
 * @brief Represents the data transfer object for a Rock-Paper-Scissors game.
 * @details Holds the game result and the moves made by both players.
 */
public record GameDTO(
        /**
         * @brief The result of the game.
         * @details Could represent outcomes such as "Player 1 Wins", "Player 2 Wins", or "Draw".
         */
        String gameResult,

        /**
         * @brief The move made by Player 1.
         * @details Should be one of the values defined in the `RPSPlayer.Move` enum.
         */
        RPSPlayer.Move player1Move,

        /**
         * @brief The move made by Player 2.
         * @details Should be one of the values defined in the `RPSPlayer.Move` enum.
         */
        RPSPlayer.Move player2Move
) {
}

