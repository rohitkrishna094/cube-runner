package Client;

import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import NetworkClasses.LoginResponse;
import NetworkClasses.PacketAddPlayer;
import NetworkClasses.PacketGameOver;
import NetworkClasses.PacketRemovePlayer;
import NetworkClasses.PacketScore;
import NetworkClasses.PacketScoreCubeUpdate;
import NetworkClasses.PacketUpdateX;
import NetworkClasses.PacketUpdateY;
import NetworkClasses.PacketUserName;
import NetworkClasses.RandomNumber;

public class PlayerClientListener extends Listener {
	
	public void received(Connection connection, Object object){
		if(object instanceof LoginResponse){
			LoginResponse response = (LoginResponse) object;
			System.out.println(response.getResponseText());
			if(response.getResponseText().equalsIgnoreCase("ok")){
				Log.info("Login Ok");
			} else {
				Log.info("Login failed");
			}
		}
		
		if(object instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) object;
			MPPlayer newPlayer = new MPPlayer();
			MultiPlayerState.players.put(packet.id, newPlayer);
		} else if(object instanceof PacketRemovePlayer){
			PacketRemovePlayer packet = (PacketRemovePlayer) object;
			MultiPlayerState.players.remove(packet.id);
		} else if(object instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) object;
			MultiPlayerState.players.get(packet.id).x = packet.x;
		} else if(object instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) object;
			MultiPlayerState.players.get(packet.id).y = packet.y;
		} else if(object instanceof PacketUserName){
			PacketUserName packet = (PacketUserName) object;
			for(Map.Entry<Integer, MPPlayer> entry : MultiPlayerState.players.entrySet() ){
				if(entry.getKey() == packet.id){
					entry.getValue().userName = packet.userName;
				}
			}
		} 
		if(object instanceof RandomNumber) {
			RandomNumber packet = (RandomNumber)object;
			MultiPlayerState.rand = packet.randomFloat;
		}
		if(object instanceof PacketScore){
			PacketScore packet = (PacketScore) object;
			for(Map.Entry<Integer, MPPlayer> entry : MultiPlayerState.players.entrySet() ){
				if(entry.getKey() == packet.id){
					entry.getValue().score = packet.score;
				}
			}
		}
		if(object instanceof PacketScoreCubeUpdate){
			PacketScoreCubeUpdate packet = (PacketScoreCubeUpdate) object;
			MultiPlayerState.cube.setX(packet.x);
			MultiPlayerState.cube.setY(packet.y);
		}
		if(object instanceof PacketGameOver){
			PacketGameOver packet = (PacketGameOver) object;
			MultiPlayerState.gameOver = true;
			MultiPlayerState.whoWon = MultiPlayerState.players.get(packet.id).userName;
		}
	} // end received method
	
}
