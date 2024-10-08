package com.example.RPS_client;


import com.example.RPS_client.communication.Communication;
import com.example.RPS_client.input.Input;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Scanner;

public class Main {



    private static final Input input = new Input();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("No serial ports available.");
            return;
        }

        System.out.println("Available ports:");
        for (int i = 0; i < ports.length; i++) {
            System.out.println(i + ": " + ports[i].getSystemPortName());
        }

        System.out.println("Enter number of serial port attached to Arduino: ");
        int portNumber = scanner.nextInt();

        if (portNumber < 0 || portNumber >= ports.length) {
            System.out.println("Invalid port number. Please try again.");
            return;
        }


        Communication communication = new Communication(SerialPort.getCommPorts()[portNumber]);

        while (true) {
            communication.open();

            String messageToSend = input.getInputMessage();
            communication.send(messageToSend);

            Thread.sleep(90);

            String receivedMessage = communication.receive();
            System.out.println("Received from server: " + receivedMessage);

            communication.close();
        }
    }

}
