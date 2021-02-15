package tud.ai2.pacman.model;

import tud.ai2.pacman.util.Consts;

/**
 * Modelliert einen einzelnen Highscore-Eintrag.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Kurt Cieslinski
 */
public class HighscoreEntry implements Comparable<HighscoreEntry> {
    /** Nickname des Eintrags */
    private final String name;
    /** Punktzahl des Eintrags */
    private final int points;

    /**
     * Konstruktor.
     *
     * @param line bestimmt formatierte String-Repraesentation
     */
    public HighscoreEntry(String line) {
        if (line == null) throw new IllegalArgumentException();

        String[] parts = line.split(Consts.HS_DELIMITER, 2);
        if (parts.length < 2) throw new IllegalArgumentException();

        points = Integer.parseInt(parts[0]);
        name = parts[1];
    }

    /**
     * Konstruktor.
     *
     * @param n Nickname
     * @param p Punktzahl
     */
    public HighscoreEntry(String n, int p) {
        name = n;
        points = p;
    }

    /**
     * @return Nickname des Eintrags
     */
    public String getName() {
        return name;
    }

    /**
     * @return Punktzahl des Eintrags
     */
    public int getPoints() {
        return points;
    }

    /**
     * {@inheritDoc}
     * Vergleicht die Eintraege anhand der Punktzahl.
     */
    public int compareTo(HighscoreEntry hscore) {
		return Integer.compare(hscore.getPoints(), this.getPoints());
	}

}
