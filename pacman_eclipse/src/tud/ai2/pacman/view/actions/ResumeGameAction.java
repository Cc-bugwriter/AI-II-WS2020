package tud.ai2.pacman.view.actions;

import eea.engine.action.Action;
import eea.engine.component.Component;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.states.GamePlayState;

/**
 * Laedt das gespeicherte Spiel.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class ResumeGameAction implements Action {

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
        try {
            GamePlayState s = (GamePlayState) sb.getState(Consts.GAME_STATE);
            s.resumeGame(Consts.SAVE_FILE);
        } catch (Exception ex) {
            ex.printStackTrace();
            sb.enterState(Consts.MENU_STATE);
            return;
        }
        sb.enterState(Consts.GAME_STATE);
    }
}
