package Client;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlayerClient extends StateBasedGame {

	public static String userName;
	public static String password;

	public PlayerClient(String title) {
		super(title);

	}

	public static JLabel label_login;
	public static JTextField login;
	public static JLabel label_password;
	public static JPasswordField passwordField_password;

	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new MenuState());
		this.addState(new MultiPlayerState());
		this.addState(new SinglePlayerState());
		this.addState(new CreditsState());
	}

	public static void main(String args[]) throws SlickException {
		userName = getUserNameAndpasswordField_password();
		AppGameContainer app = new AppGameContainer(new PlayerClient("Cube Runner"));
		app.setAlwaysRender(true);
		app.setTargetFrameRate(60);
		app.setDisplayMode(800, 400, false);
		app.setShowFPS(false);
		app.setVSync(true);
		app.start();
	}

	private static String getUserNameAndpasswordField_password() {
		String s = "";
		label_login = new JLabel("Username:");
		login = new JTextField();

		label_password = new JLabel("Password:");
		passwordField_password = new JPasswordField();

		Object[] array = { label_login, login, label_password, passwordField_password };

		int res = JOptionPane.showConfirmDialog(null, array, "Login", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if(res == JOptionPane.OK_OPTION) {
			System.out.println("username: " + login.getText().trim());
			System.out.println("password: " + new String(passwordField_password.getPassword()));
			s = login.getText().trim();
			password = new String(passwordField_password.getPassword());
		} else {System.exit(1);}
		return s;
	}

} // end total class
