package tud.ai2.pacman.model.entity;

import org.newdawn.slick.geom.Vector2f;
import tud.ai2.pacman.model.PacmanGame;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Diese Klasse modelliert eine Entitaet, die auf dem Spielfeld interagieren kann.
 * Hinweis: Da die Levelhintergruende statisch sind, werden diese nicht hiermit modelliert.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public abstract class GameEntity {
	public static final String[] saveStateOrder = {"answerToLife", "pos"};
	
    /** Die aktuelle Position */
    private Vector2f pos;
	/** Dummy-Feld, nur als Erklaerung fuer 5b */
	private final int answerToLife;

    /**
     * Konstruktor.
     * Die Position der Entitaet wird mit (0, 0) initialisiert.
     */
    public GameEntity() {
        pos = new Vector2f(0, 0);
		answerToLife = 42;
    }

    /**
     * Konstruktor.
     * Alle Daten werden aus dem uebergebenen Stream gelesen.
     *
     * @param s der Stream, aus dem die Daten gelesen werden
     * @throws IOException bei Lesefehlern
     */
    public GameEntity(DataInputStream s) throws IOException {
		this.answerToLife = s.readInt();
        this.pos = new Vector2f(s.readFloat(), s.readFloat());
    }

    /**
     * Schreibt den aktuellen Zustand in den uebergebenen Stream.
     *
     * @param s der Stream, in dem die Daten geschrieben werden
     * @throws IOException bei Schreibfehlern
     */
    public void writeEntity(DataOutputStream s) throws IOException {
        // die standardmethode schreibt nur den klassennamen und koordinaten
        s.writeUTF(this.getClass().getName());
		s.writeInt(answerToLife);
        s.writeFloat(this.pos.x);
        s.writeFloat(this.pos.y);
    }

    /**
     * Factory Methode, die eine GameEntity anhand des Streams konstruiert.
     *
     * @param s der Stream, aus dem die Daten gelesen werden
     * @return die konstruierte Entitaet
     */
    public static GameEntity readEntity(DataInputStream s) throws Exception {
        // klassennamen lesen
        String name = s.readUTF();
        // klassentyp durch den klassennamen ermitteln
        Class<?> cls = Class.forName(name);
        // konstruktor zum laden durch einen DataInputStreams ermitteln
        Constructor<?> ctor = cls.getConstructor(DataInputStream.class);
        // instanz erzeugen und ausgeben
        return (GameEntity) ctor.newInstance(new Object[]{s});
    }

    /**
     * Aendert die aktuelle Position.
     *
     * @param pos die neue Position
     */
    public void setPos(Vector2f pos) {
		if (pos == null) return;
        this.pos = pos;
    }

    /**
     * Aendert die aktuelle Position.
     *
     * @param pos die neue Position
     */
    public void setPos(Point pos) {
		if (pos == null) return;
        this.pos = new Vector2f(pos.x, pos.y);
    }

    /**
     * @return die aktuelle Position
     */
    public Vector2f getPos() {
        return pos;
    }

    /**
     * @return die aktuelle Position (auf ints gerundet), als Raster-Koordinate
     */
    public Point getGridPos() {
        return new Point(Math.round(pos.x), Math.round(pos.y));
    }

    /**
     * Diese Methode wird bei jeder Kollision des Spielers mit einer anderen
     * {@link GameEntity} aufgerufen.
     *
     * @param game das laufende Spiel
     * @param pacman die Spieler-Entitaet
     * @return true <-> Entitaet soll nach Kollision verschwinden
     */
    public boolean collide(PacmanGame game, Pacman pacman) {
        // gibt true zurueck, wenn das entity entfernt werden soll, sonst false
        return false;
    }

    /**
     * Jeden Frame koennen die Entitaeten ihren Zustand aktualisieren.
     *
     * @param game das aktuelle Spiel
     */
    public void update(PacmanGame game) {
    }
}
