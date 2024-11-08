package com.example.RPS_client.RPSGame;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RPSPlayer {
    private String name;
    private Move move;

    public enum Move {
        ROCK,
        PAPER,
        SCISSORS
    }
}
