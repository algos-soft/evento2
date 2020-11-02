package it.algos.webbase.web.navigator;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;

/**
 * Provides the views for the Navigator.
 * The views can be instantiated each time they are requested by the Navigator
 * or can be created the first time and cached.
 * Use the cache flag to set this behaviour.
 * <p>
 * Created by alex on 21/12/15.
 */
public class CachingViewProvider extends Navigator.ClassBasedViewProvider {

    private View view;
    boolean caching;

    /**
     * Constructor.
     *
     * @param viewName  the view name for the Navigator
     * @param viewClass the View class to instantiate
     * @param caching   true to create the View the first time and cache it,
     *                  false to create the View each time is requested
     */
    public CachingViewProvider(String viewName, Class<? extends View> viewClass, boolean caching) {
        super(viewName, viewClass);
        this.caching = caching;
    }



    @Override
    public View getView(String viewName) {

        if (caching) {
            if (view == null) {
                view = super.getView(viewName);
            }
        } else {
            view = super.getView(viewName);
        }

        return view;
    }

    public boolean isCaching() {
        return caching;
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
    }

}
