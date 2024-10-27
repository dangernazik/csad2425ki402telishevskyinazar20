package com.example.RPS_client.controller;

import com.example.RPS_client.DTO.GameDTO;
import com.example.RPS_client.RPSGame.RPSPlayer;
import com.example.RPS_client.communication.Communication;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class GameController {
    private final Communication communication;

    public GameController(int portNumber) {
        communication = new Communication(SerialPort.getCommPorts()[portNumber]);
    }

    public void sendModeAndMoves(String mode, RPSPlayer.Move move1, RPSPlayer.Move move2) throws IOException {
        communication.sendMessage(mode + "," + move1.name() + "," + move2.name() + "\n");
    }

    public GameDTO receiveResult() {
        String rawResponse;
        try {
            rawResponse = communication.receiveMessageUntil("|");
        } catch (IOException e) {
            throw new RuntimeException("Failed to receive a message from server" + e);
        }
        String[] responseData = rawResponse.split(",");

        return new GameDTO(responseData[0], RPSPlayer.Move.valueOf(responseData[1]), RPSPlayer.Move.valueOf(responseData[2]));
    }

    public void close() {
        communication.close();
    }
}
