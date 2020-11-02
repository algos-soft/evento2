package it.algos.webbase.web.navigator;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SingleComponentContainer;


/**
 * A Placeholder component which a Navigator can populate with different views
 */
@SuppressWarnings("serial")
public class NavPlaceholder extends CustomComponent implements SingleComponentContainer {

    public NavPlaceholder(){
        this(null);
    }

    public NavPlaceholder(Component content) {
        super();
        setContent(content);
        setSizeFull();
    }

    @Override
    public void addComponentAttachListener(ComponentAttachListener listener) {}

    @Override
    public void removeComponentAttachListener(ComponentAttachListener listener) {}

    @Override
    public void addComponentDetachListener(ComponentDetachListener listener) {}

    @Override
    public void removeComponentDetachListener(ComponentDetachListener listener) {}

    @Override
    public Component getContent() {
        return getCompositionRoot();
    }

    @Override
    public void setContent(Component content) {
        setCompositionRoot(content);
    }


}
