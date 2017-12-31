package Client;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.FontUtils;

import com.esotericsoftware.kryonet.Connection;

public class PlayerChar {

	public Shape hitbox;
	public float x;
	public float y;

	public float netX;
	public float netY;

	public int width;
	public int height;
	public int score = 0;

	public String userName;

	public Connection c;

	public PlayerChar() {
		Random rand = new Random();
		x = rand.nextInt(800);
		y = rand.nextInt(400);
		width = 16;
		height = 16;
	}

	public void update(int delta, GameContainer container) {
		float tempX = x;
		float tempY = y;

		delta = delta * 5;
		if((MenuState.inputHandler.isKeyDown(Input.KEY_W))) {
			y -= 100 * delta / 1000f;
		}
		if((MenuState.inputHandler.isKeyDown(Input.KEY_S))) {
			y += 100 * delta / 1000f;
		}
		if((MenuState.inputHandler.isKeyDown(Input.KEY_A))) {
			x -= 100 * delta / 1000f;
		}
		if((MenuState.inputHandler.isKeyDown(Input.KEY_D))) {
			x += 100 * delta / 1000f;
		}
		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(y + height > container.getHeight()) y = container.getHeight() - height;
		if(x + width > container.getWidth()) x = container.getWidth() - width;
		for (MPPlayer mp : MultiPlayerState.players.values()) {
			if(MultiPlayerState.isCollision(this, mp)) {
				this.x = tempX;
				this.y = tempY;
			}
		}
	} // end update

	public void render(Graphics g, Color c) {
		g.setColor(c);
		if(y > 3 && userName != null) g.drawString(this.userName, x - this.userName.length() * 4, y - height);
		else if(y <= 3 && userName != null) g.drawString(this.userName, x - this.userName.length() * 4, y + height);
		// render player
		g.fillRect(x, y, width, height);
	} // end render

}
