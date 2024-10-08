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
