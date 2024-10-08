package com.example.RPS_client.input;

import java.util.Scanner;

public class Input {
    private final Scanner scanner = new Scanner(System.in);

    public String getInputMessage() {
        System.out.println("Please, enter your name to send to Arduino and wait for receive: ");
        String message = scanner.nextLine();

        if (message.equalsIgnoreCase("exit")) {
            System.exit(0);
        }

        return message + "\n";
    }
}
