package tud.ai2.pacman.view;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import tud.ai2.pacman.model.level.MapModule;
import tud.ai2.pacman.util.Consts;

import java.io.IOException;

/**
 * Speichert alle relevanten anzeigbaren Assets.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class Theme {
    /** Basispfad aller Bild-Dateien */
    private final String basePath;

    /** aktuell aktives Thema */
    public static Theme currentTheme;

    /** Hintergrundbild im Hauptmenu */
    public final Image MENU_BACKGROUND;
    /** Hintergrundbild im Highscore */
    public final Image HIGHSCORE_BACKGROUND;
    /** Zusatzversuch Asset */
    public final Image LIFE;

    /** Groesse der Module */
    public final int TILE_SIZE;
    /** Wand-Assets */
    public final Image[] WALLS = new Image[16];
    /** Hintergrund Asset */
    public final Image BACKGROUND;
    /** Dot Asset */
    public final Image DOT;
    /** PowerUp Asset */
    public final Image POWER_UP;
    /** SpeedUp Asset */
    public final Image SPEED_UP;
    /** Teleporter Asset */
    public final Image TELEPORTER;

    /** Pacman-Assets */
    public final Image[] PACMAN = new Image[8];
    /** Geister-Assets */
    public final Image[] GHOST = new Image[17];

    public Theme(String path) throws SlickException, IOException {
        if (!path.endsWith("/")) path += "/";

        basePath = Consts.THEME_FOLDER + path;

        MENU_BACKGROUND = loadImage("ui/menuback");
        HIGHSCORE_BACKGROUND = loadImage("ui/highscore_background");
        LIFE = loadImage("ui/life");

        for (int i = 0; i < 16; i++)
            WALLS[i] = loadImage("map/" + MapModule.WALL.getValue() + i);
        BACKGROUND = loadImage("map/" + MapModule.BACKGROUND.getValue());
        DOT = loadImage("entities/dot");
        POWER_UP = loadImage("entities/powerup");
        SPEED_UP = loadImage("entities/speedup");
        TELEPORTER = loadImage("entities/teleporter");
        TILE_SIZE = WALLS[0].getWidth();
        for (int i = 0; i < 4; i++) {
            PACMAN[i] = loadImage("entities/P" + i);
            PACMAN[i + 4] = loadImage("entities/P" + i + "_powerup");
        }
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                GHOST[i*4 + j] = loadImage("entities/G" + i + j);

        GHOST[16] = loadImage("entities/G_chase");
    }

    /**
     * Laedt ein Bild aus einem bestimmten Pfad.
     */
    private Image loadImage(String name) throws SlickException {
        return new Image(basePath + name + Consts.IMAGE_EXTENSION);
    }

    /**
     * Aktualisiert das verwendete Thema.
     */
    public static void setCurrentTheme(String path) throws SlickException, IOException {
        if (path.trim().toLowerCase().equals("covid"))
            path = "theme1";

        currentTheme = new Theme(path);
    }
}
