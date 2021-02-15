package tud.ai2.pacman.model.level;

import tud.ai2.pacman.exceptions.NoDotsException;
import tud.ai2.pacman.exceptions.NoGhostSpawnPointException;
import tud.ai2.pacman.exceptions.NoPacmanSpawnPointException;
import tud.ai2.pacman.exceptions.ReachabilityException;
import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;


/**
 * Modelliert einen spielbaren Level.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public class Level {
    /** aktuell angesprochener Geisterspawner */
    private int currentGhostSpawnCounter = 0;

    /** Ein Random-Generator */
    private final Random rnd;
    /** Name des Levels */
    private String name;
    /** Level-Layout */
    private final MapModule[][] map;
    /** alle Positionen der Pacman-Spawner */
    private final Point[] pacmanSpawns;
    /** alle Positionen der Geister-Spawner */
    private final Point[] ghostSpawns;

    /**
     * Konstruktor.
     *
     * @param map Level-Layout
     * @param pacmanSpawns Positionen der Pacman-Spawner
     * @param ghostSpawns Positionen der Geister-Spawner
     */
    public Level(MapModule[][] map, Point[] pacmanSpawns, Point[] ghostSpawns) {
        this("Unbenannter Level", map, pacmanSpawns, ghostSpawns);
    }

    /**
     * Konstruktor.
     *
     * @param name Name des Levels
     * @param map Level-Layout
     * @param pacmanSpawns Positionen der Pacman-Spawner
     * @param ghostSpawns Positionen der Geister-Spawner
     */
    public Level(String name, MapModule[][] map, Point[] pacmanSpawns, Point[] ghostSpawns) {
        this.name = name;
        this.map = map;
        this.pacmanSpawns = pacmanSpawns;
        this.ghostSpawns = ghostSpawns;
        rnd = new Random();
    }

    /**
     * @return Level-Name
     */
    public String getName() {
        return name;
    }

    /**
     * Aendert den Level-Namen
     * @param value neuer Namen
     */
    public void setName(String value) {
        name = value;
    }

    /**
     * @return Breite des Levels
     */
    public int getWidth() {
        return map[0].length;
    }

    /**
     * @return Hoehe des Levels
     */
    public int getHeight() {
        return map.length;
    }

    /**
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return Modul an der uebergebenen Position
     */
    public MapModule getField(int x, int y) {
        return map[y][x];
    }

    @Deprecated
    public void setField(int x, int y, char c) {
        map[y][x] = MapModule.findByValue(c);
    }

    /**
     * Punkte ausserhalb des Spielfeldbereichs sind solid.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return true <-> Modul ist Wand oder Hintergrund
     */
    public boolean isSolid(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return true;
        return map[y][x] == MapModule.WALL || map[y][x] == MapModule.BACKGROUND;
    }

    /**
     * Punkte ausserhalb des Spielfeldbereichs sind keine Wand.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return true <-> Modul ist Wand
     */
    public boolean isWall(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return false;
        return map[y][x] == MapModule.WALL;
    }

    //TODO task 2a
    /**
     * Check whether a horizontal or vertical free path exists between p1 and p2.
     * @param p1, Position 1
     * @param p2, Position 2
     * @return true <-> Straight path without any obstacles
     */
    public boolean existsStraightPath(Point p1, Point p2) {
    	boolean straight = false; 
    	if (!isSolid(p1.x, p1.y) && !isSolid(p2.x, p2.y)
    			&& rangeValid(p1) && rangeValid(p2)) {
    		// both of input points are valid
    		if (p1.x != p2.x && p1.y != p2.y) {
    			// neither horizontal or vertical 
    			straight = false;
    		} else {
    			// horizontal check
    			if (p1.y == p2.y) {
    				straight = existsHorizontalPath(p1, p2);
    			}
    			// vertical check
    			if (p1.x == p2.x) {
    				straight = existsVerticalPath(p1, p2);
    			}
    		}
    	} else {
    		// invalid input points
    		straight = false;
    	}
    	return straight;
    }
    
    /**
     * Check free path in horizontal direction
     * @param p1, valid position 1
     * @param p2, valid position 2
     * @return true <-> horizontal straight path without any obstacles
     */
    public boolean existsHorizontalPath(Point p1, Point p2) {
    	int x_index_min = Math.min(p1.x, p2.x);
    	int x_index_max = Math.max(p1.x, p2.x);
    	for (int i = x_index_min; i <= x_index_max; i++) {
    		if (isWall(i, p1.y)) {
    			return false;
    		}
    	}
		return true;
    }
    
    /**
     * Check free path in vertical direction
     * @param p1, valid position 1
     * @param p2, valid position 2
     * @return true <-> vertical straight path without any obstacles
     */
    public boolean existsVerticalPath(Point p1, Point p2) {
    	int y_index_min = Math.min(p1.y, p2.y);
    	int y_index_max = Math.max(p1.y, p2.y);
    	for (int i = y_index_min; i <= y_index_max; i++) {
    		if (isWall(p1.x, i)) {
    			return false;
    		}
    	}
		return true;
    }
    
    /**
     * check out of boundary
     * @param p, input point
     * @return true <-> point is valid
     */
    public boolean rangeValid(Point p) {
    	if (p.x >= 0 && p.x < getWidth() && p.y >= 0 && p.y <getHeight()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    /**
     * @return zufaelliger PacmanSpawner-Punkt
     */
    public Point getRandomPacmanSpawn() {
        return pacmanSpawns[rnd.nextInt(pacmanSpawns.length)];
    }

    /**
     * @return zufaelliger GeisterSpawner-Punkt
     */
    public Point getRandomGhostSpawn() {
        return ghostSpawns[rnd.nextInt(ghostSpawns.length)];
    }

    /**
     * @return naechster GeisterSpawner-Punkt
     */
    public Point getNextGhostSpawn() {
        return ghostSpawns[currentGhostSpawnCounter++ % ghostSpawns.length];
    }

    /**
     * @return eine zufaellige Position, die ein Dot oder Freiraum ist
     */
    public Point getRandomSpaceField() {
        // alle begehbaren felder auflisten
        ArrayList<Point> fields = new ArrayList<>();

        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                if (map[y][x] == MapModule.DOT || map[y][x] == MapModule.SPACE)
                    fields.add(new Point(x, y));

        // zufaelliges element der liste ausgeben
        return fields.get(rnd.nextInt(fields.size()));
    }

    /**
     * @return true <-> mindestens ein Dot wurde platziert
     */
    private boolean hasDot() {
        for (int x = 0; x < getWidth(); x++)
            for (int y = 0; y < getHeight(); y++)
                if (map[y][x] == MapModule.DOT)
                    return true;
        return false;
    }

    /**
     * Ueberprueft, ob der Level-Grid korrekt aufgebaut ist.
     *
     * @throws NoDotsException falls keine Dots platziert wurden
     * @throws ReachabilityException falls nicht erreichbare Dots existieren
     * @throws NoPacmanSpawnPointException falls keine PacmanSpawner existieren
     * @throws NoGhostSpawnPointException falls keine GeisterSpawner existieren
     */
    public void validate() throws ReachabilityException, NoPacmanSpawnPointException, NoGhostSpawnPointException, NoDotsException {
        // anzahl spawner pruefen
        if (pacmanSpawns.length == 0) throw new NoPacmanSpawnPointException();
        if (ghostSpawns.length == 0)  throw new NoGhostSpawnPointException();

        // punkte pruefen
        if (!hasDot()) throw new NoDotsException();

        reachability();
    }

    //TODO task 2b
    /**
     * check whether the loaded level is valid
     * @throws ReachabilityException
     */
    private void reachability() throws ReachabilityException {
    	// initialize boolean array corresponding to level (as false)
    	boolean[][] valids = new boolean[getHeight()][getWidth()];
    	for (int i = 0; i < getHeight(); i++) {
    		for (int j = 0; j < getWidth(); j++) {
    			valids[i][j] = isSolid(j, i); // swap i and j
    		}
    	}
    	
    	ArrayList<Point> trace = new ArrayList<>();
    	trace.add(pacmanSpawns[0]);
    	while (!trace.isEmpty()) {
    		Point p = trace.remove(trace.size() - 1);
    		valids[p.y][p.x] = true;
    		
    		Point[] geighbours = getBranches(p);
    		for (Point q : geighbours) {
    			if (rangeValid(q)) {
    				if(!valids[q.y][q.x]) {
    					trace.add(q);
    				}
    			}
    		}
    	}
    	
    	for (int i = 0; i < getHeight(); i++) {
    		for (int j = 0; j < getWidth(); j++) {
    			if (getField(j, i) == MapModule.DOT && !valids[i][j]) 
    				throw new ReachabilityException(getName());
    		}
    	}
    }

    /**
     * {@inheritDoc}
     * Gibt den Level-Grid anhand der Modul-Zeichen als String zurueck.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++)
                sb.append(map[y][x].getValue());
            if (y < (getHeight() - 1))
                sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * @param p zu untersuchende Position
     * @return alle direkten Nachbarn, die keine Wand sind
     */
    public Point[] getBranches(Point p) {
        ArrayList<Point> branches = new ArrayList<>();
        if (p.x >= 0 && !isWall(p.x - 1, p.y))
            branches.add(new Point(p.x - 1, p.y));
        if (p.y >= 0 && !isWall(p.x, p.y - 1))
            branches.add(new Point(p.x, p.y - 1));
        if (p.x <= (getWidth() - 1) && !isWall(p.x + 1, p.y))
            branches.add(new Point(p.x + 1, p.y));
        if (p.y <= (getHeight() - 1) && !isWall(p.x, p.y + 1))
            branches.add(new Point(p.x, p.y + 1));
        return branches.toArray(new Point[0]);
    }

    /**
     * @return alle Levelnamen in dem Levelordner
     */
    public static String[] listLevelFiles() {
        File f = new File(Consts.LEVEL_FOLDER);
        return f.list();
    }
}
