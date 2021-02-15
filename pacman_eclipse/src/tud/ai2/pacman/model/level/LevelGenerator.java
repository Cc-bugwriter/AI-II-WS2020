package tud.ai2.pacman.model.level;

import tud.ai2.pacman.util.Consts;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Generiert einen zufaelligen Level.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 * @author Kurt Cieslinski
 */
public class LevelGenerator {
    /** Ein Random-Generator */
    private final Random rnd;

    /**
     * WARNUNG
     * Nicht manuell veraendern! Diese beiden Variablen werden bereits zufaellig initialisiert.
     *
     * Zeigen Hoehe und Breite des generierten Levels an
     */
    private int width, height;
    /** Die aktuelle Anzahl von Dots */
    private int free;
    /** aktueller Level-Grid */
    private MapModule[][] map;
    /** die aktuellen Pacman-Spawner */
    private List<Point> pacmanSpawns;
    /** die aktuellen Geister-Spawner */
    private List<Point> ghostSpawns;

    /**
     * Konstruktor.
     */
    public LevelGenerator() {
        rnd = new Random();
    }

    /**
     * Aktualisiert einen Level-Baustein.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param m neues Modul
     */
    private void setMapAt(int x, int y, MapModule m) {
        map[y][x] = m;
    }

    /**
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return Level-Modul an der gewaehlten Position
     */
    private MapModule getMapAt(int x, int y) {
        return map[y][x];
    }

    /**
     * @param min minimale Zahl
     * @param max maximale Zahl
     * @return eine Zufahlszahl zwischen min und max
     */
    private int rndInt(int min, int max) {
        if (min >= max) return min;
        return rnd.nextInt(max - min) + min;
    }

    /**
     *
     *
     * @return einen zufaellig generierten Level
     */
    public Level generateLevel() {
        // levelabmessungen zufaellig bestimmen (nur ungerade zahlen)
        width = rndInt(Consts.WIDTH_MIN, Consts.WIDTH_MAX) * 2 + 1;
        height = rndInt(Consts.HEIGHT_MIN, Consts.HEIGHT_MAX) * 2 + 1;
        free = 0;

        // level mit waenden initialisieren
        map = new MapModule[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                setMapAt(x, y, MapModule.WALL);

        pacmanSpawns = new ArrayList<>();
        ghostSpawns = new ArrayList<>();

        // landschaft generieren
        genMaze();
        removeDeadEnds();
        for (int i = 0; i < rndInt(1, free / 15); i++)
            genOpenBorder();

        // spawner platzieren
        for (int i = 0; i < rndInt(1, free / 18); i++)
            genPacmanSpawner();
        for (int i = 0; i < 4; i++)
            genGhostSpawner();

        // items platzieren
        for (int i = 0; i < free / 15; i++)
            genRandomModule(MapModule.POWERUP);
        for (int i = 0; i < rndInt(1, free / 18); i++)
            genRandomModule(MapModule.SPEEDUP);
        if (rnd.nextInt(3) == 0)
            for (int i = 0; i < rndInt(1, free / 20); i++)
                genRandomModule(MapModule.TELEPORT);

        return new Level("Random Level", map, pacmanSpawns.toArray(new Point[0]), ghostSpawns.toArray(new Point[0]));
    }

    /**
     * Rekursions-Initialisierung.
     * Starte mit einem zufaelligen Punkt.
     */
    private void genMaze() {
        genMaze(new Point(rnd.nextInt((width - 1) / 2) * 2 + 1, rnd.nextInt((height - 1) / 2) * 2 + 1));
    }


    /**
     * @param p der Startpunkt der Pfadgeneration
     */
    // TODO task 3b
    private void genMaze(Point p) {
    	if(getMapAt(p.x, p.y) != MapModule.DOT) {
    		setMapAt(p.x, p.y, MapModule.DOT);
    		free++;
    	}
    	
    	List<Point> neighbours = get2Neighbours(p);
    	if (neighbours.isEmpty()) {
    		return;
    	} else {
    		Point neighbour = neighbours.get(rnd.nextInt(neighbours.size()));
    		setMapAt((p.x + neighbour.x)/2, (p.y + neighbour.y)/2, MapModule.DOT);
    		free++;
    		
    		// Free up memory space
    		neighbours = new LinkedList<>();
    		
    		// recursive steps
    		genMaze(p);
    		genMaze(neighbour);
    	}
    }

    /**
     * Gibt alle Punkte mit gerader Entfernung 2 zum Ursprungspunkt zurueck.
     * Herausgefiltert werden Punkte, die am Rand liegen
     * oder mehr als einen Dot als direkten Nachbarn besitzen.
     *
     * @param p Ursprungspunkt
     */
    private List<Point> get2Neighbours(Point p) {
        List<Point> neighbours = new LinkedList<>();

        if (getMapAt(p.x - 1, p.y) != MapModule.DOT)
            neighbours.add(new Point(p.x - 2, p.y));
        if (getMapAt(p.x + 1, p.y) != MapModule.DOT)
            neighbours.add(new Point(p.x + 2, p.y));
        if (getMapAt(p.x, p.y - 1) != MapModule.DOT)
            neighbours.add(new Point(p.x, p.y - 2));
        if (getMapAt(p.x, p.y + 1) != MapModule.DOT)
            neighbours.add(new Point(p.x, p.y + 2));

        neighbours.removeIf(point -> !isTarget(point));

        return neighbours;
    }

    /**
     * @param p ein Punkt
     * @return true <-> nicht am Rand und weniger als einem Dot als Nachbar
     */
    private boolean isTarget(Point p) {
        // Punkte direkt am Rand werden hier noch nicht betrachtet
        if (p.x < 1 || p.x >= (width - 1) || p.y < 1 || p.y >= (height - 1)) return false;

        // Punkte mit mehr als einem Dot als direkte Nachbarn werden nicht betrachtet
        int sum = 0;
        for (int y = (p.y - 1); y <= (p.y + 1); y++)
            for (int x = (p.x - 1); x <= (p.x + 1); x++)
                if (getMapAt(x, y) == MapModule.DOT) sum++;
        return sum <= 1;
    }

    // TODO task 3c
    /**
     * swap a wall of DeadEnds to Dot
     */
    private void removeDeadEnds() {
    	for (int y = 2; y < height - 2; y++) {
    		for(int x = 2; x < width - 2; x++) {
    			List<Point> walls = getSurroundingWalls(x, y);
    			if (getMapAt(x, y) != MapModule.WALL && walls.size() > 2) {
    				// random select a wall
    				Point wall = walls.get(rnd.nextInt(walls.size()));
    				setMapAt(wall.x, wall.y, MapModule.DOT);
    			}
    		}
    	}
    }

    /**
     * Gibt alle Koordinaten der umgebenen Waende zurueck, wenn
     * diese NICHT am Rand des Levels liegen.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return alle Koordinaten der umgebenden Waende
     */
    private List<Point> getSurroundingWalls(int x, int y) {
        List<Point> walls = new ArrayList<>();

        if (x > 1 && getMapAt(x - 1, y) == MapModule.WALL)
            walls.add(new Point(x - 1, y));
        if (x < (width - 2) && getMapAt(x + 1, y) == MapModule.WALL)
            walls.add(new Point(x + 1, y));
        if (y > 1 && getMapAt(x, y - 1) == MapModule.WALL)
            walls.add(new Point(x, y - 1));
        if (y < (height - 2) && getMapAt(x, y + 1) == MapModule.WALL)
            walls.add(new Point(x, y + 1));

        return walls;
    }

    /**
     * Generiert einen zufaelligen Pacman-Spawner.
     * Dieser muss am weitesten von anderen Pacman-Spawnern entfernt sein.
     */
    private void genPacmanSpawner() {
        // platziert einen pacman spawner
        Point p;

        if (pacmanSpawns.size() == 0) {
            // ersten spawner zufaellig platzieren
            do {
                p = new Point(rnd.nextInt(width), rnd.nextInt(height));
            } while (getMapAt(p.x, p.y) != MapModule.DOT);

        } else p = furthestFromPacSpawn();

        if (p != null) {
            pacmanSpawns.add(p);
            setMapAt(p.x, p.y, MapModule.PLAYER_SPAWN);
        }
    }

    /**
     * Generiert einen zufaelligen Geister-Spawner.
     * Dieser muss am weitesten von Pacman-Spawnern entfernt sein.
     */
    private void genGhostSpawner() {
        // platziert einen geist spawner
        Point p = furthestFromPacSpawn();

        if (p != null) {
            ghostSpawns.add(p);
            setMapAt(p.x, p.y, MapModule.GHOST_SPAWN);
        }
    }

    /**
     * Generiert eine zufaellige WrapArround-Verbindung am Rand.
     * Diese ist horizontal oder vertikal.
     */
    private void genOpenBorder() {
        if (rnd.nextInt(2) == 0) {
            // vertikale verbindung
            int x = rnd.nextInt((width - 1) / 2) * 2 + 1;
            setMapAt(x, 0, MapModule.DOT);
            setMapAt(x, height - 1, MapModule.DOT);
        } else {
            // horizontale verbindung
            int y = rnd.nextInt((height - 1) / 2) * 2 + 1;
            setMapAt(0, y, MapModule.DOT);
            setMapAt(width - 1, y, MapModule.DOT);
        }
    }

    // TODO task 3a
    /**
     * recursive run, until find a dot to replace appointed MapModule
     * @param m appointed Module
     */
    private void genRandomModule(MapModule m) {
    	int x = rnd.nextInt(width);
    	int y = rnd.nextInt(height);
    	if (getMapAt(x, y) == MapModule.DOT) {
    		setMapAt(x, y, m);
    		return;
    	} else {
    		genRandomModule(m);
    	}
    }

    // TODO task 3d
    /**
     * find the farthest point to PacSpawns
     * @return
     */
    private Point furthestFromPacSpawn() {
    	Point furthest = null;
    	double dist = 0.0, dist_max = 0.0;
    	
    	if (!pacmanSpawns.isEmpty()) {
			for (int y = 0; y < height; y++) {
	    		for(int x = 0; x < width; x++) {
	    			Point tmp = new Point(x, y);
	    			dist = distanceFromPacSpawn(tmp);
	    			if (dist_max == 0.0 || dist > dist_max) {
	    				dist_max = dist;
	    				furthest = tmp;
	    			}
	    		}
	    	}
		}
    	return furthest;    	
    }
    
    /**
     * compute the minimal straight line distance for all PacSpawn
     * @param p, interest Point
     * @return minimal distance
     */
    private double distanceFromPacSpawn(Point p) {
    	double dist = 0.0, dist_min = 0.0;
    	for (Point pacmanSpawn: pacmanSpawns) {
    		dist = Math.sqrt(Math.pow(pacmanSpawn.x - p.x, 2) + Math.pow(pacmanSpawn.y - p.y, 2));
    		if (dist_min == 0.0 || dist < dist_min) {
    			dist_min = dist;
    		}
    	}
    	return dist_min;
    }

}
