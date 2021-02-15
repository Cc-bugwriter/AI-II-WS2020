package tud.ai2.pacman.model;

import tud.ai2.pacman.util.Consts;
import tud.ai2.pacman.util.FileOperations;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Speichert den Spiel Highscore.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Kurt Cieslinski
 */
public class Highscore {
    /** Highscore-Eintraege */
    private final ArrayList<HighscoreEntry> entries;
    /** Singleton Instanz */
    private static Highscore hs;

    /**
     * @return die Singleton-Instanz
     */
    public static Highscore getInstance() {
        if (hs == null) hs = new Highscore();
        return hs;
    }

    /**
     * Konstruktor. Privat!
     * Liest alle Highscores der Datei ein.
     */
    private Highscore() {
        this.entries = new ArrayList<>();

        if (!new File(Consts.HIGHSCORE_FILE).isFile()) return;

        String[] lines = FileOperations.readFile(Consts.HIGHSCORE_FILE).split(System.lineSeparator());
        for (String line : lines) {
            if (entries.size() < Consts.HIGHSCORE_DISPLAYED_ENTRIES)
                entries.add(new HighscoreEntry(line));
        }
        sortHighscores();
    }

    /**
     * Fuegt den Eintrag hinzu. Updatet auch die Datei, sodass
     * maximal eine bestimmte Anzahl an Eintraegen vorhanden sind.
     * Die Liste wird sortiert.
     *
     * @param he der neue Highscore Eintrag
     */
    public void addHighscore(HighscoreEntry he) {
        entries.add(he);
        sortHighscores();
        if (entries.size() > Consts.HIGHSCORE_DISPLAYED_ENTRIES)
            entries.remove(entries.size() - 1);
        saveHighscores();
    }

    /**
     * @return die minimal abgespeicherte Punktzahl,
     *          bzw. 0, falls keine vorhanden sind
     */
    private int getMinHighscore() {
        sortHighscores();
        if (entries.isEmpty()) return 0;

        return entries.get(entries.size() - 1).getPoints();
    }

    /**
     * Sortiere alle Eintraege anhand der Punktzahl.
     */
    private void sortHighscores() {
        Collections.sort(entries);
    }

    /**
     * @return alle abgespeicherte Highscores
     */
    public List<HighscoreEntry> getAllEntries() {
        return entries;
    }

    /**
     * @param points zu ueberpruefende Punktzahl
     * @return true <=> Punktzahl kann in den Highscore aufgenommen werden
     */
    public boolean checkNewEntry(int points) {
        return (entries.size() < Consts.HIGHSCORE_DISPLAYED_ENTRIES) || (points > getMinHighscore());
    }

    /**
     * Speichert die Highscore-Eintraege in der passenden Datei ab.
     */
    private void saveHighscores() {
        StringBuilder sb = new StringBuilder();
        for (HighscoreEntry he : entries)
            sb.append(he.getPoints()).append(Consts.HS_DELIMITER).append(he.getName()).append(System.lineSeparator());

        FileOperations.writeFile(Consts.HIGHSCORE_FILE, sb.toString());
    }
}
