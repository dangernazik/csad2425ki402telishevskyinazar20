package com.example.RPS_client.communication;

import com.fazecast.jSerialComm.SerialPort;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @file Communication.java
 * @brief Class for handling serial port communication.
 * @details Provides methods to send and receive messages, as well as manage the connection to a serial port.
 */

@RequiredArgsConstructor
public class Communication {

    /**
     * @brief The serial port used for communication.
     */
    private final SerialPort serialPort;

    /**
     * @brief Input stream for reading data from the serial port.
     */
    private final InputStream inputStream;

    /**
     * @brief Output stream for writing data to the serial port.
     */
    private final OutputStream outputStream;

    /**
     * @brief Constructor of the class.
     * @param serialPort The serial port object to be used.
     * @details Opens the serial port and initializes the input/output streams.
     */
    public Communication(SerialPort serialPort) {
        this.serialPort = serialPort;
        this.serialPort.openPort();
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }

    /**
     * @brief Sends a message through the serial port.
     * @param message The message to send.
     * @throws IOException If an error occurs during writing.
     */
    public void sendMessage(String message) throws IOException {
        outputStream.write(message.getBytes());
        outputStream.flush();
    }

    /**
     * @brief Receives a message from the serial port.
     * @return The received message as a string.
     * @throws IOException If no data is received or an error occurs during reading.
     */
    public String receiveMessage() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer);

        if (bytesRead < 0) {
            throw new IOException("No data received from server");
        }

        return new String(buffer, 0, bytesRead).trim();
    }

    /**
     * @brief Receives a message from the serial port until the specified delimiter is found.
     * @param delimiter The delimiter that marks the end of the message.
     * @return The message without the delimiter.
     * @throws IOException If no data is received or an error occurs during reading.
     */
    public String receiveMessageUntil(String delimiter) throws IOException {
        StringBuilder messageBuffer = new StringBuilder();
        byte[] buffer = new byte[1];
        int bytesRead;

        while (true) {
            bytesRead = inputStream.read(buffer);

            if (bytesRead < 0) {
                throw new IOException("No data received from server");
            }

            messageBuffer.append((char) buffer[0]);

            if (messageBuffer.toString().endsWith(delimiter)) {
                break;
            }
        }

        return messageBuffer.substring(0, messageBuffer.length() - delimiter.length()).trim();
    }

    /**
     * @brief Closes the input/output streams and the serial port.
     * @details If an error occurs while closing the streams, a RuntimeException is thrown.
     */
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close I/O streams");
        }

        serialPort.closePort();
    }
}

