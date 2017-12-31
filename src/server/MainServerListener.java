package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import Client.PlayerChar;
import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.PacketAddPlayer;
import NetworkClasses.PacketUserName;
import NetworkClasses.RandomNumber;

public class MainServerListener extends Listener {

	public static Map<Integer, PlayerChar> players = new HashMap<Integer, PlayerChar>();

	public void connected(Connection connection) {
		PlayerChar player = new PlayerChar();
		player.c = connection;

		// add packet or init packet
		PacketAddPlayer addPacket = new PacketAddPlayer();
		addPacket.id = connection.getID();
		MainServer.server.sendToAllExceptTCP(connection.getID(), addPacket);

		for (PlayerChar p : players.values()) {
			PacketAddPlayer addPacket2 = new PacketAddPlayer();
			addPacket2.id = p.c.getID();
			connection.sendTCP(addPacket2);
		}

		players.put(connection.getID(), player);
		MainServer.jTextArea.append(connection.getID() + " (ID) joined the server (Player.entity)");
		MainServer.jTextArea.append("\n");
	} // end connected

	public void disconnected(Connection connection) {
		players.remove(connection.getID());
		NetworkClasses.PacketRemovePlayer removePacket = new NetworkClasses.PacketRemovePlayer();
		removePacket.id = connection.getID();
		MainServer.server.sendToAllExceptTCP(connection.getID(), removePacket);
		MainServer.jTextArea.append(connection.getID() + " (ID) left the server (Player.entity)");
		MainServer.jTextArea.append("\n");
		MainServer.jTextArea.append("\n");
	} // end disconnected

	public void received(Connection connection, Object object) {
		if(object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest) object;
			LoginResponse response = new LoginResponse();

			// // check with database
			// if(request.getUserName().equalsIgnoreCase("rAlA") &&
			// request.getUserPassword().equalsIgnoreCase("test")){
			// response.setResponseText("ok");
			// MainServer.jTextArea.append(connection.getRemoteAddressTCP() + "
			// connected.");
			// MainServer.jTextArea.append("\n");
			// MainServer.jTextArea.append("\n");
			// }
			// else{
			// response.setResponseText("no");
			// MainServer.jTextArea.append(connection.getRemoteAddressTCP() + "
			// connected, but with invalid userdata");;
			// MainServer.jTextArea.append("\n");
			// MainServer.jTextArea.append("\n");
			// }

			// remove this below line
			response.setResponseText("ok");
			connection.sendTCP(response);
			PacketUserName packetUserName = new PacketUserName();
			packetUserName.id = connection.getID();
			packetUserName.userName = request.getUserName();
			// MainServer.server.sendToAllExceptTCP(connection.getID(),
			// packetUserName);
			MainServer.server.sendToAllExceptUDP(connection.getID(), packetUserName);

			players.get(connection.getID()).userName = request.getUserName();
			// MainServer.jTextArea.append("we got" + request.getUserName());
			for (PlayerChar p : players.values()) {
				PacketUserName packetUserName2 = new PacketUserName();
				packetUserName2.id = p.c.getID();
				packetUserName2.userName = p.userName;
				// connection.sendTCP(packetUserName2);
				connection.sendUDP(packetUserName2);
			}
		}

		// random number packet, sync'd across the entire network
		RandomNumber packetRandom = new RandomNumber();
		packetRandom.randomFloat = 0.254f;
		MainServer.server.sendToTCP(connection.getID(),packetRandom);

		if(object instanceof NetworkClasses.PacketUpdateX) {
			NetworkClasses.PacketUpdateX packet = (NetworkClasses.PacketUpdateX) object;
			players.get(connection.getID()).x = packet.x;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
			// System.out.println("received and sent an update X packet");
		} else if(object instanceof NetworkClasses.PacketUpdateY) {
			NetworkClasses.PacketUpdateY packet = (NetworkClasses.PacketUpdateY) object;
			players.get(connection.getID()).y = packet.y;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
			// System.out.println("received and sent an update Y packet");
		}
		
		if(object instanceof NetworkClasses.PacketScore){
			NetworkClasses.PacketScore packet = (NetworkClasses.PacketScore) object;
			players.get(connection.getID()).score = packet.score;
			packet.id = connection.getID();
			MainServer.server.sendToAllExceptTCP(connection.getID(), packet);
		}
		if(object instanceof NetworkClasses.PacketScoreCubeUpdate){
			NetworkClasses.PacketScoreCubeUpdate packet = (NetworkClasses.PacketScoreCubeUpdate) object;
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		if(object instanceof NetworkClasses.PacketGameOver){
			NetworkClasses.PacketGameOver packet = (NetworkClasses.PacketGameOver) object;
			packet.gameFinished = true;
			packet.id = connection.getID();
			MainServer.server.sendToAllExceptTCP(connection.getID(), packet);
		}
	}

} // end total class
