/**
 * @enum RPSMode
 * @brief Enum for defining the game mode in "Rock, Paper, Scissors".
 * - MAN_VS_MAN: Human vs Human.
 * - MAN_VS_AI: Human vs AI.
 * - AI_VS_AI: AI vs AI.
 */
enum RPSMode {
  MAN_VS_MAN,
  MAN_VS_AI,
  AI_VS_AI
};

/**
 * @enum Move
 * @brief Enum for defining the move in "Rock, Paper, Scissors".
 * - ROCK: Rock.
 * - PAPER: Paper.
 * - SCISSORS: Scissors.
 */
enum Move {
  ROCK,
  PAPER,
  SCISSORS
};

/**
 * @class RPSPlayer
 * @brief Class to represent a player in the "Rock, Paper, Scissors" game.
 * @details The class contains the player's name and their move in the game.
 */
class RPSPlayer {
  private:
    String name;  ///< Player's name.
    Move move;    ///< Player's move.

  public:
    /**
     * @brief Constructor for RPSPlayer class.
     * @param name Player's name.
     * @param move Player's move.
     */
    RPSPlayer(String name, Move move) : name(name), move(move) {}

    /**
     * @brief Get the player's name.
     * @return The player's name.
     */
    String getName() {
      return name;
    }

    /**
     * @brief Get the player's move.
     * @return The player's move.
     */
    Move getMove() {
      return move;
    }
};

/**
 * @class RPSGame
 * @brief Class for the logic of the "Rock, Paper, Scissors" game.
 * @details The class contains two players and a method to determine the winner.
 */
class RPSGame {
  private:
    RPSPlayer player1;  ///< First player.
    RPSPlayer player2;  ///< Second player.

  public:
    /**
     * @brief Constructor for RPSGame class.
     * @param p1 First player.
     * @param p2 Second player.
     */
    RPSGame(RPSPlayer p1, RPSPlayer p2) : player1(p1), player2(p2) {}

    /**
     * @brief Method to start the game between two players.
     * @details Determines the winner by comparing the players' moves.
     * @return Returns a pointer to the winner or nullptr if it's a draw.
     */
    RPSPlayer* play() {
      Move move1 = player1.getMove();
      Move move2 = player2.getMove();

      // Check for a draw.
      if (move1 == move2) {
        return nullptr;
      }

      // Logic to determine the winner.
      switch (move1) {
        case ROCK:
          return (move2 == SCISSORS) ? &player1 : &player2;
        case PAPER:
          return (move2 == ROCK) ? &player1 : &player2;
        case SCISSORS:
          return (move2 == PAPER) ? &player1 : &player2;
        default:
          return nullptr;
      }
    }
};

/**
 * @brief Initialization function.
 * @details Sets up the serial connection and initializes the random number generator.
 */
void setup() {
  Serial.begin(9600);  ///< Initialize serial communication at 9600 bps.
  randomSeed(analogRead(0));  ///< Initialize the random number generator.
}

/**
 * @brief Main loop function.
 * @details Processes input from the serial port, converting it into game mode and moves. Executes the game according to the selected mode (Human vs Human, Human vs AI, AI vs AI) and outputs the result.
 */
void loop() {
  if (Serial.available() > 0) {
    String input = Serial.readStringUntil('\n');  ///< Reads the input string until a newline character.

    // Splits the input string into parts.
    int delimiterIndex1 = input.indexOf(',');
    int delimiterIndex2 = input.indexOf(',', delimiterIndex1 + 1);

    String modeString = input.substring(0, delimiterIndex1);
    String move1String = input.substring(delimiterIndex1 + 1, delimiterIndex2);
    String move2String = input.substring(delimiterIndex2 + 1);

    // Converts strings to game mode and moves.
    RPSMode mode = stringToRPSMode(modeString);
    Move player1Move;
    Move player2Move;

    String response;

    // Human vs Human mode.
    if (mode == MAN_VS_MAN) {
      player1Move = stringToMove(move1String);
      player2Move = stringToMove(move2String);

      RPSPlayer player1("Player 1", player1Move);
      RPSPlayer player2("Player 2", player2Move);

      RPSGame game(player1, player2);
      RPSPlayer* winner = game.play();

      response = convertResponse(winner, player1Move, player2Move);

    } 
    // Human vs AI mode.
    else if (mode == MAN_VS_AI) {
      player1Move = stringToMove(move1String);
      Move aiMove = generateAIMove();

      RPSPlayer player("Player 1", player1Move);
      RPSPlayer ai("AI", aiMove);

      RPSGame game(player, ai);
      RPSPlayer* winner = game.play();

      response = convertResponse(winner, player1Move, aiMove);

    } 
    // AI vs AI mode.
    else if (mode == AI_VS_AI) {
      Move ai1Move = generateAIMove();
      Move ai2Move = generateAIMove();

      RPSPlayer ai1("Player 1", ai1Move);
      RPSPlayer ai2("Player 2", ai2Move);

      RPSGame game(ai1, ai2);
      RPSPlayer* winner = game.play();

      response = convertResponse(winner, ai1Move, ai2Move);
    }

    Serial.print(response);  ///< Send the result to the serial output.
    Serial.print("|");
    delay(2000);  ///< Delay for 2 seconds before accepting the next input.
  }
}

/**
 * @brief Converts a string representing a move into the corresponding enum value.
 * @param moveString The move as a string (e.g., "ROCK").
 * @return The corresponding move enum value.
 */
Move stringToMove(String moveString) {
  if (moveString == "ROCK") return ROCK;
  if (moveString == "PAPER") return PAPER;
  if (moveString == "SCISSORS") return SCISSORS;
  return ROCK;  ///< Default move.
}

/**
 * @brief Converts a string representing a game mode into the corresponding enum value.
 * @param modeString The game mode as a string (e.g., "MAN_VS_MAN").
 * @return The corresponding game mode enum value.
 */
RPSMode stringToRPSMode(String modeString) {
  if (modeString == "MAN_VS_MAN") return MAN_VS_MAN;
  if (modeString == "MAN_VS_AI") return MAN_VS_AI;
  if (modeString == "AI_VS_AI") return AI_VS_AI;
  return MAN_VS_MAN;  ///< Default mode.
}

/**
 * @brief Converts a move enum to a string.
 * @param move The move enum value.
 * @return The corresponding string representation of the move.
 */
String moveToString(Move move) {
  switch (move) {
    case ROCK: return "ROCK";
    case PAPER: return "PAPER";
    case SCISSORS: return "SCISSORS";
    default: return "UNDEFINED";
  }
}

/**
 * @brief Generates a random move for the AI.
 * @return A randomly generated move (Rock, Paper, or Scissors).
 */
Move generateAIMove() {
  return static_cast<Move>(random(3));  ///< Randomly generate a move (0, 1, or 2).
}

/**
 * @brief Converts the result of the game into a response string.
 * @param winner The winner of the game (either player or nullptr for a draw).
 * @param player1Move The first player's move.
 * @param player2Move The second player's move.
 * @return A string representing the game result.
 */
String convertResponse(RPSPlayer* winner, Move player1Move, Move player2Move) {
  String result;

  // Determine the result: winner or draw.
  if (winner == nullptr) {
    result = "DRAW";
  } else {
    result = winner->getName();
  }

  // Convert moves to strings.
  String player1MoveStr = moveToString(player1Move);
  String player2MoveStr = moveToString(player2Move);

  // Return the formatted response.
  return result + "," + player1MoveStr + "," + player2MoveStr;
}
