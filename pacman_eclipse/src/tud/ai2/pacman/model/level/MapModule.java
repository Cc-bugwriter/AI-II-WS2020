package tud.ai2.pacman.model.level;

import java.util.HashMap;
import java.util.Map;

/**
 * Ein MapModule modelliert einen Levelbaustein.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public enum MapModule {
    WALL('X'),
    DOT(' '),
    SPACE('_'),
    BACKGROUND('B'),
    PLAYER_SPAWN('P'),
    GHOST_SPAWN('G'),
    POWERUP('U'),
    SPEEDUP('S'),
    TELEPORT('T');

    /** Zeichen, welches dieses Modul repraesentiert */
    private final char value;

    /**
     * @param c Zeichen, welches dieses Modul repraesentiert
     */
    MapModule(char c) {
        value = c;
    }

    /**
     * @return Zeichen des Moduls
     */
    public char getValue() {
        return value;
    }

    /** Mapt Zeichen auf das passende Modul */
    private static final Map<Character, MapModule> byValueMap = new HashMap<>();

    static {
        for (MapModule value : values())
            byValueMap.put(value.getValue(), value);
    }

    /**
     * @param c gesuchtes Zeichen
     * @return passendes MapModule
     */
    public static MapModule findByValue(char c) {
        return byValueMap.get(c);
    }

    /**
     * @param c zu pruefendes Zeichen
     * @return true <-> Zeichen is
     */
    public static boolean isValid(char c) {
        return byValueMap.get(c) != null;
    }
}