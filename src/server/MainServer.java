package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import Client.PlayerChar;
import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.Message;

public class MainServer {

	private int tcpPort;
	private int udpPort;
	public static Server server;
	private Kryo kryo;
	public static float randomFloatNumber;
	
	public static JFrame jFrame;
	public static JTextArea jTextArea;
	static MainServerListener listener = new MainServerListener();

	public MainServer(int tcpPort, int udpPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		server = new Server();

		kryo = server.getKryo();
		registerKryoClasses();
		Random rand = new Random(1);
	}

	public void startServer() {
		Log.info("Starting Server");
		jTextArea.append("Starting Server...");
		jTextArea.append("\n");
		server.start();
		try {
			server.bind(tcpPort, udpPort);
			server.addListener(listener);
			jTextArea.append("Server online! \n");
			jTextArea.append("----------------------------");
			jTextArea.append("\n");
			update();
		} catch (IOException e) {
			Log.info("Port already used");
			jTextArea.append("Port already in use");
			jTextArea.append("\n");
			e.printStackTrace();
		}
	}

	// Try changing this to non staic and see where this effects our game
	public static void stopServer() {
		Log.info("Server stopped");
		jTextArea.append("Server stopped.");
		jTextArea.append("\n");
		server.stop();
	}

	public void update() {
		while (true) {

		}
	}

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

	public static void createServerInterface() {
		jFrame = new JFrame("GameServerInterface");
		jTextArea = new JTextArea();
		jTextArea.append("\n");
		jTextArea.setLineWrap(true);
		jTextArea.setEditable(false);

		jFrame.add(jTextArea);
		jFrame.setSize(700, 900);
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "You want to shut down the server?");
				if(i == 0) {
					stopServer();
					System.exit(0); // successful exit
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(jTextArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jFrame.add(scrollPane);
	}

	public static void main(String args[]) {
		Log.set(Log.LEVEL_INFO);
		Random rand = new Random();
		randomFloatNumber = rand.nextFloat();
		MainServer main = new MainServer(4070, 4070);
		createServerInterface();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setVisible(true);
		main.startServer();
	}

} // end total class
