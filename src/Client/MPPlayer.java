package Client;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class MPPlayer {

	public float x = 256, y = 256;
	public int id;
	public String userName;
	public int score = 0;
	
	public int width = 16;
	public int height = 16;
	
	public void render(Graphics g, Color c) {
		g.setColor(c);
		if(userName != null)
		g.drawString(this.userName, x - width/2, y - height);
		// render player
		g.fillRect(x, y, width, height);
	} // end render
	
}
