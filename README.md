# cube-runner
Cube Runner: A Multiplayer Game Using Slick2D, Kryonet and LWJGL in Java
+
This is a small mockup of multiplayer game in java using Slick2D, Kryonet and LWJGL. To run the game you can use the runnable-jars provided.

#### Run the game as follows:
1. Double click server.jar which should open the server window
![image](https://user-images.githubusercontent.com/18495886/34471603-e508302a-ef1b-11e7-805b-3ab8706177e2.png)
2. Now double click client.jar which should prompt you the username and password window. Just type your username to proceed. The authentication feature has not been implemented yet. 
3. Now you can play single player as is (look for gameplay instructions below).
4. To play multiplayer game, click on multiplayer button.
5. It should prompt you for ip address and port. Enter the ip address of that machine where the server is running. Enter 4070 as the port. 4070 is the default port used for this game.
6. Other clients can connect to the same server using the same ip address and port 4070.
7. Once inside you can play the game simultaneously. 

#### Single Player Gameplay instructions:
1. You do not need to run the server to play this game mode.
2. Click on SinglePlayer button.
3. Enter the bot count (the maximum is 10).
4. Once in the game mode, you can start playing. Use WASD keys to move your cube. Try to catch the scoring cube to win before the bots do. The collision between bots has been turned off. The first to catch 10 scoring cubes wins. You can hold TAB key to check the current scores.
5. Once someone catches 10 scoring cubes, the game ends by prompting the winner name and returns to the menu state.

#### Multi-Player Gameplay instructions:
1. You need the server running in order to play this game mode.
2. Once you click the multiplayer button, you will need to enter the ip address of the server and 4070 as the port to join the multiplayer game mode.
3. Use WASD keys to move your cube. Try to catch the scoring cube to win before the other players do. The first player to catch 10 scoring cube winds. You can hold TAB key to check the current scores.
5. Once someone catches 10 scoring cubes, the game ends by prompting the winner name and returns to the menu state.

#### Load the project in Eclipse
1. Click File -> Import -> Existing Projects into Workspace and browse for this folder.
2. Once imported, make sure to add library jars as follows
3. Right click your project folder and click properties and navigate to "Java Build Path" -> "Libraries" tab. 
4. Click "Add External Jars..." and slick.jar and then add kryonet-all-2.21.jar and then lwjgl.jar. (The order is important). You can find these jars in libraries folder of this repository.
5. Now expand lwjgl.jar and and select "Native Library Location" and on the right click "Edit". Now Browse to the natives folder (of your OS) provided in the libraries folder of this repository. Then click Ok. Finally click "Apply and Close".
6. Now you can run the server by running MainServer.java under server.MainServer.java and then the client by running PlayerClient.java under Client.PlayerClient.java.

Note: However if you are getting any error saying that "Java failed to initialize lwjgl display", then go to run -> run configurations  > arguments and in jvm arguments field, add this without the quotes "-Dorg.lwjgl.opengl.Display.allowSoftwareOpenGL=true"

#### Known Bugs and Future Implementations
* Once the multiplayer game is done, you cannot return back to the game to play. So that mode must be reset in order for players to replay the multiplayer game once the game ends.
* There is no intermediate countdown state between menu state and multiplayer game state. So the client that enters the multiplayer state first may end up winning by collecting scoring cubes earlier than the other clients that join the game later. So there is a need for an intermediate countdown state to prevent this issue.

