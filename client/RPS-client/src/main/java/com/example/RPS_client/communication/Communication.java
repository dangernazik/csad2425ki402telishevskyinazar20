package com.example.RPS_client.communication;

import com.fazecast.jSerialComm.SerialPort;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RequiredArgsConstructor
public class Communication {

    private final SerialPort serialPort;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public Communication(SerialPort serialPort) {
        this.serialPort = serialPort;
        this.serialPort.openPort();
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }


    public void sendMessage(String message) throws IOException {
        outputStream.write(message.getBytes());
        outputStream.flush();
    }


    public String receiveMessage() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer);

        if (bytesRead < 0) {
            throw new IOException("No data received from server");
        }

        return new String(buffer, 0, bytesRead).trim();
    }


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
