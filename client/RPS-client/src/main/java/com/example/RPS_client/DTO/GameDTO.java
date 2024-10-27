package com.example.RPS_client.DTO;

import com.example.RPS_client.RPSGame.RPSPlayer;

public record GameDTO(
        String gameResult,
        RPSPlayer.Move player1Move,
        RPSPlayer.Move player2Move
) {
}
