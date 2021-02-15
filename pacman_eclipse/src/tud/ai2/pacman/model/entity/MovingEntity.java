package tud.ai2.pacman.model.entity;

import org.newdawn.slick.geom.Vector2f;
import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.model.level.Level;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Diese Klasse modelliert eine Entitaet, die sich ueber das Spielraster
 * bewegen kann. Modelliert ausserdem eine "Blickrichtung".
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public abstract class MovingEntity extends GameEntity {
    public static final String[] saveStateOrder = {"start", "target", "speed",
    		"lastMoveTick", "moveInterval", "moveSpeedX", "moveSpeedY", "dir"};

    /** Startpunkt der fluessigen Bewegung */
    protected Vector2f start = null;
    /** Endpunkt der fluessigen Bewegung */
    protected Vector2f target = null;

    /** Geschwindigkeit in Felder/ns */
    private float speed;
    /** Letzter Zeitpunkt der Bewegung */
    private long lastMoveTick;
    /** Interval, in dem die Bewegung ausgefuehrt wird */
    private int moveInterval;
    /** Geschwindigkeit in X Richtung */
    private float moveSpeedX;
    /** Geschwindigkeit in Y Richtung */
    private float moveSpeedY;

    /** Modelliert die Blickrichtung */
    protected int dir;

    /**
     * Konstruktor.
     * Schaue in Richtung 0.
     */
    public MovingEntity() {
        super();
        dir = 0;
    }

    /**
     * Konstruktor.
     *
     * @param speed urspruengliche Bewegungsgeschwindigkeit
     */
    public MovingEntity(float speed) {
        this();
        setSpeed(speed);
    }

    /**
     * Konstruktor.
     * Alle Daten werden aus dem uebergebenen Stream gelesen.
     *
     * @param s der Stream, aus dem die Daten gelesen werden
     * @throws IOException bei Lesefehlern
     */
    public MovingEntity(DataInputStream s) throws IOException {
        // TODO task 5c
    	super(s);
    	if (s.readBoolean()) {
    		this.start = new Vector2f(s.readFloat(), s.readFloat());
    	}
    	if (s.readBoolean()) {
    		this.target = new Vector2f(s.readFloat(), s.readFloat());
    	}
    	this.speed = s.readFloat();
    	this.lastMoveTick = s.readLong() + PacmanGame.getTime();
    	this.moveInterval = s.readInt();
    	this.moveSpeedX = s.readFloat();
    	this.moveSpeedY = s.readFloat();
    	this.dir = s.readInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEntity(DataOutputStream s) throws IOException {
        // TODO task 5b
		super.writeEntity(s);
		if (this.start == null) {
			s.writeBoolean(false);
		}else {
			s.writeBoolean(true);
			s.writeFloat(this.start.x);
			s.writeFloat(this.start.y);
		}
		if (this.target == null) {
			s.writeBoolean(false);
		}else {
			s.writeBoolean(true);
			s.writeFloat(this.target.x);
			s.writeFloat(this.target.y);
		}
		s.writeFloat(this.speed);
		s.writeLong(this.lastMoveTick - PacmanGame.getTime());
		s.writeInt(this.moveInterval);
		s.writeFloat(this.moveSpeedX);
		s.writeFloat(this.moveSpeedY);
		s.writeInt(this.dir);
    }

    /**
     * 0: rechts, 1: oben; 2: links; 3: unten
     *
     * @return die aktuelle Blickrichtung
     */
    public int getDir() {
        return dir;
    }

    /**
     * @return die Bewegungsgeschwindigkeit in Felder/s
     */
    public float getFieldsPerSecond() {
        return speed * 1000000000;
    }

    /**
     * Aktualisiert die Geschwindigkeit.
     * @param fieldsPerSecond die neue Bewegungsgeschwindigkeit in Felder/s
     */
    public void setSpeed(float fieldsPerSecond) {
        // in felder pro ns umrechnen (1000000000ns = 1s)
        speed = fieldsPerSecond / 1000000000;
    }

    /**
     * {@inheritDoc}
     * Verschiebt ausserdem die geplante Bewegung, diese wird
     * NICHT abgebrochen.
     *
     * @param pos die neue Position
     */
    public void setPos(Vector2f pos) {
        if (pos == null) return;

        if (target != null) {
            // gesamte bewegung mitverschieben
            float dx = pos.x - super.getPos().x;
            float dy = pos.y - super.getPos().y;
            target.x += dx;
            target.y += dy;
            start.x += dx;
            start.y += dy;
        }
        super.setPos(pos);
    }

    /**
     * {@inheritDoc}
     * Verschiebt ausserdem die geplante Bewegung, diese wird
     * NICHT abgebrochen.
     *
     * @param pos die neue Position
     */
    public void setPos(Point pos) {
        if (pos == null) return;
        setPos(new Vector2f(pos.x, pos.y));
    }

    /**
     * Aendert die Blickrichtung anhand der geplanten Bewegung.
     * 0: rechts, 1: oben; 2: links; 3: unten
     *
     * @param newPos neue Position
     */
    private void changeDir(Vector2f newPos) {
        Vector2f oldPos = super.getPos();

        if      (oldPos.x < newPos.x) dir = 0;
        else if (newPos.y < oldPos.y) dir = 1;
        else if (newPos.x < oldPos.x) dir = 2;
        else if (oldPos.y < newPos.y) dir = 3;
    }

    /**
     * Initialisiert eine fluessige Bewegung zu einer bestimmten Position.
     *
     * @param pos die Zielposition
     */
    public void move(Vector2f pos) {
        if (pos == null) return;

        // bewegungsrichtung ermitteln
        changeDir(pos);

        // startzeit der bewegung speichern
        lastMoveTick = System.nanoTime();

        // alte bewegung sofort abschliessen
        if (target != null) super.setPos(target);

        // eckpunkte der bewegung speichern
        start = super.getPos();
        target = pos;

        // geschwindigkeitsvektor und bewegungszeit berechnen
        moveInterval = (int) (Math.sqrt((target.x - start.x) * (target.x - start.x) + (target.y - start.y) * (target.y - start.y)) / speed); // = distanz/speed
        moveSpeedX = (target.x - start.x) / (float) moveInterval;
        moveSpeedY = (target.y - start.y) / (float) moveInterval;
    }

    /**
     * Initialisiert eine fluessige Bewegung zu einer bestimmten Position.
     *
     * @param pos die Zielposition
     */
    public void move(Point pos) {
        if (pos == null) return;
        move(new Vector2f(pos.x, pos.y));
    }

    /**
     * @return true <-> Entitaet hat keine geplante fluessige Bewegung
     */
    public boolean notMoving() {
        // bewegung nur erlaubt, wenn gerade keine andere ablaeuft
        return target == null;
    }

    /**
     * Beendet die aktuelle fluessige Bewegung sofort.
     * Die Entitaet wird zur Zielposition bewegt.
     */
    public void endMove() {
        if (target != null) {
            super.setPos(target);
        }
        abortMove();
    }

    /**
     * Beendet die aktuelle fluessige Bewegung sofort.
     * Die Entitaet wird NICHT zur Zielposition bewegt.
     */
    public void abortMove() {
        target = null;
        start = null;
    }

    /**
     * {@inheritDoc}
     * Aktualiesiert die Position anhand der geplanten Bewegung.
     */
    public void update(PacmanGame game) {
        if (target != null) {
            // zeitpunkt innerhalb der bewegung bestimmen
            long dt = System.nanoTime() - lastMoveTick;
            if (dt < (long) moveInterval)
                // bewegungsschritt ausfuehren
                super.setPos(new Vector2f(start.x + dt * moveSpeedX, start.y + dt * moveSpeedY));
            else {
                // bewegung beenden
                super.setPos(target);
                target = null;
                start = null;
            }
        }
    }

    /**
     * Laesst die Entitaet auf die andere Seite des Levels teleportieren.
     * Wird nur ausgefuehrt, wenn die Position ausserhalb der Levelgrenzen ist.
     *
     * @param level zu verwendender level
     */
    protected void borderTeleport(Level level) {
        int width = level.getWidth(), height = level.getHeight();

        Point p = getGridPos();
        Vector2f pos = getPos();

        if (p.x < 0)
            setPos(new Vector2f(pos.x + width, pos.y));
        else if (width <= p.x)
            setPos(new Vector2f(pos.x - width, pos.y));

        if (p.y < 0)
            setPos(new Vector2f(pos.x, pos.y + height));
        else if (height <= p.y)
            setPos(new Vector2f(pos.x, pos.y - height));
    }
}
