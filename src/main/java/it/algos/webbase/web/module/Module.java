package it.algos.webbase.web.module;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.navigator.AlgosNavigator;

@SuppressWarnings("serial")
public abstract class Module extends CustomComponent implements View {

    public Module() {
        super();
        init(); // early initialization method
        setSizeFull();// default sizing
    }// end of constructor

    /**
     * Perform here any early initialization of the module.
     * This method is called before calling the constructor
     * of any intermediate superclass.
     */
    protected void init(){
    }



	/**
	 * Crea una nuova istanza (test alex)
	 */
	public static Module getInstanceNew(Class clazz) {
		Module istanza = null;

		try {
			istanza = (Module) clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return istanza;
	}

    @Override
    // called when a Navigator enters this view
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}// end of class
