package Client;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SinglePlayer {

	public float x;
	public float y;

	public int width;
	public int height;
	public int speed;
	public int time = 0;
	public Random rand = new Random();
	public int score = 0;

	public String userName;

	public SinglePlayer(String userName) {
		x = rand.nextFloat() * 800;
		y = rand.nextFloat() * 400;
		width = 16;
		height = 16;
		speed = 100;
		this.userName = userName;
	}

	public void update(int delta, GameContainer container, boolean ai) {
		delta = delta * 5;
		float tempX = x;
		float tempY = y;

		if(ai) {
			if(this.x < SinglePlayerState.cube.getX() && this.y >= SinglePlayerState.cube.getY()) {
				this.x += this.speed * delta / 1000f;
				this.y -= this.speed * delta / 1000f;
				// System.out.println("First");
			}
			if(this.x >= SinglePlayerState.cube.getX() && this.y > SinglePlayerState.cube.getY()) {
				this.x -= this.speed * delta / 1000f;
				this.y -= this.speed * delta / 1000f;
				// System.out.println("Second");
			}
			if(this.x > SinglePlayerState.cube.getX() && this.y <= SinglePlayerState.cube.getY()) {
				this.x -= this.speed * delta / 1000f;
				this.y += this.speed * delta / 1000f;
				// System.out.println("third");
			}
			if(this.x <= SinglePlayerState.cube.getX() && this.y < SinglePlayerState.cube.getY()) {
				// System.out.println("fourth");
				this.x += this.speed * delta / 1000f;
				this.y +=this.speed * delta / 1000f;
			}

		} else {
			if((MenuState.inputHandler.isKeyDown(Input.KEY_W))) {
				y -= speed * delta / 1000f;
			}
			if((MenuState.inputHandler.isKeyDown(Input.KEY_S))) {
				y += speed * delta / 1000f;
			}
			if((MenuState.inputHandler.isKeyDown(Input.KEY_A))) {
				x -= speed * delta / 1000f;
			}
			if((MenuState.inputHandler.isKeyDown(Input.KEY_D))) {
				x += speed * delta / 1000f;
			}
		}

		if(!ai) {
			for (int i = 0; i < SinglePlayerState.gameBots.size(); i++) {
				if(SinglePlayerState.isCollision(this, SinglePlayerState.gameBots.get(i))) {
					x = tempX;
					y = tempY;
				}
			} // end for
		} else {
			if(SinglePlayerState.isCollision(this, SinglePlayerState.player)) {
				x = tempX;
				y = tempY;
			}
		}

		if(y < 0) y = 0;
		if(x < 0) x = 0;
		if(y + height > container.getHeight()) y = container.getHeight() - height;
		if(x + width > container.getWidth()) x = container.getWidth() - width;
	} // end update

	public void render(Graphics g, Color c) {
		g.setColor(c);
		if(y > 3) g.drawString(this.userName, x - this.userName.length() * 4, y - height);
		else if(y <= 3) g.drawString(this.userName, x - this.userName.length() * 4, y + height);
		// render player
		g.fillRect(x, y, width, height);
	} // end render
}
