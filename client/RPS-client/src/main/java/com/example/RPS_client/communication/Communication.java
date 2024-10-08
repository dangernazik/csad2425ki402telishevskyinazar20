package com.example.RPS_client.communication;

import com.fazecast.jSerialComm.SerialPort;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class Communication {

    private final SerialPort serialPort;


    public void open() {
        serialPort.openPort();
    }


    public void send(String message) throws IOException {
        serialPort.getOutputStream().write(message.getBytes());
        serialPort.getOutputStream().flush();
    }


    public String receive() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = serialPort.getInputStream().read(buffer);

        if (bytesRead < 0) {
            throw new IOException("No data received from server");
        }

        return new String(buffer, 0, bytesRead);
    }


    public void close() {
        serialPort.closePort();
    }
}
