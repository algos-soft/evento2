package it.algos.webbase.web.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * Created by gac on 04 ott 2016.
 */
public class AHorizontalLayout extends HorizontalLayout {

    public AHorizontalLayout() {
    }

    public AHorizontalLayout(Component... children) {
        this();
        this.setSpacing(true);
        this.addComponents(children);
    }

}
