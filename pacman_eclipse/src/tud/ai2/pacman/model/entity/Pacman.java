package tud.ai2.pacman.model.entity;

import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Modelliert den spielbaren Pacman.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Kurt Cieslinski
 */
public class Pacman extends MovingEntity {
    public static final String[] saveStateOrder = {"poweredUp", "speedUp", "speedUpStartTime", "powerUpStartTime"};

    /** Ob gerade ein PowerUp aktiv ist */
    private boolean poweredUp;
    /** Ob gerade ein SpeedUp aktiv ist */
    private boolean speedUp;
    /** Startzeit des letzten SpeedUps */
    private long speedUpStartTime;
    /** Startzeit des letzten PowerUps */
    private long powerUpStartTime;

    /**
     * Konstruktor.
     */
    public Pacman() {
        super(Consts.P_MOVE_SPEED);
        poweredUp = false;
        speedUp = false;
    }

    /**
     * Konstruktor.
     * Alle Daten werden aus dem uebergebenen Stream gelesen.
     *
     * @param s der Stream, aus dem die Daten gelesen werden
     * @throws IOException bei Lesefehlern
     */
    public Pacman(DataInputStream s) throws IOException {
        // TODO task 5c
    	super(s);
    	this.poweredUp = s.readBoolean();
    	this.speedUp = s.readBoolean();
    	this.speedUpStartTime = s.readLong() + PacmanGame.getTime();
    	this.powerUpStartTime = s.readLong() + PacmanGame.getTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEntity(DataOutputStream s) throws IOException {
        // TODO task 5b
    	super.writeEntity(s);
    	s.writeBoolean(this.poweredUp);
		s.writeBoolean(this.speedUp);
		// save the buff duration
		s.writeLong(this.speedUpStartTime - PacmanGame.getTime());
		s.writeLong(this.powerUpStartTime - PacmanGame.getTime());
    }

    /**
     * Setzt Pacman an eine bestimmte Position zurueck.
     *
     * @param pos Respawn-Position
     */
    public void respawn(Point pos) {
        // Ausrichtung auf default setzten und Entity sofort zu pos verschieben
        setSpeed(Consts.P_MOVE_SPEED);
        poweredUp = false;
        speedUp = false;
        dir = 0;
        super.abortMove();
        super.setPos(pos);
    }

    /**
     * @return true <-> Pacman hat ein PowerUp
     */
    public boolean isPoweredUp() {
        return poweredUp;
    }

    /**
     * Aktiviert ein PowerUp.
     */
    public void powerUp() {
        setSpeed(Math.max(getFieldsPerSecond(), Consts.P_POWER_UP_MOVE_SPEED));
        powerUpStartTime = System.nanoTime();
        poweredUp = true;
    }

    /**
     * Deaktiviert ein PowerUp.
     */
    public void deactivatePowerUp() {
        setSpeed(speedUp ? Consts.P_SPEED_UP_MOVE_SPEED : Consts.P_MOVE_SPEED);
        poweredUp = false;
    }

    /**
     * @return true <-> Pacman hat ein SpeedUp
     */
    public boolean hasSpeedUp() {
        return speedUp;
    }

    /**
     * Aktiviert ein SpeedUp.
     */
    public void speedUp() {
        setSpeed(Math.max(getFieldsPerSecond(), Consts.P_SPEED_UP_MOVE_SPEED));
        speedUpStartTime = System.nanoTime();
        speedUp = true;
    }

    /**
     * Deaktiviert ein SpeedUp.
     */
    public void deactivateSpeedUp() {
        setSpeed(poweredUp ? Consts.P_POWER_UP_MOVE_SPEED : Consts.P_MOVE_SPEED);
        speedUp = false;
    }

    /**
     * @return minimale Zeit, bis ein Item auslaeuft.
     *          -1, falls nichts aktiv ist
     */
    public long getRemainingItemTime() {
        long speedUpTime = Consts.P_SPEED_UP_TIME - System.nanoTime() + speedUpStartTime;
        long powerUpTime = Consts.P_POWER_UP_TIME - System.nanoTime() + powerUpStartTime;

        // die uebrige Zeit haengt von dem aktuellen Zustand ab
        if (speedUp && poweredUp)
            return Math.min(speedUpTime, powerUpTime);
        if (speedUp)
            return speedUpTime;
        if (poweredUp)
            return powerUpTime;

        return -1;
    }

    /**
     * {@inheritDoc}
     * Deaktiviert Items, wenn diese abgelaufen sind.
     */
    public void update(PacmanGame game) {
        super.update(game);
        if (!Consts.TEST) {
            // booster deaktivieren, wenn dieser abgelaufen ist
            if (speedUp && System.nanoTime() > (Consts.P_SPEED_UP_TIME + speedUpStartTime))
                deactivateSpeedUp();

            if (poweredUp && System.nanoTime() > (Consts.P_POWER_UP_TIME + powerUpStartTime))
                deactivatePowerUp();
        }

        // pacman durch spielfeldrand teleportieren
        borderTeleport(game.getLevel());
    }

}
