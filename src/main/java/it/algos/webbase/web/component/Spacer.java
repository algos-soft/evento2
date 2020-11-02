package it.algos.webbase.web.component;

import com.vaadin.ui.Label;

/**
 * An empty spacing component
 */
@SuppressWarnings("serial")
public class Spacer extends Label {

    /**
     * Create a generic Spacer.
     *
     * @param width  the width
     * @param heigth the height
     * @param unit   the unit (com.vaadin.server.Sizeable.Unit)
     */
    public Spacer(int width, int heigth, Unit unit) {
        super();
        setWidth(width, unit);
        setHeight(heigth, unit);
    }

    /**
     * Create a square Spacer with size in a given unit.
     *
     * @param size the size in EM (same for width & height)
     * @param unit the unit (com.vaadin.server.Sizeable.Unit)
     */
    public Spacer(int size, Unit unit) {
        this(size, size, unit);
    }

    /**
     * Create a square Spacer.
     *
     * @param size the size in EM
     */
    public Spacer(int size) {
        this(size, Unit.EM);
    }

    /**
     * Create a default Spacer, 1em for width & height.
     */
    public Spacer() {
        this(1);
    }


}
