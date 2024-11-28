package com.example.RPS_client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.RPS_client.RPSGame.RPSPlayer;
import com.example.RPS_client.DTO.GameDTO;
import com.example.RPS_client.controller.GameController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

@DisabledIfSystemProperty(named = "ci.environment", matches = "true")
public class ControllerTest {
    private GameController gameController;

    private static final int ARDUINO_PORT_INDEX = 0;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        gameController = new GameController(ARDUINO_PORT_INDEX);
        Thread.sleep(2000);
    }

    @AfterEach
    void tearDown() {
        gameController.close();
    }

    @Order(1)
    @Test
    public void testSendModeAndMoves_MAN_VS_MAN() throws Exception {
        String mode = "MAN_VS_MAN";
        RPSPlayer.Move player1Move = RPSPlayer.Move.ROCK;
        RPSPlayer.Move player2Move = RPSPlayer.Move.SCISSORS;

        gameController.sendModeAndMoves(mode, player1Move, player2Move);
        GameDTO response = gameController.receiveResult();

        assertNotNull(response, "Response should not be null");
        assertEquals("Player 1", response.gameResult(), "Expected Player 1 to win with ROCK vs SCISSORS");
        assertEquals(RPSPlayer.Move.ROCK, response.player1Move(), "Player 1's move should be ROCK");
        assertEquals(RPSPlayer.Move.SCISSORS, response.player2Move(), "Player 2's move should be SCISSORS");
    }

    @Order(2)
    @Test
    public void testSendModeAndMoves_DRAW() throws Exception {
        String mode = "MAN_VS_MAN";
        RPSPlayer.Move player1Move = RPSPlayer.Move.PAPER;
        RPSPlayer.Move player2Move = RPSPlayer.Move.PAPER;

        gameController.sendModeAndMoves(mode, player1Move, player2Move);

        GameDTO response = gameController.receiveResult();

        assertNotNull(response, "Response should not be null");
        assertEquals("DRAW", response.gameResult(), "Expected a draw when both moves are PAPER");
        assertEquals(RPSPlayer.Move.PAPER, response.player1Move(), "Player 1's move should be PAPER");
        assertEquals(RPSPlayer.Move.PAPER, response.player2Move(), "Player 2's move should be PAPER");
    }
}
