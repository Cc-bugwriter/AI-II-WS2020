package tud.ai2.pacman.model.entity.pickup;

import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.model.entity.GameEntity;
import tud.ai2.pacman.model.entity.Pacman;
import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Modelliert einen Punkt, den der Spieler aufsammeln muss.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class Dot extends GameEntity {

    /**
     * Konstruktor.
     * Setzt die Koordinaten entsprechend.
     *
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     */
    public Dot(int x, int y) {
        setPos(new Point(x, y));
    }

    /**
     * {@inheritDoc}
     */
    public Dot(DataInputStream s) throws IOException {
        super(s);
    }

    /**
     * {@inheritDoc}
     * Ein Dot verschwindet nach Kollision und vergibt die entsprechende Punktzahl.
     */
    public boolean collide(PacmanGame game, Pacman pacman) {
        if (Consts.BUG_256 && game.getWonLevels() >= 256 && game.getLevel().getWidth()/2f <= getPos().x)
            return false;
        game.updatePoints(Consts.DOT_POINTS);
        return true;
    }

}
