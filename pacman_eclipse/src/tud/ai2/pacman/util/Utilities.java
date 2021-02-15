package tud.ai2.pacman.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

/**
 * Global verfuegbare Hilfsmethoden, die im gesamten Spiel verwendet werden koennen.
 * 
 * @author Maximilian Kratz
 * @author Niklas Vogel
 * @author Phil Reize
 * @version 2020-05-13
 */
public class Utilities {

  /**
   * Diese Methode wandelt die Sekunden in Minuten um.
   *
   * @param l Zeit in Sekunden.
   * @return Uhrzeit als ein String in einem Format "h:m".
   */
  public static String secInMin(final long l) {
    long min = l / 60;
    min = min % 60;
    long rest = l % 60;
    String m = "00";
    String s = "00";

    if (min > 0 && min < 10) {
      m = "0" + min;
    }

    if (min >= 10) {
      m = min + "";
    }

    if (rest > 0 && rest < 10) {
      s = "0" + rest;
    }

    if (rest >= 10) {
      s = "" + rest;
    }

    return m + ":" + s;
  }

  /**
   * Diese Methode macht den String der Schwierigkeit schoener.
   * 
   * @param diff Die Schwierigkeit als String.
   * @return Die schoener formatierte Schwierigkeit.
   */
  public static String diffNice(final String diff) {
    if (diff == null) {
      throw new IllegalArgumentException("Uebergebener String ist null.");
    }

    if (diff.isEmpty()) {
      throw new IllegalArgumentException("Uebergebener String ist leer.");
    }

    if (!diff.contains(".")) {
      throw new IllegalArgumentException("Uebergebener String enthaelt keinen Punkt.");
    }

    String[] diffString = diff.split("\\.");

    if (diffString[0].length() == 1) {
      diffString[0] = "0" + diffString[0];
    }

    if (diffString[1].length() == 1) {
      diffString[1] += "0";
    }

    return diffString[0] + "." + diffString[1];
  }

  /**
   * Gibt einen Vektor mit dem Mittelpunkt eines gegebenen {@link GameContainer} zurueck.
   * 
   * @param gameContainer {@link GameContainer}, fuer welchen der Mittelpunkt bestimmt werden soll.
   * @return {@link Vector2f} mit Mittelpunkt des {@link GameContainer}.
   */
  public static Vector2f getCenterPoint(final GameContainer gameContainer) {
    if (gameContainer == null) {
      throw new IllegalArgumentException("Uebergebener GameContainer ist null.");
    }

    return new Vector2f(gameContainer.getWidth() / 2, gameContainer.getHeight() / 2);
  }

}
