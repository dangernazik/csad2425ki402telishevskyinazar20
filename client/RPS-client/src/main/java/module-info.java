module com.example.RPS_client {
    requires com.fazecast.jSerialComm;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires static lombok;
    requires com.google.gson;

    opens com.example.RPS_client.RPSGame to javafx.fxml;
    exports com.example.RPS_client.RPSGame;
}