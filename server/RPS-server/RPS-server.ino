#include <Arduino.h>

enum RPSMode {
  MAN_VS_MAN,
  MAN_VS_AI,
  AI_VS_AI
};

enum Move {
  ROCK,
  PAPER,
  SCISSORS
};

class RPSPlayer {
  private:
    String name;
    Move move;

  public:
    RPSPlayer(String name, Move move) : name(name), move(move) {}

    String getName() {
      return name;
    }

    Move getMove() {
      return move;
    }
};

class RPSGame {
  private:
    RPSPlayer player1;
    RPSPlayer player2;

  public:
    RPSGame(RPSPlayer p1, RPSPlayer p2) : player1(p1), player2(p2) {}

    RPSPlayer* play() {
      Move move1 = player1.getMove();
      Move move2 = player2.getMove();

      if (move1 == move2) {
        return nullptr;
      }

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

void setup() {
  Serial.begin(9600);
  randomSeed(analogRead(0));
}

void loop() {
  if (Serial.available() > 0) {
    String input = Serial.readStringUntil('\n');

    int delimiterIndex1 = input.indexOf(',');
    int delimiterIndex2 = input.indexOf(',', delimiterIndex1 + 1);

    String modeString = input.substring(0, delimiterIndex1);
    String move1String = input.substring(delimiterIndex1 + 1, delimiterIndex2);
    String move2String = input.substring(delimiterIndex2 + 1);

    RPSMode mode = stringToRPSMode(modeString);
    Move player1Move;
    Move player2Move;

    String response;

    if (mode == MAN_VS_MAN) {
      player1Move = stringToMove(move1String);
      player2Move = stringToMove(move2String);

      RPSPlayer player1("Player 1", player1Move);
      RPSPlayer player2("Player 2", player2Move);

      RPSGame game(player1, player2);
      RPSPlayer* winner = game.play();

      response = convertResponse(winner, player1Move, player2Move);

    } else if (mode == MAN_VS_AI) {
      player1Move = stringToMove(move1String);
      Move aiMove = generateAIMove();

      RPSPlayer player("Player 1", player1Move);
      RPSPlayer ai("AI", aiMove);

      RPSGame game(player, ai);
      RPSPlayer* winner = game.play();

      response = convertResponse(winner, player1Move, aiMove);

    } else if (mode == AI_VS_AI) {
      Move ai1Move = generateAIMove();
      Move ai2Move = generateAIMove();

      RPSPlayer ai1("Player 1", ai1Move);
      RPSPlayer ai2("Player 2", ai2Move);

      RPSGame game(ai1, ai2);
      RPSPlayer* winner = game.play();

      response = convertResponse(winner, ai1Move, ai2Move);
    }

    Serial.print(response);
    Serial.print("|");
    delay(2000);
  }
}

Move stringToMove(String moveString) {
  if (moveString == "ROCK") return ROCK;
  if (moveString == "PAPER") return PAPER;
  if (moveString == "SCISSORS") return SCISSORS;
  return ROCK;
}

RPSMode stringToRPSMode(String modeString) {
  if (modeString == "MAN_VS_MAN") return MAN_VS_MAN;
  if (modeString == "MAN_VS_AI") return MAN_VS_AI;
  if (modeString == "AI_VS_AI") return AI_VS_AI;
}

String moveToString(Move move) {
  switch (move) {
    case ROCK: return "ROCK";
    case PAPER: return "PAPER";
    case SCISSORS: return "SCISSORS";
    default: return "UNDEFINED";
  }
}

Move generateAIMove() {
  return static_cast<Move>(random(3));
}

String convertResponse(RPSPlayer* winner, Move player1Move, Move player2Move) {
  String result;

  if (winner == nullptr) {
    result = "DRAW";
  } else {
    result = winner->getName();
  }

  String player1MoveStr = moveToString(player1Move);
  String player2MoveStr = moveToString(player2Move);

  return result + "," + player1MoveStr + "," + player2MoveStr;
}