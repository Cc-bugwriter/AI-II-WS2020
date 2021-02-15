package tud.ai2.pacman.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Klasse zum Lesen und Schreiben von Dateien. Dies wird insbesondere zum Lesen und Schreiben der
 * Highscore-Datei und zum Lesen der Level genutzt.
 *
 * @author Maximilian Kratz
 * @version 2019-06-04
 */
public class FileOperations {

  /**
   * Diese Methode wird dazu genutzt um eine Datei auszulesen.
   *
   * @param fileName Dateiname, welcher in der Form eines Strings uebergeben wird.
   * @return Den String des Inhalts der uebergebenen Datei, sofern diese gefunden werden kann,
   *         ansonsten kommt ein leerer String zurueck.
   */
  public static String readFile(final String fileName) {
    if (fileName == null) {
      throw new IllegalArgumentException("Der uebergebene Dateiname ist null.");
    }

    if (fileName.isEmpty()) {
      throw new IllegalArgumentException("Der uebergebene Dateiname ist leer.");
    }

    StringBuffer sb = new StringBuffer();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line + System.lineSeparator());
      }
    } catch (final FileNotFoundException e) {
      System.err.println(
          String.format("FileNotFoundException: %s konnte nicht gefunden werden!", fileName));
    } catch (final IOException e) {
      e.printStackTrace();
      System.err.println("IOException: Allgemeiner Lesefehler in der Datei: " + fileName);
    }

    return sb.toString();
  }

  /**
   * Allgemein kann die Methode einen beliebigen String in einer Datei speichern. Diese Methode wird
   * dazu genutzt um den Highscore in einer Datei zu speichern.
   *
   * @param fileName Ein in einem String uebergebener Dateiname. Bei einem Pfad muss der zugehoerige
   *        Pfad bereits existieren.
   * @param data Der Inhalt, welcher in die angegebene Datei geschrieben werden soll.
   * @return true, wenn die Datei erstellt bzw. in sie geschrieben werden konnte, sonst false.
   */
  public static boolean writeFile(final String fileName, final String data) {
    if (fileName == null) {
      throw new IllegalArgumentException("Der uebergebene Dateiname ist null.");
    }

    if (fileName.isEmpty()) {
      throw new IllegalArgumentException("Der uebergebene Dateiname ist leer.");
    }

    if (data == null) {
      throw new IllegalArgumentException("Die uebergebenen Daten sind null.");
    }

    if (data.isEmpty()) {
      throw new IllegalArgumentException("Die uebergebenen Daten sind leer.");
    }

    try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8)) {
      bw.write(data);
    } catch (final IOException e) {
      e.printStackTrace();
      System.err.println("IOException: Datei konnte nicht erstellt werden: " + fileName);
      return false;
    }

    return true;
  }

}
