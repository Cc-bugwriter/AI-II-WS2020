package tud.ai2.pacman.model;

import tud.ai2.pacman.model.entity.GameEntity;
import tud.ai2.pacman.model.entity.Ghost;
import tud.ai2.pacman.model.entity.MovingEntity;
import tud.ai2.pacman.model.entity.Pacman;
import tud.ai2.pacman.model.entity.pickup.Dot;
import tud.ai2.pacman.model.entity.pickup.PowerUp;
import tud.ai2.pacman.model.entity.pickup.SpeedUp;
import tud.ai2.pacman.model.entity.pickup.Teleporter;
import tud.ai2.pacman.model.level.Level;
import tud.ai2.pacman.model.level.LevelParser;
import tud.ai2.pacman.model.level.MapModule;
import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Speichert alle fuer das Spiel relevanten Daten.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class PacmanGame {
    /** aktueller Level */
    private Level level = null;
    /** alle im Level aktiven Elemente */
    private final ArrayList<GameEntity> entities;

    /** Pacman-Spielfigur */
    private final Pacman pacman;
    /** Restversuche */
    private int lives;
    /** aktuelle Punktzahl */
    private int points;
    private int wonLevels;

    /**
     * Konstruktor.
     */
    public PacmanGame() {
        lives = (Consts.TEST ? 1 : Consts.INITIAL_LIVES);
        points = 0;
        wonLevels = 0;
        pacman = new Pacman();
        entities = new ArrayList<>();
    }

    /**
     * Laedt einen spielstand aus dem Stream.
     *
     * @param s der Stream, aus dem die Daten gelesen werden
     * @throws Exception bei fehlerhaft abgespeicherten Daten
     */
    public PacmanGame(DataInputStream s) throws Exception {
        level = LevelParser.fromString(s.readUTF());
        level.setName(s.readUTF());
        lives = s.readInt();
        points = s.readInt();
        wonLevels = s.readInt();
        pacman = (Pacman) Pacman.readEntity(s);
        int count = s.readInt();
        entities = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
            entities.add(GameEntity.readEntity(s));
    }

    /**
     * Schreibt den aktuellen Zustand in den uebergebenen Stream.
     *
     * @param s der Stream, in dem die Daten geschrieben werden
     * @throws IOException bei Schreibfehlern
     */
    public void saveGame(DataOutputStream s) throws IOException {
        s.writeUTF(level.toString());
        s.writeUTF(level.getName());
        s.writeInt(lives);
        s.writeInt(points);
        s.writeInt(wonLevels);
        pacman.writeEntity(s);
        s.writeInt(entities.size());
        for (GameEntity entity : entities)
            entity.writeEntity(s);
    }

    /**
     * @return alle Entitaeten im Level
     */
    public List<GameEntity> getEntities() {
        return entities;
    }

    /**
     * @return die Spielfigur
     */
    public Pacman getPacman() {
        return pacman;
    }

    /**
     * Wechselt den aktuellen Level.
     *
     * @param level neuer Level
     */
    public void changeLevel(Level level) {
        if (Consts.TEST) {
            this.level = level;
            return;
        }

        try {
            level.validate();
        } catch (Exception e) {
            e.printStackTrace();
            level = this.level;
        }
        this.level = level;
        entities.clear();
        pacman.respawn(level.getRandomPacmanSpawn());
        // items spawnen
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                if (level.getField(x, y) == MapModule.DOT)
                    entities.add(new Dot(x, y));
                else if (level.getField(x, y) == MapModule.POWERUP)
                    entities.add(new PowerUp(x, y));
                else if (level.getField(x, y) == MapModule.SPEEDUP)
                    entities.add(new SpeedUp(x, y));
                else if (level.getField(x, y) == MapModule.TELEPORT)
                    entities.add(new Teleporter(x, y));
            }
        }

        // 4 Geister im normalen Spiel, 1 Geist fuer Tests
        for (int i = 0; i < (Consts.TEST ? 1 : Consts.NUM_GHOSTS); i++) {
            Ghost g = new Ghost(i);
            g.respawn(level.getNextGhostSpawn());
            entities.add(g);
        }
    }

    /**
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return true <=> angegebener Punkt ist begehbar
     */
    private boolean validMovement(int x, int y) {
        int w = level.getWidth();
        int h = level.getHeight();
        return !level.isSolid((x + w) % w, (y + h) % h);
    }

    /**
     * Updatet im normalen Spielablauf alle Entitaeten.
     */
    public void updateFrame() {
        if (level != null) {
            for (GameEntity entity : entities) entity.update(this);
            pacman.update(this);
            checkCollisions();
        }
    }

    /**
     * Updatet im Testfall alle Entitaeten
     */
    public void updateStep() {
        if (level != null) {
            // erst bewegung beenden, dann aktivitaet
            pacman.endMove();
            pacman.update(this);
            checkCollisions();
            // beendet die bewegungen von entities sofort, sonst keine aktivitaet
            GameEntity e;
            for (GameEntity entity : entities) {
                e = entity;
                // erst aktivitaet, dann bewegung beenden
                e.update(this);
                if (e instanceof MovingEntity)
                    ((MovingEntity) e).endMove();
            }
            checkCollisions();
        }
    }

    /**
     * @return Anzahl der aktiven Dots
     */
    public int countDots() {
        int sum = 0;
        for (GameEntity entity : entities)
            if (entity instanceof Dot)
                sum++;
        return sum;
    }

    /**
     * Bewegt Pacman um die uebergebene Differenz.
     *
     * @param dx Differenz in X-Richtung
     * @param dy Differenz in Y-Richtung
     * @return true <=> Bewegung erfolgreich
     */
    public boolean movePacman(int dx, int dy) {
        if (pacman.notMoving()) {
            int x = (int) pacman.getPos().x;
            int y = (int) pacman.getPos().y;
            if (validMovement(x + dx, y + dy)) {
                pacman.move(new Point(x + dx, y + dy));
                checkCollisions();
                return true;
            }
        }
        return false;
    }

    /**
     * Ueberprueft Kollision von Pacman mit den anderen Entitaeten.
     * Diese werden ggf. entfernt.
     */
    private void checkCollisions() {
        float x = pacman.getPos().x;
        float y = pacman.getPos().y;

        GameEntity e;
        for (Iterator<GameEntity> i = entities.iterator(); i.hasNext(); ) {
            e = i.next();
            // auf kollision mit spielfigur pruefen
            if (Math.sqrt((x - e.getPos().x) * (x - e.getPos().x) + (y - e.getPos().y) * (y - e.getPos().y)) < 0.5f)
                if (e.collide(this, pacman))
                    i.remove();
        }
    }

    /**
     * Pacman wurde besiegt, setzte alles entsprechend zurueck.
     */
    public void kill() {
        // leben abziehen und geister sowie pacman respawnen
        lives--;
        pacman.respawn(level.getRandomPacmanSpawn());
        for (GameEntity entity : entities)
            if (entity instanceof Ghost)
                ((Ghost) entity).respawn(level.getNextGhostSpawn());
    }

    /**
     * @return Anzahl der bereits gewonnenen Level
     */
    public int getWonLevels() {
        return wonLevels;
    }

    /**
     * @return Anzahl der Zusatztversuche
     */
    public int getLives() {
        return lives;
    }

    /**
     * @param change delta fuer die Versuche
     */
    public void updateLives(int change) {
        lives += change;
    }

    /**
     * @return die aktuelle Punktzahl
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param change delta fuer die Punktzahl
     */
    public void updatePoints(int change) {
        points += change;
    }

    /**
     * @return aktueller Level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @return true <=> Level ist gewonnen
     */
    public boolean isWon() {
        boolean won = countDots() == 0;
        if (won) wonLevels++;
        return won;
    }

    /**
     * @return true <=> Spiel ist verloren
     */
    public boolean isLost() {
        boolean lost = lives <= 0;
        if (lost) wonLevels = 0;
        return lost;
    }

    /**
     * @return die aktuelle Systemzeit in Nanosekunden
     */
    public static long getTime() {
        if (Consts.TEST) return Consts.TEST_TIME;
        return System.nanoTime();
    }
}
