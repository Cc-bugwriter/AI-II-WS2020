package tud.ai2.pacman.view.states;

import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.model.Highscore;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.ButtonEntity;
import tud.ai2.pacman.view.actions.SaveHighscoreAction;

import java.util.Arrays;

/**
 * Nachdem das Spiel verloren wurde.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public class GameLostState extends BasicGameState {
    /** ID des Zustands */
    private final int stateID;
    /** Erspielte Punktzahl */
    private int points;
    /** Ob der Eintrag abgespeichert wird */
    private boolean newEntry;

    /** aktuell bearbeitetes Zeichen */
    private int index = 0;
    /** Der eingegebene Name */
    private char[] nickname;

    /** Enthaelt alle relevanten Entitaeten */
    private final StateBasedEntityManager entityManager;
    /** Font des Titels */
    private UnicodeFont titleFont;
    /** Font des Eintrags */
    private UnicodeFont listFont;

    /** Speicher Button */
    private ButtonEntity buttonSave;

    /**
     * Konstruktor.
     *
     * @param id ID des Zustands
     */
    public GameLostState(int id) {
        stateID = id;
        entityManager = StateBasedEntityManager.getInstance();
    }

    /**
     * @param points neue Punktzahl
     */
    public void setPoints(int points) {
        this.points = points;
        Highscore h = Highscore.getInstance();
        newEntry = h.checkNewEntry(points);

        if (newEntry) buttonSave.enable();
        else buttonSave.disable();
    }

    /**
     * Nimmt einen Tastendruck entgegen.
     * Laesst Initialien eingeben.
     *
     * @param key gedrueckte Taste
     * @param c Zeichen hinter der Taste
     */
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_BACK) {
            nickname[Math.max(0, index)] = 'A';

            if (index > 0) index--;
        } else if (Character.isAlphabetic(c) || Character.isDigit(c)) {
            c = Character.toUpperCase(c);
            nickname[Math.min(nickname.length - 1, index)] = c;

            if (index < nickname.length - 1) index++;
        }
    }

    @SuppressWarnings("unchecked")
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        //Schriftarten initialisieren
        titleFont = new UnicodeFont(Consts.FONT_FOLDER + "LOT.otf", 50, false, false);
        titleFont.addAsciiGlyphs();
        titleFont.getEffects().add(new ColorEffect());
        titleFont.loadGlyphs();

        listFont = new UnicodeFont(Consts.FONT_FOLDER + "Exo-Bold.ttf", 30, false, false);
        listFont.addAsciiGlyphs();
        listFont.getEffects().add(new ColorEffect());
        listFont.loadGlyphs();

        // Speichern-Button setzen
        buttonSave = new ButtonEntity("Save", (gameContainer, stateBasedGame, i, component) -> {
            new SaveHighscoreAction(points, new String(nickname)).update(gameContainer, stateBasedGame, i, component);
        }, titleFont, new Integer[]{Input.KEY_RETURN});
        buttonSave.setPosition(new Vector2f(gc.getWidth() / 2f, 350));
        entityManager.addEntity(getID(), buttonSave);

        // Initialien-array initialisieren
        index = 0;
        nickname = new char[Consts.HS_MAX_NAME_LENGTH];
        Arrays.fill(nickname, 'A');

        // Menue-Button setzen
        ButtonEntity buttonMenu = new ButtonEntity("Back to menu", new ChangeStateAction(Consts.MENU_STATE), titleFont);
        buttonMenu.setPosition(new Vector2f(gc.getWidth() / 2f, 400));
        entityManager.addEntity(getID(), buttonMenu);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GamePlayState s = (GamePlayState) sbg.getState(Consts.GAME_STATE);
        points = s.getPoints();

        if (newEntry) {
            int labelOffset = 100; // Abstand der Eingaben zur oberen Kante

            // eingabe des names und Darstellung der Punktzahl
            titleFont.drawString(gc.getWidth() / 2f - titleFont.getWidth("New Highscore!") / 2f, 10, "New Highscore!");
            listFont.drawString(200, labelOffset, "Your name: ");
            drawName(listFont, 250, labelOffset + 50);
            listFont.drawString(200, labelOffset + 100, "Your score: ");
            listFont.drawString(250, labelOffset + 150, String.format("%09d", points), Color.gray);
        } else {
            //kein neuer High Score
            titleFont.drawString(gc.getWidth() / 2f - titleFont.getWidth("No new Highscore!") / 2f, 10, "No new Highscore!");
        }

        entityManager.renderEntities(gc, sbg, g);
    }

    /**
     * Zeige die Initialen an.
     *
     * @param font zu verwendender Font
     * @param x X-Position
     * @param y Y-Position
     */
    private void drawName(Font font, int x, int y) {
        for (int i = 0; i < nickname.length; i++) {
            Color c = i == index ? Color.white : Color.gray;
            font.drawString(x + i * font.getWidth("W "), y, new String(new char[]{nickname[i]}), c);
        }
    }


    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (sbg.getCurrentStateID() == Consts.LOST_STATE)
            if (gc instanceof AppGameContainer)
                ((AppGameContainer) gc).setDisplayMode(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT, false);

        entityManager.updateEntities(gc, sbg, delta);
    }

    public int getID() {
        return stateID;
    }

}
