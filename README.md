# csad2425ki402telishevskyinazar20
 
 ## Repo Details
 This repository was created for the development of a game project, the server part of which will be created on HW, and the client part on SW.  

 ## Task Details
 The game should have a menu, several game modes: 
 ∙ Man vs AI
 ∙ Man vs Man
 ∙ AI vs AI:
   ○ Random move;
   ○ Win strategy.
 Actions to be performed: 
 ∙ New;
 ∙ Save;
 ∙ Load.

 ## Individual task 
 My number is 20, so my task is GAME rock paper scissors with CONFIG FORMAT json.

 ## Technology
 I will use Java to write Desktop SW program, Java.swing to create GUI, and Arduino Uno Rev3 debug board for development of server HW part. 
 
--- 

 # Task 2: Creating Communication Between Java Client and Arduino Server

This section outlines how to build the project for the Java client and the Arduino server.

## 1. How to Build the Project for the Client and Arduino Server

### Building the Java Client
To build the Java client, follow these steps:

1. **Download Dependencies**: In the root directory of your Java project, run the following command:
   ```bash
   mvn clean compile
   ```
   This command will download all the necessary libraries specified in the `pom.xml` file.

2. **Package the Project**: To create a JAR file, execute the command:
   ```bash
   mvn clean package
   ```
   This will generate a JAR file in the `target` directory of your project.

### Building the Arduino Server
To build the Arduino server, perform the following actions:

1. **Install Arduino IDE**: Download and install the Arduino IDE if you haven't already.

2. **Open the Project**: Open the `RPS-server.ino` in folder */server/RPS-server* in the Arduino IDE.

3. **Compile the Code**: Click on the **Verify** button (checkmark icon) in the IDE to compile the code. Ensure that there are no errors in the code.

4. **Connect device**: Connect your Arduino Uno.


## 2. How to Run the Client and Arduino Server

### Running the Java Client
1. To run the Java client, use *Main.java*
 
### Running the Arduino Server
To run the Arduino server, follow these steps:

1. In the Arduino IDE, after client project started select the appropriate board and port, then click the **Upload** button (right arrow icon) to upload the code to the Arduino.

Once both the Java client and the Arduino server are running, they will be able to communicate with each other. For any further assistance, please refer to the documentation or contact support.

# Task 3: Implementing a Rock-Paper-Scissors Game with Java GUI Client and Arduino Server

In this section, we will detail the steps to create a fully functional Rock-Paper-Scissors game, incorporating a Java client with a graphical user interface (GUI) and an Arduino server. We will also provide instructions for using a local PowerShell script to build and deploy the entire project seamlessly.

## 1. Overview of the Project Structure

The project consists of two main components:

- **Java Client**: A desktop application with a GUI that allows players to select their moves and view game results.
- **Arduino Server**: A microcontroller that manages game logic and communicates with the Java client.

## 2. Setting Up the Java GUI Client

### Designing the User Interface
1. **Create the GUI**: The Java client uses JavaFX to create an interactive user interface. Ensure you have the necessary JavaFX libraries integrated into your project.

2. **Implement Game Logic**: The client should handle user inputs and send selected moves to the Arduino server. Use sockets for communication.

### Building the Java Client
To build the Java client, perform the following:

1. **Open Command Line**: Navigate to the root directory of your Java project.

2. **Compile and Package**: Execute the commands:
   ```bash
   mvn clean compile
   mvn package
   ```

## 3. Setting Up the Arduino Server

### Programming the Arduino
1. **Open the Arduino IDE**: Load the `RPS-server.ino` file located in the `/server/RPS-server` folder.

2. **Implement Communication Protocol**: The server should be programmed to receive moves from the client, determine the winner, and send back the results.

3. **Upload the Code**: Once your code is ready, select the appropriate board and port in the Arduino IDE and click the **Upload** button.

## 4. Using the PowerShell Build Script

A PowerShell script is included to streamline the build process for both the Java client and Arduino server. Follow these steps:

1. **Launch PowerShell**: Open PowerShell on your Windows machine.

2. **Navigate to Project Directory**: Change to the project’s root directory:
   ```powershell
   cd "D:\path\to\your\project\csad2425ki402telishevskyinazar20"
   ```

3. **Run the Build Script**: Execute the script to build the client and server:
   ```powershell
   .\localCI.ps1
   ```
   This script will automatically compile the Java client, package it into a JAR file, compile the Arduino code, and upload it to the connected Arduino device.

## 5. Running the Game

1. **Start the Arduino Server**: Ensure the Arduino is connected and the code is uploaded.

2. **Download the JAR Files from GitHub Actions**: After the CI process completes successfully on GitHub Actions, you can download the `deploy` directory artifact from Task3. The artifact contains the packaged JAR files. To access the artifact:
   - Navigate to the Actions tab in your GitHub repository.
   - Select the Task3 workflow run.
   - Find the Artifacts section on the page and download the `deploy` directory. This will contain the packaged `RPS-client` JAR file.

3. **Set Up JavaFX**: To run the Java client, you will need the JavaFX SDK. You can download it from the official JavaFX website. Once the SDK is downloaded, make sure you update the following paths:
   - `<your path to javafx-sdk>`: This should point to the directory where you downloaded the JavaFX SDK.
   - `<your path to jar file with dependencies>`: This should point to the location of the `RPS-client-0.0.1-jar-with-dependencies.jar` file within the `deploy` folder.

4. **Run the Java Client**: Use PowerShell to run the Java client with the following command:

   `java --module-path "<your path to javafx-sdk>\lib\javafx.base.jar;<your path to javafx-sdk>\lib\javafx.controls.jar;<your path to javafx-sdk>\lib\javafx.fxml.jar;<your path to javafx-sdk>\lib\javafx.graphics.jar" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics -jar "<your path to jar file with dependencies>"`

Replace the placeholders `<your path to javafx-sdk>` and `<your path to jar file with dependencies>` with the appropriate paths on your system.


# Task 4: Implementing a doxygen Documentation
Run the loclCI.ps1 script and it will create documentation of prohect to deploy/docs folder.
 develop
