package Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.newdawn.slick.AppGameContainer;
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
import org.newdawn.slick.state.transition.SelectTransition;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.Message;

public class MultiPlayerState extends BasicGameState {

	public static float rand;

	public static JLabel label_login;
	public static JTextField login;
	public static JLabel label_password;
	public static JPasswordField password;

	public static JFrame frame;
	public Random random = new Random();
	
	private int tcpPort;
	private int udpPort;
	private int timeout;
	private String ip;
	private int flag = 0;
	public int gameOverTimer = 1000 * 15;

	public static boolean weEntered = false;
	public static boolean gameOver = false;
	public static String whoWon = "";

	public static AppGameContainer app;

	public static Client client;
	private Kryo kryo;

	public static PlayerChar player = new PlayerChar();
	static Map<Integer, MPPlayer> players = new HashMap<Integer, MPPlayer>();
	public static SpeedCube speedCube;
	public static Shape cube;

	public MultiPlayerState() {

	} // end constructor

	private void registerKryoClasses() {
		kryo.register(LoginRequest.class);
		kryo.register(LoginResponse.class);
		kryo.register(Message.class);
		kryo.register(PlayerChar.class);
		kryo.register(org.newdawn.slick.geom.Rectangle.class);
		kryo.register(float[].class);
		kryo.register(NetworkClasses.PacketUpdateX.class);
		kryo.register(NetworkClasses.PacketUpdateY.class);
		kryo.register(NetworkClasses.PacketAddPlayer.class);
		kryo.register(NetworkClasses.PacketRemovePlayer.class);
		kryo.register(NetworkClasses.PacketUserName.class);
		kryo.register(NetworkClasses.RandomNumber.class);
		kryo.register(NetworkClasses.PacketScore.class);
		kryo.register(NetworkClasses.PacketScoreCubeUpdate.class);
		kryo.register(NetworkClasses.PacketGameOver.class);
		kryo.register(NetworkClasses.PacketSpeedCube.class);
		
	}

	public void connect(String ip) {
		try {
			Log.info("connecting...");
			client.start();
			client.connect(timeout, ip, tcpPort, udpPort);
			client.addListener(new PlayerClientListener());

			LoginRequest request = new LoginRequest();
			request.setUserName(PlayerClient.userName);
			request.setUserPassword(PlayerClient.password);
			client.sendTCP(request);
			Log.info("Connected.");
		} catch (IOException e) {
			Log.info("Server offline");
			e.printStackTrace();
		}
	}

	public void disconnect() {
		Log.info("disconnecting...");
		client.stop();
	}

	public void enter(GameContainer container, StateBasedGame sbg) {
		this.ip = MenuState.ipAddress;
		this.udpPort = MenuState.port;
		this.tcpPort = MenuState.port;
		this.timeout = 500000;
		player.userName = PlayerClient.userName;

		client = new Client();
		kryo = client.getKryo();
		registerKryoClasses();
		connect(ip);

		speedCube = new SpeedCube();
	}

	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		cube = new Rectangle(rand * (800 - 16), rand * (400 - 16), 16, 16);
		System.out.println(rand + " " + cube.getX() + " " + cube.getY());
	}

	public static boolean isCollision(PlayerChar player, MPPlayer mpPlayer) {
		return !(player.x > mpPlayer.x + mpPlayer.width || player.x + player.width < mpPlayer.x
				|| player.y > mpPlayer.y + mpPlayer.height || player.y + player.height < mpPlayer.y);
	}

	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {

		if(flag == 0) {
			cube = new Rectangle(rand * (800 - 16), rand * (400 - 16), 16, 16);
			System.out.println(rand + " " + cube.getX() + " " + cube.getY());
			flag = 1;
		}

		if(container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			sbg.enterState(0, new FadeInTransition(), new SelectTransition());
		}
		
		// Collisions
		if(isCollisionShape(player, cube)) {
			player.score++;
			NetworkClasses.PacketScore packet = new NetworkClasses.PacketScore();
			packet.score = player.score;
			client.sendTCP(packet);
			changeScoreCube();
		}

		// MenuState.inputHandler = container.getInput();

		// System.out.println(GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
		player.update(delta, container);

		if(player.netX != player.x) {
			NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
			packetX.x = player.x;
			client.sendUDP(packetX);
			player.netX = player.x;
		}

		if(player.netY != player.y) {
			NetworkClasses.PacketUpdateY packetY = new NetworkClasses.PacketUpdateY();
			packetY.y = player.y;
			client.sendUDP(packetY);
			player.netY = player.y;
		}
		
		if(player.score >= 10){
			gameOver = true;
			whoWon = player.userName;
			NetworkClasses.PacketGameOver packet = new NetworkClasses.PacketGameOver();
			packet.gameFinished = true;
			packet.id = client.getID();
			client.sendTCP(packet);
		}
		
		if(gameOverTimer < 0) {
			sbg.enterState(0, new FadeInTransition(), new SelectTransition());
		}
	} // end update

	public void renderScore(GameContainer container, StateBasedGame sbg, Graphics g) {
		g.setColor(new Color(255, 0, 0, 150));
		g.fillRect(0, 50, 200, 300);
		g.setColor(new Color(255, 255, 255, 150));
		g.drawString("Name     Score", 10, 60);
		g.drawString("----     -----", 10, 70);
		g.drawString(String.format("%-5s %6d", player.userName, player.score), 10, 80);
		
		int index = 0;
		for(Map.Entry<Integer, MPPlayer> entry : players.entrySet()){
			g.drawString(String.format("%-5s %6d", entry.getValue().userName, entry.getValue().score), 10, 100 + (20 * index));
			index++;
		}
	}
	
	public void changeScoreCube() {
		cube.setX(random.nextFloat() * (800 - 16));
		cube.setY(random.nextFloat() * (400 - 16));
		NetworkClasses.PacketScoreCubeUpdate packet = new NetworkClasses.PacketScoreCubeUpdate();
		packet.x = cube.getX();
		packet.y = cube.getY();
		client.sendUDP(packet);
	}

	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {

		// Render player
		player.render(g, Color.red);

		// Render other players
		for (MPPlayer mp : players.values()) {
			mp.render(g, Color.blue);
		}

		if(MenuState.inputHandler.isKeyDown(Input.KEY_TAB)) {
			renderScore(container, sbg, g);
		}
		
		// Render score cube
		g.setColor(Color.orange);
		g.fill(cube);
		

		if(gameOver) {
			g.setColor(Color.red);
			g.drawRect(200, 170, 400, 100);
			g.setColor(new Color(156, 156, 156, 50));
			g.fillRect(200, 170, 400, 100);
			g.setColor(Color.white);
			g.drawString("Game Over!!! Won by " + whoWon, container.getWidth() / 2 - 125, container.getHeight() / 2);
			gameOverTimer -= 100;
		}
	} // end render

	public static boolean isCollisionShape(PlayerChar player, Shape cube) {
		return !(player.x > (cube.getX() + cube.getWidth()) || (player.x + player.width) < cube.getX()
				|| (player.y > cube.getY() + cube.getHeight()) || (player.y + player.height) < cube.getY());
	}

	public static void initFrame() {
		frame = new JFrame("myGame - Chat");
		frame.setSize(300, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	// public static void main(String args[]) {
	// try {
	// PlayerClient.player.userName = login.getText().trim();
	//
	// PlayerClient pClient = new PlayerClient(55555, 55556, 500000);
	// boolean DEBUG = false;
	// if(client.isConnected() || DEBUG) {
	// // initFrame();
	// app = new AppGameContainer(pClient);
	// app.setUpdateOnlyWhenVisible(false);
	// app.setTargetFrameRate(60);
	// app.setDisplayMode(800, 400, false);
	// app.setAlwaysRender(true);
	// app.start();
	//
	// }
	// } catch (SlickException e) {
	// e.printStackTrace();
	// }
	// }

	public int getID() {
		return 2;
	}

} // end total class
