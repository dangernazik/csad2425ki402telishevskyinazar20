package com.example.RPS_client.RPSGame;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.example.RPS_client.controller.GameController;
import com.example.RPS_client.DTO.GameDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * @file RPSApp.java
 * @brief The main application class for the Rock-Paper-Scissors game.
 * @details Initializes the application, sets up the main menu, and handles game start and loading functionality.
 */
public class RPSApp extends Application {

    /**
     * @brief Player 1 instance.
     * @details Represents the first player in the game.
     */
    private RPSPlayer player1;

    /**
     * @brief Player 2 instance.
     * @details Represents the second player in the game.
     */
    private RPSPlayer player2;

    /**
     * @brief The game controller for managing server communication.
     */
    private GameController gameController;

    /**
     * @brief Constructor for the RPSApp class.
     * @details Attempts to initialize the game controller by connecting to the server. Logs an error if the connection fails.
     */
    public RPSApp() {
        try {
            gameController = new GameController(0);
        } catch (Exception ex) {
            System.err.println("Connection with server failed!");
        }
    }

    /**
     * @brief Starts the JavaFX application.
     * @param primaryStage The main stage of the JavaFX application.
     * @details Sets up the main menu, logo, and game options. Adds event handlers for starting a new game and loading a game.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rock Paper Scissors Menu");

        // Logo setup
        Image logoImage = new Image(Objects.requireNonNull(RPSApp.class.getResourceAsStream("/images/Logo.png")));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(300);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        // Menu setup
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        MenuItem loadGameItem = new MenuItem("Load Game");

        gameMenu.getItems().addAll(newGameItem, loadGameItem);
        menuBar.getMenus().add(gameMenu);

        // Event handlers for menu items
        newGameItem.setOnAction(e -> showGameModeSelection());

        loadGameItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                loadGame(file);
                startManVsManGame(primaryStage, true);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Load Failed");
                alert.setHeaderText(null);
                alert.setContentText("Game is already played");
                alert.showAndWait();
            }
        });

        // Layout and scene setup
        VBox vbox = new VBox(10, logoImageView, menuBar);
        Scene scene = new Scene(vbox, 300, 350);

        // Adding CSS styles
        scene.getStylesheets().add(Objects.requireNonNull(RPSApp.class.getResource("/styles/main.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @brief Loads game data from a JSON file.
     * @param file The file containing the saved game data.
     * @throws IOException If an error occurs while reading the file.
     * @details Parses the JSON file to extract game information such as player names, moves, and winner.
     */
    private void loadGame(File file) throws IOException {
        Gson gson = new Gson();

        // Inner class to represent game data structure
        class GameData {
            String Player1Name;
            String Player2Name;
            String Player1Move;
            String Player2Move;
            String Winner; // Can be null or empty if the game is ongoing
        }

        // Check if file exists
        if (file == null || !file.exists()) {
            System.out.println("File does not exist: " + (file != null ? file.getAbsolutePath() : "null"));
            return;
        }

        try (Reader reader = new FileReader(file)) {
            GameData gameData = gson.fromJson(reader, GameData.class);

            if (gameData == null) {
                System.out.println("No game data found or JSON is malformed.");
                return;
            }

            System.out.println("Player 1 Name: " + (gameData.Player1Name != null ? gameData.Player1Name : "Not Provided"));
            System.out.println("Player 2 Name: " + (gameData.Player2Name != null ? gameData.Player2Name : "Not Provided"));
            System.out.println("Player 1 Move: " + (gameData.Player1Move != null && !gameData.Player1Move.isEmpty() ? gameData.Player1Move : "Not Made"));
            System.out.println("Player 2 Move: " + (gameData.Player2Move != null && !gameData.Player2Move.isEmpty() ? gameData.Player2Move : "Not Made"));
            System.out.println("Winner: " + (gameData.Winner != null && !gameData.Winner.isEmpty() ? gameData.Winner : "Game Ongoing"));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while processing the file: " + e.getMessage());
        }
    }

    /**
     * @brief Displays the game mode selection menu.
     * @details Allows the user to select a game mode: Player vs Player, Player vs AI, or AI vs AI.
     */
    private void showGameModeSelection() {
        Stage modeStage = new Stage();
        modeStage.setTitle("Select Game Mode");

        Button manVsManButton = new Button(RPSMode.MAN_VS_MAN.name().replace("_", " "));
        Button manVsAIButton = new Button(RPSMode.MAN_VS_AI.name().replace("_", " "));
        Button aiVsAIButton = new Button(RPSMode.AI_VS_AI.name().replace("_", " "));

        // Event handlers for game modes
        manVsManButton.setOnAction(e -> {
            player1 = new RPSPlayer();
            player2 = new RPSPlayer();
            setPlayerNicknames(RPSMode.MAN_VS_MAN);
        });

        manVsAIButton.setOnAction(e -> {
            player1 = new RPSPlayer();
            player2 = new RPSPlayer();
            setPlayerNicknames(RPSMode.MAN_VS_AI);
        });

        aiVsAIButton.setOnAction(e -> {
            player1 = new RPSPlayer();
            player2 = new RPSPlayer();
            startAiVsAiGame(modeStage);
        });

        VBox modeLayout = new VBox(10, manVsManButton, manVsAIButton, aiVsAIButton);
        Scene modeScene = new Scene(modeLayout, 300, 200);
        modeScene.getStylesheets().add(Objects.requireNonNull(RPSApp.class.getResource("/styles/main.css")).toExternalForm());
        modeStage.setScene(modeScene);
        modeStage.show();
    }

    /**
     * @brief Starts the AI vs AI game.
     * @param modeStage The stage to close after selecting AI vs AI mode.
     * @details Simulates a game between two AI players, displays their moves, and shows the result.
     */
    private void startAiVsAiGame(Stage modeStage) {
        modeStage.close();

        Stage gameStage = new Stage();
        gameStage.setTitle("AI vs AI Game");

        GridPane grid = new GridPane();
        HBox moveImages = new HBox(10);
        grid.add(moveImages, 0, 5, 3, 1);

        Label player1Label = new Label("AI 1's Move:");
        Label player1MoveLabel = new Label();
        grid.add(player1Label, 0, 0);
        grid.add(player1MoveLabel, 1, 0);

        Label player2Label = new Label("AI 2's Move:");
        Label player2MoveLabel = new Label();
        grid.add(player2Label, 0, 1);
        grid.add(player2MoveLabel, 1, 1);

        Button playButton = new Button("Play");
        grid.add(playButton, 0, 2, 3, 1);
        Label resultLabel = new Label();
        grid.add(resultLabel, 0, 3, 3, 1);

        playButton.setOnAction(e -> {
            try {
                gameController.sendModeAndMoves(RPSMode.AI_VS_AI.name(), RPSPlayer.Move.ROCK, RPSPlayer.Move.ROCK);
                GameDTO gameResponseDto = gameController.receiveResult();

                String resultText = gameResponseDto.gameResult().equals("DRAW") ? "Draw"
                        : (gameResponseDto.gameResult().equals("Player 1") ? "AI1" : "AI2");

                String movesHistory = "AI 1 put " + gameResponseDto.player1Move().name() + ". "
                        + "AI 2 put " + gameResponseDto.player2Move().name();
                resultLabel.setText("Result: " + resultText + "\nMoves: " + movesHistory);

                moveImages.getChildren().clear();
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player1Move()));
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player2Move()));
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Server connection failed");
                alert.showAndWait();
            }
        });

        Scene gameScene = new Scene(grid, 500, 300);
        gameScene.getStylesheets().add(Objects.requireNonNull(RPSApp.class.getResource("/styles/main.css")).toExternalForm());
        gameStage.setScene(gameScene);
        gameStage.show();
    }


    /**
     * @brief Displays a dialog for players to set their nicknames.
     * @param gameMode The game mode selected (MAN_VS_MAN or MAN_VS_AI).
     * @details Allows players to input their names and initializes the game based on the selected mode.
     */
    private void setPlayerNicknames(RPSMode gameMode) {
        Stage nicknameStage = new Stage();
        nicknameStage.setTitle("Set Player Nicknames");

        TextField player1NameField = new TextField();
        player1NameField.setPromptText("Enter Player 1 Name");

        TextField player2NameField = new TextField();
        player2NameField.setPromptText("Enter Player 2 Name");

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            player1.setName(player1NameField.getText());
            player2.setName(player2NameField.getText());

            if (gameMode.equals(RPSMode.MAN_VS_MAN)) {
                startManVsManGame(nicknameStage, false);
            }
            if (gameMode.equals(RPSMode.MAN_VS_AI)) {
                startManVsAiGame(nicknameStage);
            }

        });

        VBox nicknameLayout = new VBox(10, player1NameField, player2NameField, confirmButton);
        Scene nicknameScene = new Scene(nicknameLayout, 300, 200);
        nicknameScene.getStylesheets().add(Objects.requireNonNull(RPSApp.class.getResource("/styles/main.css")).toExternalForm());
        nicknameStage.setScene(nicknameScene);
        nicknameStage.show();
    }

    /**
     * @brief Starts a Man vs AI game.
     * @param nicknameStage The stage where player nicknames were entered (will be closed).
     * @details Allows a human player to make a move, and the AI generates its move. Displays the result and moves history.
     */
    private void startManVsAiGame(Stage nicknameStage) {
        nicknameStage.close();

        Stage gameStage = new Stage();
        gameStage.setTitle("Man vs AI Game");

        GridPane grid = new GridPane();
        HBox moveImages = new HBox(10);
        grid.add(moveImages, 0, 5, 3, 1);

        Label player1Label = new Label(player1.getName() + "'s Move:");
        ChoiceBox<RPSPlayer.Move> player1Move = new ChoiceBox<>();
        player1Move.getItems().addAll(RPSPlayer.Move.values());
        Button player1MakeMoveButton = new Button("Make Move");
        grid.add(player1Label, 0, 0);
        grid.add(player1Move, 1, 0);
        grid.add(player1MakeMoveButton, 2, 0);

        Label player2Label = new Label("AI's Move:");
        Label player2MoveLabel = new Label();
        grid.add(player2Label, 0, 1);
        grid.add(player2MoveLabel, 1, 1);

        Button playButton = new Button("Play");
        grid.add(playButton, 0, 2, 3, 1);
        Label resultLabel = new Label();
        grid.add(resultLabel, 0, 3, 3, 1);

        // Button for Player 1 to make a move and lock their choice.
        player1MakeMoveButton.setOnAction(e -> {
            if (player1Move.getValue() != null) {
                player1.setMove(player1Move.getValue());
                player1Move.setDisable(true);
                player1Move.getSelectionModel().clearSelection();
                resultLabel.setText(player1.getName() + " has made their move!");
            } else {
                resultLabel.setText("Player 1 must make a move!");
            }
        });

        // Button to play the game: sends moves to the server, receives the result, and updates UI.
        playButton.setOnAction(e -> {
            if (player1.getMove() != null) {
                try {
                    gameController.sendModeAndMoves(RPSMode.MAN_VS_AI.name(), player1.getMove(), RPSPlayer.Move.ROCK);
                    GameDTO gameResponseDto = gameController.receiveResult();

                    String resultText;
                    if (gameResponseDto.gameResult().equals("DRAW")) {
                        resultText = "Draw";
                    } else {
                        resultText = gameResponseDto.gameResult().equals("Player 1") ? player1.getName() : "AI";
                    }

                    String movesHistory = player1.getName() + " put " + gameResponseDto.player1Move().name() + ". "
                            + player2.getName() + " put " + gameResponseDto.player2Move().name();
                    resultLabel.setText("Result: " + resultText + " wins!\n Moves: " + movesHistory);

                    moveImages.getChildren().clear();
                    moveImages.getChildren().add(createMoveImage(gameResponseDto.player1Move()));
                    moveImages.getChildren().add(createMoveImage(gameResponseDto.player2Move()));
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Connection Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Server connection failed");
                    alert.showAndWait();
                }
            } else {
                resultLabel.setText("Player 1 must make a move!");
            }
        });

        // Apply styles and display the game stage.
        Scene gameScene = new Scene(grid, 500, 300);
        gameScene.getStylesheets().add(Objects.requireNonNull(RPSApp.class.getResource("/styles/main.css")).toExternalForm());
        gameStage.setScene(gameScene);
        gameStage.show();

    }

    /**
     * @brief Starts a Man vs Man game.
     * @param nicknameStage The stage where player nicknames were entered (will be closed).
     * @param isLoaded Flag indicating if the game is being loaded from a saved state.
     * @details Initializes the Man vs Man game UI. Handles player moves, game result calculation,
     * and saving the game result to a JSON file.
     */
    private void startManVsManGame(Stage nicknameStage, boolean isLoaded) {
        nicknameStage.close();

        Stage gameStage = new Stage();
        gameStage.setTitle("Man vs Man Game");

        GridPane grid = new GridPane();
        HBox moveImages = new HBox(10);
        grid.add(moveImages, 0, 5, 3, 1);

        Label player1Label = new Label(player1.getName() + "'s Move:");
        ChoiceBox<RPSPlayer.Move> player1Move = new ChoiceBox<>();
        player1Move.getItems().addAll(RPSPlayer.Move.values());
        Button player1MakeMoveButton = new Button("Make Move");
        grid.add(player1Label, 0, 0);
        grid.add(player1Move, 1, 0);
        grid.add(player1MakeMoveButton, 2, 0);

        Label player2Label = new Label(player2.getName() + "'s Move:");
        ChoiceBox<RPSPlayer.Move> player2Move = new ChoiceBox<>();
        player2Move.getItems().addAll(RPSPlayer.Move.values());
        Button player2MakeMoveButton = new Button("Make Move");
        grid.add(player2Label, 0, 1);
        grid.add(player2Move, 1, 1);
        grid.add(player2MakeMoveButton, 2, 1);

        Button playButton = new Button("Play");
        grid.add(playButton, 0, 2, 3, 1);
        Label resultLabel = new Label();
        grid.add(resultLabel, 0, 3, 3, 1);

        Button saveButton = new Button("Save to Json");
        grid.add(saveButton, 0, 4, 3, 1);

        if (isLoaded) {
            if (player1.getMove() != null) {
                player1Move.setDisable(true);
                player1MakeMoveButton.setDisable(true);
            }

            if (player2.getMove() != null) {
                player2Move.setDisable(true);
                player2MakeMoveButton.setDisable(true);
            }
        }
        // Button for Player 1 to make a move and lock their choice.
        player1MakeMoveButton.setOnAction(e -> {
            if (player1Move.getValue() != null) {
                player1.setMove(player1Move.getValue());
                player1Move.setDisable(true);
                player1Move.getSelectionModel().clearSelection();
                resultLabel.setText(player1.getName() + " has made their move!");
            } else {
                resultLabel.setText("Player 1 must make a move!");
            }
        });
        // Button for Player 2 to make a move and lock their choice.
        player2MakeMoveButton.setOnAction(e -> {
            if (player2Move.getValue() != null) {
                player2.setMove(player2Move.getValue());
                player2Move.setDisable(true);
                player2Move.getSelectionModel().clearSelection();
                resultLabel.setText(player2.getName() + " has made their move!");
            } else {
                resultLabel.setText("Player 2 must make a move!");
            }
        });

        String[] gameResultHolder = new String[1];

        // Button to play the game: sends moves to the server, receives the result, and updates UI.
        playButton.setOnAction(e -> {
            try {
                gameController.sendModeAndMoves(RPSMode.MAN_VS_MAN.name(), player1.getMove(), player2.getMove());
                GameDTO gameResponseDto = gameController.receiveResult();

                if (gameResponseDto.gameResult().equals("DRAW")) {
                    gameResultHolder[0] = "Draw";
                } else {
                    gameResultHolder[0] = gameResponseDto.gameResult().equals("Player 1") ? player1.getName() : player2.getName();
                }

                String movesHistory = player1.getName() + " put " + gameResponseDto.player1Move().name() + ". "
                        + player2.getName() + " put " + gameResponseDto.player2Move().name();
                resultLabel.setText("Result: " + gameResultHolder[0] + " wins!\n Moves: " + movesHistory);

                moveImages.getChildren().clear();
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player1Move()));
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player2Move()));
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Server connection failed");
                alert.showAndWait();
            }
        });
        // Button to save the game result to a JSON file.
        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game Result");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showSaveDialog(gameStage);

            if (file != null) {
                try {
                    saveGameResult(file, String.valueOf(gameResultHolder[0]), player1, player2);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Save Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Game result saved successfully!");
                    alert.showAndWait();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to save game result: " + ex.getMessage());
                    alert.showAndWait();
                }
            }
        });
        // Apply styles and display the game stage.
        Scene gameScene = new Scene(grid, 500, 300);
        gameScene.getStylesheets().add(Objects.requireNonNull(RPSApp.class.getResource("/styles/main.css")).toExternalForm());
        gameStage.setScene(gameScene);
        gameStage.show();
    }


    /**
     * @brief Saves the game result to a JSON file.
     * @param file The file to save the game result to.
     * @param result The result of the game ("Player 1", "Player 2", or "Draw").
     * @param player1 The first player object containing name and move.
     * @param player2 The second player object containing name and move.
     * @throws IOException If an I/O error occurs during file writing.
     * @details Generates a JSON representation of the game data and saves it in a user-specified file.
     */
    private void saveGameResult(File file, String result, RPSPlayer player1, RPSPlayer player2) throws IOException {
        // Create a GsonBuilder to enable pretty printing
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> gameData = new HashMap<>();

        gameData.put("Player1Name", player1.getName());
        gameData.put("Player2Name", player2.getName());
        gameData.put("Player1Move", player1.getMove() != null ? player1.getMove().name() : null);
        gameData.put("Player2Move", player2.getMove() != null ? player2.getMove().name() : null);
        gameData.put("Winner", result);

        // Convert the game data to a pretty-printed JSON string
        String json = gson.toJson(gameData);

        // Write the pretty-printed JSON to the specified file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(json);
        }
    }


    /**
     * @brief Creates an ImageView for the given move.
     * @param move The player's move (ROCK, PAPER, or SCISSORS).
     * @return An ImageView displaying the corresponding move image.
     * @details Maps the move to its corresponding image resource and configures
     * the image view with a fixed width and aspect ratio preservation.
     */
    private ImageView createMoveImage(RPSPlayer.Move move) {
        String imagePath = "";

        switch (move) {
            case ROCK:
                imagePath = "/images/Rock.png";
                break;
            case PAPER:
                imagePath = "/images/Paper.png";
                break;
            case SCISSORS:
                imagePath = "/images/Scissors.png";
                break;
        }
        Image image = new Image(RPSApp.class.getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }


    /**
     * @brief Entry point of the application.
     * @param args Command-line arguments.
     * @details Launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
