package tud.ai2.pacman.model.entity.pickup;

import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.model.entity.GameEntity;
import tud.ai2.pacman.model.entity.Pacman;
import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Modelliert ein SpeedUp, durch welches der Spieler sich schneller bewegen kann.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class SpeedUp extends GameEntity {

    /**
     * Konstruktor.
     * Setzt die Koordinaten entsprechend.
     *
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     */
    public SpeedUp(int x, int y) {
        setPos(new Point(x, y));
    }

    /**
     * {@inheritDoc}
     */
    public SpeedUp(DataInputStream s) throws IOException {
        super(s);
    }

    /**
     * {@inheritDoc}
     * Ein SpeedUp verschwindet nach Kollision, vergibt die entsprechende Punktzahl
     * und verschnellert Pacman.
     */
    public boolean collide(PacmanGame game, Pacman pacman) {
        game.updatePoints(Consts.SPEED_UP_POINTS);
        pacman.speedUp();
        return true;
    }

}
