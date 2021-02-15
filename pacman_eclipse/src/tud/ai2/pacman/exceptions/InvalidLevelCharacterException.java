package tud.ai2.pacman.exceptions;

/**
 * Diese Exception wird geworfen, falls ein unbekannes Zeichen beim Parsen
 * eines Levels eingelesen wird.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class InvalidLevelCharacterException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Diese Exception wird geworfen, falls ein unbekannes Zeichen beim Parsen
     * eines Levels eingelesen wird.
     *
     * @param c das unbekannte Zeichen
     */
    public InvalidLevelCharacterException(char c) {
        super("'" + c + "' ist kein gueltiger Levelbaustein!");
    }

}
