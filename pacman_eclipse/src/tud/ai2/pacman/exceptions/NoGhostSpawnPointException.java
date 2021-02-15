package tud.ai2.pacman.exceptions;

/**
 * Diese Exception wird geworfen, falls der eingelesene Level keine
 * Geister-Spawner enthaelt.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class NoGhostSpawnPointException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Diese Exception wird geworfen, falls der eingelesene Level keine
     * Geister-Spawner enthaelt.
     */
    public NoGhostSpawnPointException() {
        super("Es wurde kein Spawnpunkt fuer die Geister definiert!");
    }

}
