package tud.ai2.pacman.util;


import org.newdawn.slick.Color;

/**
 * Global definierte Konstanten des gesamten Spiels.
 * 
 * @author Maximilian Kratz
 * @author Robert Cieslinski
 * @version 2020-05-13
 */
public abstract class Consts {

  /**
   * Name des Spiels. Dieser wird in der Leiste des Fensters angezeigt.
   */
  public static final String GAME_NAME = "Pacman";
  public static boolean TEST = false;
  public static final long TEST_TIME = 42L;

  /*
   * Betriebssystem-Konstanten. Diese werden dafuer benoetigt, dass das Spiel unter Windows, Linux
   * und macOS lauffaehig ist. Fuer jedes Betriebssystem werden unterschiedliche Bibliotheken aus
   * diesen Ordnern geladen, wenn das Spiel gestartet wird.
   */
  public static final String WINDOWS_LIB_PATH = "org.lwjgl.librarypath";
  public static final String WINDOWS_USER_DIR = "/native/windows";
  public static final String MAC_LIB_PATH = "org.lwjgl.librarypath";
  public static final String MAC_USER_DIR = "/native/macosx";
  public static final String LINUX_LIB_PATH = "org.lwjgl.librarypath";
  public static final String LINUX_USER_DIR = "/native/";
  public static final String OS_NAME = "os.name";
  public static final String USER_DIR = "user.dir";
  public static final String WINDOWS_OS_NAME = "windows";
  public static final String MAC_OS_NAME = "mac";


  /** Normale Breite der grafischen Oberflaeche. */
  public static final int WINDOW_WIDTH = 700;

  /** Normale Hoehe der grafischen Oberflaeche. */
  public static final int WINDOW_HEIGHT = 440;

  /*
   * States des Spiels und deren Zuordnung zu ints.
   */
  public static final int MENU_STATE = 0;
  public static final int GAME_STATE = 1;
  public static final int LOST_STATE = 2;
  public static final int HIGHSCORE_STATE = 4;


  /*
   * GUI Konstanten
   */
  public static final int INFO_BAR_HEIGHT = 50;
  public static final Color INFO_BAR_COLOR = new Color(255, 255, 255);
  public static final int LABELS_TOP = 20;
  public static final int LABEL_LIFES_LEFT = 20;
  public static final int LABEL_POINTS_LEFT = 290;
  public static final int LABEL_LEVEL_LEFT = 480;

  /*
   * Spiel Konstanten
   */
  public final static int DOT_POINTS = 10;
  public final static int POWER_UP_POINTS = 50;
  public final static int SPEED_UP_POINTS = 30;
  public final static int GHOST_POINTS = 100;
  public final static int INITIAL_LIVES = 3;
  public final static boolean BUG_256 = true;

  /*
   * Konstanten fuer Pacman
   */
  public static final float P_MOVE_SPEED = 4f;
  public static final float P_POWER_UP_MOVE_SPEED = 5f;
  public static final float P_SPEED_UP_MOVE_SPEED = 6f;
  public static final long P_POWER_UP_TIME = 1000000000L * 4; // 4 sekunden power-zeit
  public static final long P_SPEED_UP_TIME = 1000000000L * 4; // 4 sekunden speed-zeit
  public static final Color P_DEFAULT_BLEND_COLOR = new Color(255, 204, 0);
  public static final Color P_POWER_UP_BLEND_COLOR = new Color(128, 100, 128);
  public static final Color P_SPEED_UP_BLEND_COLOR = new Color(255, 100, 0);

  /*
   * Konstanten fuer die Geister
   */
  public static final int NUM_GHOSTS = 4;
  public static final float G_MOVE_SPEED = 3f;
  public static final float G_IDLE_MOVE_SPEED = 1.5f;
  public static final long G_RESPAWN_IDLE_TIME = 2000000000L + 50000000L; // 2.5 sekunden
  public static final float G_IDLE_MOVE_SIZE = 0.15f;

  /*
   * Level-Begrenzungen (Minima und Maxima) beim LevelGenerator.
   * Dies sind keine absoluten Groessen, sondern Grenzen fuer Faktor k
   * einer ungeraden ganzen Zahl    (k * 2) + 1.
   */
  public static final int WIDTH_MIN = 7;
  public static final int WIDTH_MAX = 11;
  public static final int HEIGHT_MIN = 5;
  public static final int HEIGHT_MAX = 7;

  /*
   * Ordner-Pfade fuer die Gamestates und die Menue-Assets. Aus diesen Ordnern werden z. B. Bilder
   * geladen.
   */
  public static final String ASSETS_FOLDER = "assets";
  public static final String THEME_FOLDER = ASSETS_FOLDER + "/pictures/";
  public static final String LEVEL_FOLDER = ASSETS_FOLDER + "/levels/";
  public static final String FONT_FOLDER = ASSETS_FOLDER + "/fonts/";
  public static final String SAVE_FILE = "autosave";
  public static final String IMAGE_EXTENSION = ".png";

  /** Aktuell gewaehltes Thema */
  public static final String CURRENT_THEME = "theme1";

  /*
   * Highscore Einstellungen.
   */
  public static final String HIGHSCORE_FILE = "highscore.txt";
  public static final int HIGHSCORE_DISPLAYED_ENTRIES = 5;
  /** Delimiter zwischen Name und Punkte */
  public static final String HS_DELIMITER = ",";
  public static final int HS_MAX_NAME_LENGTH = 4;

  /*
   * Button Konstanten
   */
  public static final Color BUTTON_DEFAULT_COLOR = Color.white;
  public static final Color BUTTON_HOVER_COLOR = Color.yellow;
  public static final Color BUTTON_DISABLED_COLOR = Color.gray;

}
