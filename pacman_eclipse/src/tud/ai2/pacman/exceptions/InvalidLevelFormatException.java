package tud.ai2.pacman.exceptions;

/**
 * Diese Exception wird geworfen, falls der eingelesene Level eine ungueltige
 * Dimension besitzt. Ein Beispiel sind 10 Zeichen in der ersten Zeile,
 * aber nur 9 in der zweiten.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class InvalidLevelFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Diese Exception wird geworfen, falls der eingelesene Level eine ungueltige
     * Dimension besitzt. Ein Beispiel sind 10 Zeichen in der ersten Zeile,
     * aber nur 9 in der zweiten.
     */
    public InvalidLevelFormatException() {
        super("Der Level muss rechteckig sein!");
    }

}
