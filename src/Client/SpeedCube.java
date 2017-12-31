package Client;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class SpeedCube {
	public float x;
	public float y;

	public int id;

	public SpeedCube() {
		x = 250;
		y = 250;
	}

	public void render(Graphics g, Color c) {
		g.setColor(c);
		// render player
		g.fillRect(x, y, 4, 4);
	} // end render
}
