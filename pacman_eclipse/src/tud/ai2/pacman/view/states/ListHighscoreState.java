package tud.ai2.pacman.view.states;

import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.model.Highscore;
import tud.ai2.pacman.model.HighscoreEntry;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.ButtonEntity;
import tud.ai2.pacman.view.Theme;

import java.util.List;

/**
 * Zeige alle Highscores an.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public class ListHighscoreState extends BasicGameState {
    /** ID des Zustands */
    private final int id;
    /** Enthaelt alle relevanten Entitaeten */
    private final StateBasedEntityManager entityManager;
    /** Font der einzelnen Eintraege */
    private UnicodeFont listFont;
    /** Font des Titels */
    private UnicodeFont titleFont;
    /** Momentan nicht genutzter Font */
    private UnicodeFont backFont;

    /** Die eigentlichen Highscores */
    private final Highscore hs;

    /**
     * Konstruktor.
     *
     * @param id ID des Zustands
     */
    public ListHighscoreState(int id) {
        this.id = id;
        this.hs = Highscore.getInstance();
        entityManager = StateBasedEntityManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
        // Lade die Fonts
        listFont = new UnicodeFont(Consts.FONT_FOLDER + "FreeMonoBold.ttf", 35, false, false);
        listFont.addAsciiGlyphs();
        listFont.getEffects().add(new ColorEffect());
        listFont.loadGlyphs();

        titleFont = new UnicodeFont(Consts.FONT_FOLDER + "LOT.otf", 50, false, false);
        titleFont.addAsciiGlyphs();
        titleFont.getEffects().add(new ColorEffect());
        titleFont.loadGlyphs();

        backFont = new UnicodeFont(Consts.FONT_FOLDER + "LOT.otf", 35, false, false);
        backFont.addAsciiGlyphs();
        backFont.getEffects().add(new ColorEffect());
        backFont.loadGlyphs();

        // hintergrund
        Entity background = new Entity("highscore_background");
        background.setPosition(new Vector2f(350, 220));
        background.addComponent(new ImageRenderComponent(Theme.currentTheme.HIGHSCORE_BACKGROUND));
        entityManager.addEntity(getID(), background);

        // zurueck zum menu
        ButtonEntity buttonHighscore = new ButtonEntity("back to menu", new ChangeStateAction(Consts.MENU_STATE), backFont);
        buttonHighscore.setPosition(new Vector2f(350, gc.getHeight() - 50));
        entityManager.addEntity(getID(), buttonHighscore);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        entityManager.renderEntities(gc, sbg, g);
        g.setFont(titleFont);
        g.drawString("Hall of Fame", 150, 10);
        printEntries(g);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (sbg.getCurrentStateID() == Consts.MENU_STATE)
            if (gc instanceof AppGameContainer)
                ((AppGameContainer) gc).setDisplayMode(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT, false);

        entityManager.updateEntities(gc, sbg, delta);
    }

    /**
     * Zeigt alle Eintraege entsprechend an.
     */
    private void printEntries(Graphics g) {
        List<HighscoreEntry> entries = hs.getAllEntries();
        g.setFont(listFont);
        int offset = 80;
        String points;
        for (int i = 0; i < Math.min(entries.size(), Consts.HIGHSCORE_DISPLAYED_ENTRIES); i++) {
            HighscoreEntry he = entries.get(i);

            points = String.format("%09d", he.getPoints());
            listFont.drawString(170, offset, he.getName());
            listFont.drawString(310, offset, points);

            offset += 50;
        }
    }

    public int getID() {
        return id;
    }

}
