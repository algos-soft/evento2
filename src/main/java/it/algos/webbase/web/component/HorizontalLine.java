package it.algos.webbase.web.component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * HTML horizontal line.
 */
public class HorizontalLine extends Label {
    public HorizontalLine() {
        super("<hr/>");
        setContentMode(ContentMode.HTML);
    }
}
