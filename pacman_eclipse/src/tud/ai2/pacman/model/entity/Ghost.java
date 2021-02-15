package tud.ai2.pacman.model.entity;

import org.newdawn.slick.geom.Vector2f;
import tud.ai2.pacman.model.PacmanGame;
import tud.ai2.pacman.model.level.Level;
import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import java.util.ArrayList;

/**
 * Modelliert eine gegnerische Geister-Entitaet.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Kurt Cieslinski
 */
@SuppressWarnings("unused")
public class Ghost extends MovingEntity {
    public static final String[] saveStateOrder = {"number", "respawnTime", "idle", "oldPos"};

    /** Ein Random-Generator. NICHT ABSPEICHERN/LADEN! */
    protected final Random rnd;

    /** Geister-Nummer, bestimmt den Skin */
    private final int number;
    /** Zeitpunkt des letzten Respawns */
    private long respawnTime;
    /** Ob der Geist gerade seine Respawn-Bewegung ausfuehrt */
    private boolean idle;

    /** Position vor der letzten Bewegung */
    private Point oldPos = null;

    /**
     * Konstruktor.
     *
     * @param num Geister-ID
     */
    public Ghost(int num) {
        super(Consts.G_IDLE_MOVE_SPEED);
        number = num;
        rnd = new Random();
        idle = true;
    }

    /**
     * Konstruktor.
     * Alle Daten werden aus dem uebergebenen Stream gelesen.
     *
     * @param s der Stream, aus dem die Daten gelesen werden
     * @throws IOException bei Lesefehlern
     */
    public Ghost(DataInputStream s) throws IOException {
        // TODO task 5c
    	super(s);
    	this.rnd = new Random();
    	this.number =  s.readInt();
    	this.respawnTime = s.readLong() + PacmanGame.getTime();
    	this.idle = s.readBoolean();
        if (s.readBoolean()) {
        	this.oldPos = new Point(s.readInt(), s.readInt());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEntity(DataOutputStream s) throws IOException {
        // TODO task 5b
    	super.writeEntity(s);
		s.writeInt(this.number);
		s.writeLong(this.respawnTime - PacmanGame.getTime());
		s.writeBoolean(this.idle);
        if (oldPos == null) {
        	s.writeBoolean(false);
        }
        else {
        	s.writeBoolean(true);
        	s.writeInt(this.oldPos.x);
        	s.writeInt(this.oldPos.y);
        }
    }

    /**
     * Setzt den Geist an eine bestimmte Position zurueck.
     * In der Regel wurde der Geist vorher von Pacman gefressen.
     *
     * @param pos Respawn-Position
     */
    public void respawn(Point pos) {
        respawnTime = System.nanoTime();
        setSpeed(Consts.G_IDLE_MOVE_SPEED);
        idle = true;
        super.abortMove();
        // zufaellige startposition der wartebewegung (sieht beim start etwas schoener aus)
        super.setPos(new Vector2f(pos.x, pos.y + (rnd.nextFloat() - 0.5f) * (Consts.G_IDLE_MOVE_SIZE * 2)));
    }

    /**
     * @return die Geister-Nummer
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return ob der Geist noch in der Respawn-Animation ist
     */
    public boolean isIdle() {
        return idle;
    }

    /**
     * {@inheritDoc}
     * Ist der Geist in der Respawnanimation, kann er nicht mit Pacman kollidieren.
     * Ansonsten koennen Geister nur entfernt werden, wenn Pacman im Powerup-Zustand ist.
     */
    public boolean collide(PacmanGame game, Pacman pacman) {
        // geister duerfen nicht idle sein um den spieler zu toeten / punkte zu bringen
        if (!idle) {
            if (pacman.isPoweredUp()) {
                // im powerup-zustand den geist toeten
                game.updatePoints(Consts.GHOST_POINTS);
                respawn(game.getLevel().getNextGhostSpawn());
            } else game.kill();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * Plane eine neue Bewegung, falls der Geist gerade stillsteht.
     * Teleportiere den Geist am Rand.
     */
    @Override
    public void update(PacmanGame game) {
        if (idle && Consts.TEST) {
            super.abortMove();
            idle = false;
        }

        super.update(game);

        if (super.notMoving()) {
            // naechste bewegung starten
            if (idle && (System.nanoTime() - respawnTime) < Consts.G_RESPAWN_IDLE_TIME) {
                // idle-bewegung
                if (super.getPos().y > Math.round(super.getPos().y))
                    super.move(new Vector2f(super.getPos().x, Math.round(super.getPos().y) - Consts.G_IDLE_MOVE_SIZE));
                else
                    super.move(new Vector2f(super.getPos().x, Math.round(super.getPos().y) + Consts.G_IDLE_MOVE_SIZE));
            } else {
                if (idle) {
                    // idle-state verlassen
                    super.setPos(new Point((int) super.getPos().x, Math.round(super.getPos().y)));
                    setSpeed(Consts.G_MOVE_SPEED);
                    idle = false;
                }

                // neue bewegung starten und alte position speichern
                Point p = super.getGridPos();
                super.move(getTargetPoint(game));
                oldPos = p;
            }
        }
        borderTeleport(game.getLevel());
    }

    /**
     * @param game das Pacman-Spiel
     * @return der aktuell passende Zielpunkt
     */
    private Point getTargetPoint(PacmanGame game) {
        // erst auf Sichtkontakt pruefen
        Point p = getSightPoint(game);
        if (p != null) return p;

        // alle Abzweigungen ermitteln
        Point[] branches = game.getLevel().getBranches(super.getGridPos());
        // zufaellig eine Abzweigung waehlen, die moeglichst nicht der vorherigen Position entspricht
        return getRandomBranch(branches, oldPos);
    }

    // TODO task 4b
    private Point getSightPoint(PacmanGame game) {
    	Point pacman = game.getPacman().getGridPos();
    	Point ghost = super.getGridPos();
    	Point target = null;
    	
    	if (!game.getLevel().existsStraightPath(pacman, ghost)) {
    		// none sight contact
    		return target;
    	}
    	
    	if (game.getLevel().existsStraightPath(pacman, ghost) && !game.getPacman().isPoweredUp()) {
    		// sight contact without PoweredUp
    		if (pacman.y == ghost.y) {
    			// horizontal
    			if (pacman.x > ghost.x) target = new Point(ghost.x + 1, ghost.y);
    			if (pacman.x < ghost.x) target = new Point(ghost.x - 1, ghost.y);
    		} 
    		if (pacman.x == ghost.x) {
    			// vertical
    			if (pacman.y > ghost.y) target = new Point(ghost.x, ghost.y + 1);
    			if (pacman.y < ghost.y) target = new Point(ghost.x, ghost.y - 1);
    		}
    	}
    	
    	if (game.getLevel().existsStraightPath(pacman, ghost) && game.getPacman().isPoweredUp())  {
    		// sight contact with PoweredUp -> Ghost escape!
    		ArrayList<Point> targetList = new ArrayList<>();
    		if (pacman.y == ghost.y) {
    			// horizontal
    			if (pacman.x > ghost.x) {
    				targetList.add(new Point(ghost.x - 1, ghost.y));
    				targetList.add(new Point(ghost.x, ghost.y + 1));
    				targetList.add(new Point(ghost.x, ghost.y - 1));
    			}
    			if (pacman.x < ghost.x) {
    				targetList.add(new Point(ghost.x + 1, ghost.y));
    				targetList.add(new Point(ghost.x, ghost.y + 1));
    				targetList.add(new Point(ghost.x, ghost.y - 1));
    			}
    		} 
    		if (pacman.x == ghost.x) {
    			// vertical
    			if (pacman.y > ghost.y) {
    				targetList.add(new Point(ghost.x - 1, ghost.y));
    				targetList.add(new Point(ghost.x + 1, ghost.y));
    				targetList.add(new Point(ghost.x, ghost.y - 1));
    			}
    			if (pacman.y < ghost.y) {
    				targetList.add(new Point(ghost.x - 1, ghost.y));
    				targetList.add(new Point(ghost.x + 1, ghost.y));
    				targetList.add(new Point(ghost.x, ghost.y + 1));
    			}
    			
    		}
    		targetList.removeIf(point -> !game.getLevel().rangeValid(point));
    		targetList.removeIf(point -> game.getLevel().isWall(point.x, point.y));
    		if (!targetList.isEmpty()) {
    			target = targetList.get(rnd.nextInt(targetList.size()));
    		}
    	}
    	
        return target;
    }

    // TODO task 4a
    /**
     * randomly select a point from array
     * @param branches, candidate points array
     * @param avoid, the avoided point to select
     * @return select point
     */
    private Point getRandomBranch(Point[] branches, Point avoid) {
    	if (branches.length == 0) {
    		return null;
    	}
    	
    	if (branches.length == 1) {
    		return branches[0];
    	} else {
    		// exclude avoid point in array
    		Point randomBranch = branches[rnd.nextInt(branches.length)];
    		if (!randomBranch.equals(avoid)) {
    			return randomBranch;
    		} else {
    			return getRandomBranch(branches, avoid);
    		}
    	}
    }


}
