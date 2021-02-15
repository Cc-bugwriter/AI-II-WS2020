package tud.ai2.pacman.exceptions;

/**
 * Diese Exception wird geworfen, falls der Level nicht erreichbare Bereiche enthaelt.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class ReachabilityException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Diese Exception wird geworfen, falls der Level nicht erreichbare Bereiche enthaelt.
     */
    public ReachabilityException(String name) {
        super("Im Level "+ name +" sind unerreichbare Bereiche vorhanden.");
    }

}
