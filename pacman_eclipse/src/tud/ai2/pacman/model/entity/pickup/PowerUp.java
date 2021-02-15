package tud.ai2.pacman.model.entity.pickup;

import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.model.entity.GameEntity;
import tud.ai2.pacman.model.entity.Pacman;
import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Modelliert ein PowerUp, durch welches der Spieler die Geister fressen kann.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class PowerUp extends GameEntity {

    /**
     * Konstruktor.
     * Setzt die Koordinaten entsprechend.
     *
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     */
    public PowerUp(int x, int y) {
        setPos(new Point(x, y));
    }

    /**
     * {@inheritDoc}
     */
    public PowerUp(DataInputStream s) throws IOException {
        super(s);
    }

    /**
     * {@inheritDoc}
     * Ein PowerUp verschwindet nach Kollision, vergibt die entsprechende Punktzahl
     * und powert Pacman auf.
     */
    public boolean collide(PacmanGame game, Pacman pacman) {
        game.updatePoints(Consts.POWER_UP_POINTS);
        pacman.powerUp();
        return true;
    }
}
