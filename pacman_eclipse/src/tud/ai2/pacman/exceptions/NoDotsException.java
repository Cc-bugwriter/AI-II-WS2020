package tud.ai2.pacman.exceptions;

/**
 * Diese Exception wird geworfen, falls der Level keine freien Flaechen enthaelt.
 * Hierdurch koennen keine Dots platziert werden.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class NoDotsException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Diese Exception wird geworfen, falls der Level keine freien Flaechen enthaelt.
     * Hierdurch koennen keine Dots platziert werden.
     */
    public NoDotsException() {
        super("Es gibt keine sammelbaren Dots!");
    }

}
