package com.example.RPS_client.controller;

import com.example.RPS_client.DTO.GameDTO;
import com.example.RPS_client.RPSGame.RPSPlayer;
import com.example.RPS_client.communication.Communication;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

/**
 * @file GameController.java
 * @brief Handles communication and game logic for the Rock-Paper-Scissors (RPS) game.
 * @details Manages sending game modes and moves to the server and receiving results.
 */
public class GameController {

    /**
     * @brief The communication object used for serial communication.
     */
    private final Communication communication;

    /**
     * @brief Constructor to initialize the game controller with a specific serial port.
     * @param portNumber The index of the serial port to use for communication.
     * @details Creates a Communication instance using the specified port.
     */
    public GameController(int portNumber) {
        communication = new Communication(SerialPort.getCommPorts()[portNumber]);
    }

    /**
     * @brief Sends the game mode and moves to the server.
     * @param mode The game mode as a string.
     * @param move1 The first player's move.
     * @param move2 The second player's move.
     * @throws IOException If an error occurs during message transmission.
     * @details Formats the message as "mode,move1,move2" and sends it via the communication object.
     */
    public void sendModeAndMoves(String mode, RPSPlayer.Move move1, RPSPlayer.Move move2) throws IOException {
        communication.sendMessage(mode + "," + move1.name() + "," + move2.name() + "\n");
    }

    /**
     * @brief Receives the game result from the server.
     * @return A GameDTO object containing the game mode and moves.
     * @throws RuntimeException If an error occurs during message reception.
     * @details Waits for the server response until the "|" delimiter and parses it to create a GameDTO object.
     */
    public GameDTO receiveResult() {
        String rawResponse;
        try {
            rawResponse = communication.receiveMessageUntil("|");
        } catch (IOException e) {
            throw new RuntimeException("Failed to receive a message from server: " + e);
        }
        String[] responseData = rawResponse.split(",");

        return new GameDTO(responseData[0], RPSPlayer.Move.valueOf(responseData[1]), RPSPlayer.Move.valueOf(responseData[2]));
    }

    /**
     * @brief Closes the communication object.
     * @details Ensures that the serial port and related resources are properly released.
     */
    public void close() {
        communication.close();
    }
}

