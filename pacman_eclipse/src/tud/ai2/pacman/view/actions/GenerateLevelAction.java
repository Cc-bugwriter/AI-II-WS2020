package tud.ai2.pacman.view.actions;

import eea.engine.action.Action;
import eea.engine.component.Component;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.model.level.Level;
import tud.ai2.pacman.model.level.LevelGenerator;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.states.GamePlayState;

/**
 * Initialisiert das zufaellige Generieren eines Levels.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class GenerateLevelAction implements Action {

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
        GamePlayState s = (GamePlayState) sb.getState(Consts.GAME_STATE);
        try {
            LevelGenerator gen = new LevelGenerator();
            s.startGame(new Level[]{gen.generateLevel()});
        } catch (Exception e) {
            e.printStackTrace();
            sb.enterState(Consts.MENU_STATE);
            return;
        }
        sb.enterState(Consts.GAME_STATE);
    }
}
