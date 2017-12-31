package Client;

import java.awt.Font;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class MenuState extends BasicGameState {

	public static final int ID_MENUSTATE = 0;
	public static final int ID_SINGLEPLAYERSTATE = 1;
	public static final int ID_MULTIPLAYERSTATE = 2;
	public static final int ID_CREDITS = 3;

	public static JLabel label_ipAddress;
	public static JTextField textField_ipAddress;
	public static JLabel label_port;
	public static JTextField textField_port;
	public static JLabel label_botCount;
	public static JTextField textField_botCount;

	public static String ipAddress;
	public static int port;
	public static int botCount = 1;

	public int welcomeTimer = 1000 * 100;
	public static Color fontColorChanger = new Color(Color.white);
	public static Font font = new Font("Century Gothic", Font.PLAIN, 18);
	public static TrueTypeFont gothic = new TrueTypeFont(font, true);
	public float fontX = 0;
	public float fontY = 0;
	public int dir = 1;
	int posX;
	int posY;
	int time = 0;

	public Random rand = new Random();

	public static Input inputHandler;

	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		inputHandler = container.getInput();
	}

	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		posX = Mouse.getX();
		posY = Mouse.getY();
		// System.out.println("posx = " + posX +"," + "posy = " + posY);
		welcomeTimer -= delta;
		fontX += dir * 0.4;
		if(fontX >= container.getWidth() - 120) dir = -1;
		if(fontX <= 0) dir = 1;
		if((posX > 661 && posX < 766) && (posY > 240 && posY < 259)) {
			if(Mouse.isButtonDown(0)) {
				SinglePlayerState.backToMenu = false;
				label_botCount = new JLabel("Number of botCount?");
				textField_botCount = new JTextField();

				Object[] array = { label_botCount, textField_botCount };

				try {
					int res = JOptionPane.showConfirmDialog(null, array, "Number of botCount?", JOptionPane.OK_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					botCount = Integer.parseInt(textField_botCount.getText().trim());
				} catch (NumberFormatException e) {
					botCount = 1;
				}

				sbg.enterState(ID_SINGLEPLAYERSTATE, new FadeOutTransition(), new FadeInTransition());
			}
		}
		if((posX > 661 && posX < 766) && (posY > 208 && posY < 228)) {
			if(Mouse.isButtonDown(0)) {
				label_ipAddress = new JLabel("IP Address:");
				textField_ipAddress = new JTextField();

				label_port = new JLabel("Port:");
				textField_port = new JTextField();

				Object[] array = { label_ipAddress, textField_ipAddress, label_port, textField_port };

				int res = JOptionPane.showConfirmDialog(null, array, "IpAddress:", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if(res == JOptionPane.OK_OPTION) {
					// System.out.println("username: " +
					// login.getText().trim());
					// System.out.println("password: " + new
					// String(password.getPassword()));
					ipAddress = textField_ipAddress.getText().trim();
					port = Integer.parseInt(textField_port.getText().trim());
					sbg.enterState(ID_MULTIPLAYERSTATE, new FadeOutTransition(), new FadeInTransition());
				} else {
				}
			}
		}
		if((posX > 661 && posX < 766) && (posY > 178 && posY < 200)) {
			if(Mouse.isButtonDown(0)) {
				sbg.enterState(ID_CREDITS, new FadeOutTransition(), new FadeInTransition());
			}
		}
		if((posX > 661 && posX < 766) && (posY > 149 && posY < 170)) {
			if(Mouse.isButtonDown(0)) {
				System.exit(0);
			}
		}
	}

	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.setFont(gothic);
		if(welcomeTimer > 0) {
			fontColorChanger.darker();
			g.drawString("Hello " + PlayerClient.userName, 10 + (fontX), 370);
		}
		g.drawString("SinglePlayer", 660, 140);
		g.drawString("MultiPlayer", 668, 170);
		g.drawString("Credits", 701, 200);
		g.drawString("Exit", 731, 230);

		if((posX > 661 && posX < 766) && (posY > 240 && posY < 259)) {
			g.setColor(new Color(0, 255, 0, 150));
			g.fillRect(650, 140, 120, 25);
		}
		if((posX > 661 && posX < 766) && (posY > 208 && posY < 228)) {
			g.setColor(new Color(0, 255, 0, 150));
			g.fillRect(650, 170, 120, 25);
		}
		if((posX > 661 && posX < 766) && (posY > 178 && posY < 200)) {
			g.setColor(new Color(0, 255, 0, 150));
			g.fillRect(650, 200, 120, 25);
		}
		if((posX > 661 && posX < 766) && (posY > 149 && posY < 169)) {
			g.setColor(new Color(0, 255, 0, 150));
			g.fillRect(650, 230, 120, 25);
		}

		g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		if(time == 3) {
			g.fillRect(rand.nextInt(800), rand.nextInt(400), rand.nextInt(60), rand.nextInt(60));
			time = 0;
		}
		time++;
	}

	public int getID() {
		return 0;
	}

}
