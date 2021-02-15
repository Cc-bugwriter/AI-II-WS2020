package tud.ai2.pacman;

import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.states.GameLostState;
import tud.ai2.pacman.view.states.GamePlayState;
import tud.ai2.pacman.view.states.ListHighscoreState;
import tud.ai2.pacman.view.states.MenuState;

import static tud.ai2.pacman.util.Consts.*;

/**
 * Startet das Spiel.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public class Launch extends StateBasedGame {

	public Launch(String name) {
		super(name);
	}

	public static void main(String[] args) throws SlickException {
		// Standardpfade initialisieren
		setPaths();

		// Engine starten
		AppGameContainer app = new AppGameContainer(new Launch(GAME_NAME));

		// Konfiguration festlegen
		app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
		app.setShowFPS(false);
		app.setTargetFrameRate(60);
		app.start();
	}

	public static void setPaths() {
		if (System.getProperty(Consts.OS_NAME).toLowerCase().contains(Consts.WINDOWS_OS_NAME)) {
			System.setProperty(Consts.WINDOWS_LIB_PATH,
					System.getProperty(Consts.USER_DIR) + Consts.WINDOWS_USER_DIR);
		} else if (System.getProperty(Consts.OS_NAME).toLowerCase().contains(Consts.MAC_OS_NAME)) {
			System.setProperty(Consts.MAC_LIB_PATH,
					System.getProperty(Consts.USER_DIR) + Consts.MAC_USER_DIR);
		} else {
			System.setProperty(Consts.LINUX_LIB_PATH, System.getProperty(Consts.USER_DIR)
					+ Consts.LINUX_USER_DIR + System.getProperty(Consts.OS_NAME).toLowerCase());
		}
	}

	/**
	 * von StateBasedGame geerbt und einmal beim Start ausgefuehrt
	 */
	@Override
	public void initStatesList(GameContainer gameContainer) throws SlickException {
		// Zustaende dem FSM hinzufuegen
		// der erste Zustand ist der Startzustand
		super.addState(new MenuState(MENU_STATE));
		super.addState(new GamePlayState(GAME_STATE));
		super.addState(new GameLostState(LOST_STATE));
		super.addState(new ListHighscoreState(HIGHSCORE_STATE));

		// dem StateBasedEntityManager die Zustaende zuweisen
		StateBasedEntityManager.getInstance().addState(MENU_STATE);
		StateBasedEntityManager.getInstance().addState(GAME_STATE);
		StateBasedEntityManager.getInstance().addState(LOST_STATE);
		StateBasedEntityManager.getInstance().addState(HIGHSCORE_STATE);
	}
}