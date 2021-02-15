package tud.ai2.pacman.view.actions;

import eea.engine.action.Action;
import eea.engine.component.Component;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.model.level.Level;
import tud.ai2.pacman.model.level.LevelParser;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.states.GamePlayState;

/**
 * Startet das Spiel anhand der uebergebenen Levelnamen.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class StartGameAction implements Action {

    private final String[] levelFiles;

    public StartGameAction(String levelFile) {
        levelFiles = new String[]{levelFile};
    }

    public StartGameAction(String[] levelFiles) {
        this.levelFiles = levelFiles;
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
        GamePlayState s = (GamePlayState) sb.getState(Consts.GAME_STATE);
        try {
            s.startGame(loadLevels(levelFiles));
        } catch (Exception e) {
            e.printStackTrace();
            sb.enterState(Consts.MENU_STATE);
            return;
        }
        sb.enterState(Consts.GAME_STATE);
    }

    private Level[] loadLevels(String[] levelFiles) throws Exception {
        Level[] levels = new Level[levelFiles.length];
        for (int i = 0; i < levels.length; i++)
            levels[i] = LevelParser.fromFile(Consts.LEVEL_FOLDER + levelFiles[i]);

        return levels;
    }
}
