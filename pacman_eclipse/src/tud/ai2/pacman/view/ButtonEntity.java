package tud.ai2.pacman.view;

import eea.engine.action.Action;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.OREvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import tud.ai2.pacman.util.Consts;

/**
 * Ein Text-Knopf, der beim Maus-Highlight die Farbe aendert.
 *
 * @author Simon Breitfelder
 * @author Dominik Puellen
 * @author Robert Cieslinski
 */
public class ButtonEntity extends Entity {
    /** Zu verwendender Font */
    private final Font font;
    /** Wann die gewaehlte Aktion ausgefuehrt werden soll */
    private final Event clickEvent;

    /** aktuelle Farbe des Buttons */
    private Color color;
    /** ob die Aktion hinter dem Button ausfuehrbar ist */
    private boolean enabled = false;

    /**
     * Konstruktor.
     *
     * @param buttonTitle angezeigter Text
     * @param onClick Aktion hinter dem Button
     * @param font zu verwendender Font
     */
    public ButtonEntity(String buttonTitle, Action onClick, Font font) {
        this(buttonTitle, onClick, font, null);
    }

    /**
     * Konstruktor.
     *
     * @param buttonTitle angezeigter Text
     * @param onClick Aktion hinter dem Button
     * @param font zu verwendender Font
     * @param buttons Keyboard-Knoepfe, die diese Aktion ebenfalls ausloesen koennen
     */
    public ButtonEntity(String buttonTitle, Action onClick, Font font, Integer[] buttons) {
        // buttonTitle als id verwenden
        super(buttonTitle);
        // buttongroesse berechnen
        this.font = font;
        color = Consts.BUTTON_DEFAULT_COLOR;
        super.setSize(new Vector2f(font.getWidth(buttonTitle), font.getLineHeight()));

        // click-event erstellen
        if (buttons != null && buttons.length > 0)
            clickEvent = new OREvent(new KeyPressedEvent(buttons), new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent()));
        else
            clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());

        clickEvent.addAction(onClick);
        enable();
    }

    /**
     * Aktiviere den Button.
     */
    public void enable() {
        if (!enabled) {
            super.addComponent(clickEvent);
            enabled = true;
        }
    }

    /**
     * Deaktiviere den Button.
     */
    public void disable() {
        if (enabled) {
            super.removeComponent(clickEvent);
            enabled = false;
        }
    }

    /**
     * @return true <=> Button ist aktiviert
     */
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        super.render(container, game, g);

        // g.drawString(super.getID(), super.getPosition().x - super.getSize().x / 2f, super.getPosition().y - super.getSize().y / 2f);
        font.drawString(super.getPosition().x - super.getSize().x / 2f, super.getPosition().y - super.getSize().y / 2f, super.getID(), color);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        super.update(container, game, delta);
        // farbe aendern, wenn die maus auf den button zeigt
        Input input = container.getInput();
        color = (enabled ? (containsPoint(new Vector2f(input.getMouseX(), input.getMouseY())) ? Consts.BUTTON_HOVER_COLOR : Consts.BUTTON_DEFAULT_COLOR) : Consts.BUTTON_DISABLED_COLOR);
    }

    /**
     * @param p zu pruefende Position
     * @return true <=> Position ist innerhalb des Buttons
     */
    private boolean containsPoint(Vector2f p) {
        Vector2f pos = super.getPosition();
        Vector2f size = super.getSize();
        return (p.x >= (pos.x - size.x / 2f) && p.y >= (pos.y - size.y / 2f) && p.x <= (pos.x + size.x / 2f) && p.y <= (pos.y + size.y / 2f));
    }

}
