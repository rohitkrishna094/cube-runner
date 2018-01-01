# cube-runner
Cube Runner: A Multiplayer Game Using Slick2D, Kryonet and LWJGL

This is a small mockup of multiplayer game in java using Slick2D, Kryonet and LWJGL. To run the game you can use the runnable-jars provided.

#### Run the game as follows:
1. Double click server.jar which should open the server window
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


