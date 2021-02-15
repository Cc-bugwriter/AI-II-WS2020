package tud.ai2.pacman.model.entity.pickup;

import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.model.entity.GameEntity;
import tud.ai2.pacman.model.entity.Pacman;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Modelliert einen Teleporter, durch welchen der Spieler sich zufaellig teleportiert.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class Teleporter extends GameEntity {

    /**
     * Konstruktor.
     * Setzt die Koordinaten entsprechend.
     *
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     */
    public Teleporter(int x, int y) {
        setPos(new Point(x, y));
    }

    /**
     * {@inheritDoc}
     */
    public Teleporter(DataInputStream s) throws IOException {
        super(s);
    }

    /**
     * {@inheritDoc}
     * Ein Teleporter verschwindet nach Kollision nicht, sondern setzt
     * Pacmans Position neu.
     */
    public boolean collide(PacmanGame game, Pacman pacman) {
        pacman.abortMove();
        pacman.setPos(game.getLevel().getRandomSpaceField());
        return false;
    }

}



