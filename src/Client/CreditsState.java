package Client;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.SelectTransition;

public class CreditsState extends BasicGameState {

	public static Font creditsfont = new Font("Century Gothic", Font.PLAIN, 14);
	public static TrueTypeFont creditsGothic = new TrueTypeFont(creditsfont, true);
	
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		
	}

	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		if(container.getInput().isKeyDown(Input.KEY_ESCAPE)){
			sbg.enterState(0, new FadeInTransition(), new SelectTransition());
		}
	}

	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setFont(creditsGothic);
		g.setColor(Color.cyan);
		g.drawRect(200, 100, 400, 200);
		g.setColor(new Color(156,156,156,50));
		g.fillRect(200, 100, 400, 200);
		g.setColor(Color.white);
		g.drawString("Developers", container.getWidth()/2 - 180 , 120);
		g.drawString("------------------", container.getWidth()/2 - 180 , 135);
		g.drawString("Rohit Krishna Marneni", container.getWidth()/2 - 180 , 150);
		g.drawString("Kailash", container.getWidth()/2 - 180 , 170);
	}


	public int getID() {
		return 3;
	}

}
