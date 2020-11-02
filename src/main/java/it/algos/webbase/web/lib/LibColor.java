package it.algos.webbase.web.lib;

import com.vaadin.shared.ui.colorpicker.Color;

/**
 * Created by alex on 8-04-2016.
 */
public class LibColor {

    /**
     * Finds the right foreground color (black or white) for a given background color
     *
     * @param background the background color
     * @return the right foreground color
     */

    public static Color getForeground(Color background) {
        Color foregroundColor = Color.BLACK;

        int br = background.getRed();
        int bg = background.getGreen();
        int bb = background.getBlue();

        if (0.299 * br + 0.587 * bg + 0.114 * bb <= 128) {

            // Background is dark, so foreground should be light
            foregroundColor = Color.WHITE;

        }

        return foregroundColor;
    }

}
