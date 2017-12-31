package Client;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.SelectTransition;

public class SinglePlayerState extends BasicGameState {

	public static SinglePlayer player;
	public static SinglePlayer ai;

	public boolean gameOver = false;
	public static boolean backToMenu = false;
	public String whoWon = "";

	public int bots;
	public int mainDelta = 0;
	public static int flag = 0;
	int anotherFlag = 0;
	boolean isThisFirstTime = false;

	public int gameOverTimer = 1000 * 15;

	public static Shape cube;

	public Random rand = new Random();
	public static ArrayList<SinglePlayer> gameBots;

	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		player = new SinglePlayer(PlayerClient.userName);
		cube = new Rectangle(rand.nextFloat() * (800 - 16), rand.nextFloat() * (400 - 16), 16, 16);
	}

	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		if(container.getInput().isKeyDown(Input.KEY_ESCAPE)){
			closeEverything();
			sbg.enterState(0, new FadeInTransition(), new SelectTransition());
		}
		if(flag == 0) {
			mainDelta = delta;
			player = new SinglePlayer(PlayerClient.userName);
			bots = MenuState.botCount;
			if(MenuState.botCount > 10 || MenuState.botCount <= 0) bots = 10;
			gameBots = new ArrayList<>();
			for (int i = 0; i < bots; i++) {
				gameBots.add(new SinglePlayer("Bot " + (i + 1)));
				gameBots.get(i).speed = rand.nextInt(30);
			}
			cube = new Rectangle(rand.nextFloat() * 800, rand.nextFloat() * 400, 16, 16);
			flag = 1;
		}
		if(player.score >= 10) {
			gameOver = true;
			whoWon = player.userName;
		} else {
			for (int i = 0; i < gameBots.size(); i++) {
				if(gameBots.get(i).score >= 10) {
					gameOver = true;
					whoWon = gameBots.get(i).userName;
				}
			}
		}

		// Updates
		player.update(delta, container, false);
		for (int i = 0; i < gameBots.size(); i++) {
			gameBots.get(i).update(delta, container, true);
		}

		// Collisions
		if(isCollisionShape(player, cube)) {
			player.score++;
			changeScoreCube();
		}
		for (int i = 0; i < gameBots.size(); i++) {
			if(isCollisionShape(gameBots.get(i), cube)) {
				gameBots.get(i).score++;
				changeScoreCube();
			}
		}

		// gameOverTimer
		if(gameOverTimer < 0) {
			closeEverything();
			backToMenu = true;
			sbg.enterState(0, new FadeInTransition(), new SelectTransition());
		}
	}

	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		if(!backToMenu) {
			player.render(g, Color.red);

			for (int i = 0; i < gameBots.size(); i++) {
				gameBots.get(i).render(g, Color.blue);
			}
			// render cube
			// System.out.println(cube.getX()+","+cube.getY()+","+cube.getWidth()+","+cube.getHeight());
			g.setColor(Color.orange);
			g.fill(cube);

			if(MenuState.inputHandler.isKeyDown(Input.KEY_TAB)) {
				renderScore(container, sbg, g);
			}

			if(gameOver) {
				g.setColor(Color.red);
				g.drawRect(200, 170, 400, 100);
				g.setColor(new Color(156, 156, 156, 50));
				g.fillRect(200, 170, 400, 100);
				g.setColor(Color.white);
				g.drawString("Game Over!!! Won by " + whoWon, container.getWidth() / 2 - 125,
						container.getHeight() / 2);
				gameOverTimer -= 100;
			}
		} // end if
	}

	public void closeEverything() {
		player.score = 0;

		for (int i = 0; i < gameBots.size(); i++) {
			gameBots.get(i).score = 0;
		}
		flag = 0;
		mainDelta = 0;
		anotherFlag = 0;
		gameOverTimer = 1000 * 8;
		// backToMenu = false;
		gameOver = false;
		whoWon = "";
		gameBots.clear();
	}

	public void changeScoreCube() {
		cube.setX(rand.nextFloat() * (800 - 16));
		cube.setY(rand.nextFloat() * (400 - 16));
	}
	
	public void renderScore(GameContainer container, StateBasedGame sbg, Graphics g) {
		g.setColor(new Color(255, 0, 0, 150));
		g.fillRect(0, 50, 200, 300);
		g.setColor(new Color(255, 255, 255, 150));
		g.drawString("Name     Score", 10, 60);
		g.drawString("----     -----", 10, 70);
		g.drawString(String.format("%-5s %6d", player.userName, player.score), 10, 80);
		for (int i = 0; i < gameBots.size(); i++) {
			g.drawString(String.format("%-5s %6d", gameBots.get(i).userName, gameBots.get(i).score), 10,
					100 + (20 * i));
		}
	}

	public static boolean isCollisionShape(SinglePlayer player, Shape cube) {
		return !(player.x > (cube.getX() + cube.getWidth()) || (player.x + player.width) < cube.getX()
				|| (player.y > cube.getY() + cube.getHeight()) || (player.y + player.height) < cube.getY());
	}

	public static boolean isCollision(SinglePlayer player, SinglePlayer ai) {
		return !(player.x > ai.x + ai.width || player.x + player.width < ai.x || player.y > ai.y + ai.height
				|| player.y + player.height < ai.y);
	}

	public int getID() {
		return 1;
	}
} // end total class
