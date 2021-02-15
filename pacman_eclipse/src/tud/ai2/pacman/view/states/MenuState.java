package tud.ai2.pacman.view.states;

import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.model.level.Level;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.ButtonEntity;
import tud.ai2.pacman.view.Theme;
import tud.ai2.pacman.view.actions.GenerateLevelAction;
import tud.ai2.pacman.view.actions.ResumeGameAction;
import tud.ai2.pacman.view.actions.StartGameAction;

import java.io.File;
import java.io.IOException;

/**
 * Das Hauptmenue.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public class MenuState extends BasicGameState {
    private int creditCount = 0;

    /** ID des Zustands */
    private final int stateID;
    /** Abstand zwischen den Knoepfen */
    private static final int buttonMargin = 16;
    /** Enthaelt alle relevanten Entitaeten */
    private final StateBasedEntityManager entityManager;
    /** Der "Continue" Button */
    private ButtonEntity buttonContinue;

    /**
     * Konstruktor.
     *
     * @param id ID des Zustands
     */
    public MenuState(int id) {
        stateID = id;
        entityManager = StateBasedEntityManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
        // standardtexturen laden
        try {
            Theme.setCurrentTheme(Consts.CURRENT_THEME);
        } catch (Exception e) {
            e.printStackTrace();
            gc.exit();
        }

        int width = gc.getWidth(), height = gc.getHeight();

        // hintergrund
        Entity background = new Entity("menu");
        background.setPosition(new Vector2f(width / 2f, height / 2f));
        background.addComponent(new ImageRenderComponent(Theme.currentTheme.MENU_BACKGROUND));
        background.setScale(height / background.getSize().y); // der BG soll in die Hoehe des Containers ausfuellen
        entityManager.addEntity(getID(), background);

        UnicodeFont buttonFont = new UnicodeFont(Consts.FONT_FOLDER + "LOT.otf", 28, false, false);
        buttonFont.addAsciiGlyphs();
        buttonFont.getEffects().add(new ColorEffect());
        buttonFont.loadGlyphs();

        buttonContinue = new ButtonEntity("resume game", new ResumeGameAction(), buttonFont);
        buttonContinue.setPosition(new Vector2f(width / 1.05f - buttonFont.getWidth("resume game")/2f, height - 5 * buttonFont.getLineHeight() - 4 * buttonMargin));
        entityManager.addEntity(getID(), buttonContinue);

        // verfuegbare level auflisten
        String[] levels = Level.listLevelFiles();

        ButtonEntity startNewGame = new ButtonEntity("start new game", (gc1, sb1, delta, event) -> {
            new OptionFrame(levels).waitForStart();
            int id = OptionFrame.id;
            if (id >= 0)
                new StartGameAction(levels[id]).update(gc1, sb1, delta, event);
            else if (id == -2)
                new StartGameAction(levels).update(gc1, sb1, delta, event);
            else if (id == -3)
                new GenerateLevelAction().update(gc1, sb1, delta, event);
        }, buttonFont);
        startNewGame.setPosition(new Vector2f(width / 1.05f - buttonFont.getWidth("start new game")/2f, height - 4 * buttonFont.getLineHeight() - 3 * buttonMargin));
        entityManager.addEntity(getID(), startNewGame);

        ButtonEntity buttonHighscore = new ButtonEntity("highscore", new ChangeStateAction(Consts.HIGHSCORE_STATE), buttonFont);
        buttonHighscore.setPosition(new Vector2f(width / 1.05f - buttonFont.getWidth("highscore")/2f, height - 3 * buttonFont.getLineHeight() - 2 * buttonMargin));
        entityManager.addEntity(getID(), buttonHighscore);

        ButtonEntity buttonExit = new ButtonEntity("exit", new QuitAction(), buttonFont);
        buttonExit.setPosition(new Vector2f(width * 0.1f, height - buttonFont.getLineHeight()));//buttonFont.getLineHeight() / 2f + height / 2f + buttonFont.getLineHeight() + 2 * buttonMargin + (levels.length + 3) * (buttonFont.getLineHeight() + buttonMargin) + buttonMargin));
        entityManager.addEntity(getID(), buttonExit);


        ButtonEntity buttonCredits = new ButtonEntity("credits", (gameContainer, stateBasedGame, i, component) -> {
            String creditString = "";
            creditString += " -------------------------------- " + System.lineSeparator();
            creditString += " |    Credits Pacman 2020/21    | " + System.lineSeparator();
            creditString += " |                              | " + System.lineSeparator();
            creditString += " | Urspruengliche Entwickler:   | " + System.lineSeparator();
            creditString += " |  - Simon Breitfelder         | " + System.lineSeparator();
            creditString += " |  - Dominik Puellen           | " + System.lineSeparator();
            creditString += " |                              | " + System.lineSeparator();
            creditString += " | Entwickler:                  | " + System.lineSeparator();
            creditString += " |  - Robert Cieslinski         | " + System.lineSeparator();
            creditString += " |  - Kurt Cieslinski           | " + System.lineSeparator();
            creditString += " |                              | " + System.lineSeparator();
            creditString += " | Neue Grafiken:               | " + System.lineSeparator();
            creditString += " |  - Sophie Cieslinski         | " + System.lineSeparator();
            creditString += " |                              | " + System.lineSeparator();
            creditString += " -------------------------------- " + System.lineSeparator();

            creditCount++;
            if(creditCount == 3){
                creditString += "Covid Mode Active!" + System.lineSeparator();
                try {
                    Theme.currentTheme = new Theme("covid");
                } catch (SlickException | IOException ignored) {
                }
            }
            if(creditCount > 3){
                creditCount = 0;
                creditString += "Normal Mode Active!" + System.lineSeparator();
                try {
                    Theme.currentTheme = new Theme(Consts.CURRENT_THEME);
                } catch (SlickException | IOException ignored) {
                }
            }
            System.err.print(creditString);
        }, buttonFont);
        buttonCredits.setPosition(new Vector2f(width * 0.2f + buttonFont.getWidth("exit"), height - buttonFont.getLineHeight()));//buttonFont.getLineHeight() / 2f + height / 2f + buttonFont.getLineHeight() + 2 * buttonMargin + (levels.length + 3) * (buttonFont.getLineHeight() + buttonMargin) + buttonMargin));
        entityManager.addEntity(getID(), buttonCredits);

        updateButtons();
    }

    /**
     * Passt die klickbarkeit des "Continue" Buttons an
     */
    public void updateButtons() {
        if (new File(Consts.SAVE_FILE).isFile()) buttonContinue.enable();
        else buttonContinue.disable();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        /*float wC = container.getWidth();
        float hC = container.getHeight();
        float wI = Theme.currentTheme.MENU_BACKGROUND.getWidth();
        float hI = Theme.currentTheme.MENU_BACKGROUND.getHeight();
        float w, h;
        if ((wC / hC) > (wI / hI)) {
            w = wI;
            h = (wI * hC) / wC;
        } else {
            w = (wC * hI) / hC;
            h = hI;
        }
        g.drawImage(Theme.currentTheme.MENU_BACKGROUND, 0, 0, wC, hC, (w - wI), 0, w, h);*/

        entityManager.renderEntities(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (game.getCurrentStateID() == Consts.MENU_STATE)
            if (container instanceof AppGameContainer)
                ((AppGameContainer) container).setDisplayMode(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT, false);

        entityManager.updateEntities(container, game, delta);
    }

    public int getID() {
        return stateID;
    }

}
