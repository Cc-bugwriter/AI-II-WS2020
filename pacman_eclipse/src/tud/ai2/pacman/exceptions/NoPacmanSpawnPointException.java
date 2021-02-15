package tud.ai2.pacman.exceptions;

/**
 * Diese Exception wird geworfen, falls der eingelesene Level keine
 * Pacman-Spawner enthaelt.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class NoPacmanSpawnPointException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     *  Diese Exception wird geworfen, falls der eingelesene Level keine
     *  Pacman-Spawner enthaelt.
     */
    public NoPacmanSpawnPointException() {
        super("Es wurde kein Spawnpunkt fuer Pacman definiert!");
    }

}
