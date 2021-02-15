package tud.ai2.pacman.view.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.model.entity.GameEntity;
import tud.ai2.pacman.model.entity.Ghost;
import tud.ai2.pacman.model.entity.Pacman;
import tud.ai2.pacman.model.entity.pickup.Dot;
import tud.ai2.pacman.model.entity.pickup.PowerUp;
import tud.ai2.pacman.model.entity.pickup.SpeedUp;
import tud.ai2.pacman.model.entity.pickup.Teleporter;
import tud.ai2.pacman.model.level.Level;
import tud.ai2.pacman.model.level.LevelParser;
import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.view.Theme;

import java.io.*;

/**
 * Eigentlicher Spielzustand.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public class GamePlayState extends BasicGameState {
    /** ID des Zustands */
    private final int stateID;
    /** Alle fuer das Spiel relevanten Objekte */
    private PacmanGame game;
    /** Das aktuelle Thema */
    private Theme theme;
    /** Die Wnad-Asset-ID */
    private int[][] neededTexture;

    /** Die gespielten Level */
    private Level[] levelList;
    /** Aktuelle Level-ID */
    private int currentLevelIndex;

    /** Das Spielfenster */
    private GameContainer gc;

    /**
     * Konstruktor.
     *
     * @param id ID des Zustands
     */
    public GamePlayState(int id) {
        stateID = id;
    }

    /**
     * @return erspielte Punktzahl
     */
    public int getPoints() {
        return game.getPoints();
    }

    /**
     * Starte das Spiel.
     *
     * @param levels zu spielende Level
     */
    public void startGame(Level[] levels) {
        this.game = new PacmanGame();
        levelList = levels;
        currentLevelIndex = 0;
        changeLevel(levels[currentLevelIndex]);
        theme = Theme.currentTheme;
    }

    /**
     * Lade den Spielstand aus der Datei.
     *
     * @param path Dateipfad
     */
    public void resumeGame(String path) throws Exception {
        DataInputStream s;
        s = new DataInputStream(new FileInputStream(path));
        levelList = new Level[s.readInt()];
        for (int i = 0; i < levelList.length; i++) {
            levelList[i] = LevelParser.fromString(s.readUTF());
            levelList[i].setName(s.readUTF());
        }
        currentLevelIndex = s.readInt();
        this.game = new PacmanGame(s);
        s.close();
        theme = Theme.currentTheme;
        initLevelVisualisation();
    }

    /**
     * Aendere den aktuellen Level.
     *
     * @param level neuer Level
     */
    public void changeLevel(Level level) {
        game.changeLevel(level);
        initLevelVisualisation();
    }

    /**
     * Bereitet die Visualisierung vor.
     */
    private void initLevelVisualisation() {
        Level level = game.getLevel();

        try {
            int newWidth = Math.max(Consts.WINDOW_WIDTH, level.getWidth() * theme.TILE_SIZE);
            int newHeight = Consts.INFO_BAR_HEIGHT + level.getHeight() * theme.TILE_SIZE;
            if (gc instanceof AppGameContainer)
                ((AppGameContainer) gc).setDisplayMode(newWidth, newHeight, false);
        } catch (SlickException e) {
            e.printStackTrace();
        }

        // Texturverbindungsmatrix vorbereiten
        computeTextures(level);
    }

    // TODO task 5a
    private void computeTextures(Level level) {
    	int x = level.getWidth();
    	int y = level.getHeight();
    	neededTexture = new int[y][x];

    	for (int i = 0; i < y; i++) {
    		for (int j = 0; j < x; j++) {
    			if (level.isWall(j, i)) {
    				boolean up = level.isWall(j, i - 1);
    				boolean down = level.isWall(j, i + 1);
    				boolean left = level.isWall(j - 1, i);
    				boolean right = level.isWall(j + 1, i);
    				if (!left && !down && !right && !up) neededTexture[i][j] = 0b0000; 	// 0
    				if (!left && !down && !right && up) neededTexture[i][j] = 0b0001; 	// 1
    				if (!left && !down && right && !up) neededTexture[i][j] = 0b0010;	// 2
    				if (!left && !down && right && up) neededTexture[i][j] = 0b0011;	// 3
    				if (!left && down && !right && !up) neededTexture[i][j] = 0b0100;	// 4
    				if (!left && down && !right && up) neededTexture[i][j] = 0b0101;	// 5
    				if (!left && down && right && !up) neededTexture[i][j] = 0b0110;	// 6
    				if (!left && down && right && up) neededTexture[i][j] = 0b0111;		// 7
    				if (left && !down && !right && !up) neededTexture[i][j] = 0b1000;	// 8
    				if (left && !down && !right && up) neededTexture[i][j] = 0b1001;	// 9
    				if (left && !down && right && !up) neededTexture[i][j] = 0b1010;	// 10
    				if (left && !down && right && up) neededTexture[i][j] = 0b1011;		// 11
    				if (left && down && !right && !up) neededTexture[i][j] = 0b1100;	// 12
    				if (left && down && !right && up) neededTexture[i][j] = 0b1101;		// 13
    				if (left && down && right && !up) neededTexture[i][j] = 0b1110;		// 14
    				if (left && down && right && up) neededTexture[i][j] = 0b1111;		// 15
    			} else {
    				neededTexture[i][j] = 0;
    			}
    		}
    	}
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (game != null) {
            game.updateFrame();
            handleInput(gc.getInput(), gc, sbg);

            if (game.isWon()) {
                // naechstes Level in der liste starten
                currentLevelIndex = (currentLevelIndex + 1) % levelList.length;
                changeLevel(levelList[currentLevelIndex]);
            } else if (game.isLost()) {
                // spielstand loeschen, falls vorhanden
                if (new File(Consts.SAVE_FILE).isFile())
                    new File(Consts.SAVE_FILE).delete();

                // menustate ueber moegliche Aenderungen der Verfuegbarkeit eines Spielstands informieren
                MenuState menu = (MenuState) sbg.getState(Consts.MENU_STATE);
                menu.updateButtons();
                // loststate ueber neuen punktestand informieren und anzeigen
                GameLostState s = (GameLostState) sbg.getState(Consts.LOST_STATE);
                s.setPoints(game.getPoints());
                sbg.enterState(Consts.LOST_STATE);
            }
        }
    }

    /**
     * Laesst Pacman steuern bzw. das Spiel unterbrechen.
     */
    private void handleInput(Input input, GameContainer gc, StateBasedGame sbg) {
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            DataOutputStream s;
            try {
                // Spielstand speichern
                s = new DataOutputStream(new FileOutputStream(Consts.SAVE_FILE));
                s.writeInt(levelList.length);
                for (Level level : levelList) {
                    s.writeUTF(level.toString());
                    s.writeUTF(level.getName());
                }
                s.writeInt(currentLevelIndex);
                game.saveGame(s);
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // zum Menue wechseln
            MenuState menu = (MenuState) sbg.getState(Consts.MENU_STATE);
            menu.updateButtons();
            sbg.enterState(Consts.MENU_STATE);
        } else if (input.isKeyDown(Input.KEY_UP))
            game.movePacman(0, -1);
        else if (input.isKeyDown(Input.KEY_DOWN))
            game.movePacman(0, 1);
        else if (input.isKeyDown(Input.KEY_LEFT))
            game.movePacman(-1, 0);
        else if (input.isKeyDown(Input.KEY_RIGHT))
            game.movePacman(1, 0);
    }

    /**
     * Wird mit dem Frame ausgefuehrt
     */
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if (game != null) {
            // map zuerst zeichnen, damit sie im Hintergrund ist
            drawMap(g);
            drawInfobar(g);
            drawEntites(g);
        }
    }

    /**
     * zeichne den statischen Levelhintergrund
     */
    private void drawMap(Graphics g) {
        Image img;
        Level l = game.getLevel();
        int w = l.getWidth();
        int h = l.getHeight();
        int index;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                index = (neededTexture != null) ? neededTexture[y][x] : 0;

                if (l.isWall(x, y) && 0 <= index) img = theme.WALLS[index];
                else img = theme.BACKGROUND;
                
                g.drawImage(img, x * theme.TILE_SIZE, y * theme.TILE_SIZE + Consts.INFO_BAR_HEIGHT);
            }
        }
    }

    /**
     * Zeichne alle Entitaeten (Pacman, Geister, Module)
     */
    private void drawEntites(Graphics g) {
        Pacman pacman = game.getPacman();

        for (GameEntity entity : game.getEntities()) {
            if (entity instanceof Dot)
                g.drawImage(theme.DOT, entity.getPos().x * theme.TILE_SIZE, entity.getPos().y * theme.TILE_SIZE + Consts.INFO_BAR_HEIGHT);
            if (entity instanceof PowerUp)
                g.drawImage(theme.POWER_UP, entity.getPos().x * theme.TILE_SIZE, entity.getPos().y * theme.TILE_SIZE + Consts.INFO_BAR_HEIGHT);
            if (entity instanceof SpeedUp)
                g.drawImage(theme.SPEED_UP, entity.getPos().x * theme.TILE_SIZE, entity.getPos().y * theme.TILE_SIZE + Consts.INFO_BAR_HEIGHT);
            if (entity instanceof Teleporter)
                g.drawImage(theme.TELEPORTER, entity.getPos().x * theme.TILE_SIZE, entity.getPos().y * theme.TILE_SIZE + Consts.INFO_BAR_HEIGHT);
            if (entity instanceof Ghost) {
                // die textur ist von der nummer, blickrichtung und "fressbarkeit" abhaengig
                Ghost ghost = (Ghost) entity;
                int index = pacman.isPoweredUp() && !ghost.isIdle()  ? theme.GHOST.length - 1 : ghost.getNumber() * Consts.NUM_GHOSTS + ghost.getDir();
                g.drawImage(theme.GHOST[index], entity.getPos().x * theme.TILE_SIZE, entity.getPos().y * theme.TILE_SIZE + Consts.INFO_BAR_HEIGHT);
            }
        }

        // faerbung der spielfigur je nach aktivierten items
        Color blend = Consts.P_DEFAULT_BLEND_COLOR;
        if (pacman.isPoweredUp())
            blend = Consts.P_POWER_UP_BLEND_COLOR;
        else if (pacman.hasSpeedUp())
            blend = Consts.P_SPEED_UP_BLEND_COLOR;
        // blinken wenn item auslaeuft
        if (0 < pacman.getRemainingItemTime() && pacman.getRemainingItemTime() < 1000000000)
            if (pacman.getRemainingItemTime() % 200000000 < 100000000)
                blend = Consts.P_DEFAULT_BLEND_COLOR;

        // mit faerbung zeichnen
        g.drawImage(theme.PACMAN[game.getPacman().getDir() + (game.getPacman().isPoweredUp() ? 4 : 0)], game.getPacman().getPos().x * theme.TILE_SIZE, game.getPacman().getPos().y * theme.TILE_SIZE + Consts.INFO_BAR_HEIGHT, blend);
    }

    /**
     * Zeichne die obere Infobar.
     */
    private void drawInfobar(Graphics g) {
        g.drawString("Lives: ", Consts.LABEL_LIFES_LEFT, Consts.LABELS_TOP);
        for (int i = 0; i < game.getLives(); i++)
            g.drawImage(theme.LIFE, Consts.LABEL_LIFES_LEFT + g.getFont().getWidth("Lives: ") + i * theme.LIFE.getWidth(), Consts.LABELS_TOP + (g.getFont().getHeight("Lives: ") - theme.LIFE.getHeight()) / 2f);

        g.drawString("Score: " + game.getPoints(), Consts.LABEL_POINTS_LEFT, Consts.LABELS_TOP);
        g.setColor(Consts.INFO_BAR_COLOR);

        g.drawString("Level " + (game.getWonLevels() + 1) + ": " + game.getLevel().getName(), Consts.LABEL_LEVEL_LEFT, Consts.LABELS_TOP);
        g.setColor(Consts.INFO_BAR_COLOR);
    }

    public int getID() {
        return stateID;
    }

    public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
        this.gc = gc;
        theme = Theme.currentTheme;
    }
}
