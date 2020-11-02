package it.algos.webbase.web.navigator;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.*;
import it.algos.webbase.web.screen.ErrorScreen;

import java.util.List;

@SuppressWarnings("serial")
public class AlgosNavigator extends Navigator {

    public AlgosNavigator(UI ui, SingleComponentContainer container) {
        super(ui, container);
        setErrorView(new ErrorView());
    }// end of constructor

    /**
     * Configura il Navigator in base a una MenuBar.
     * <p/>
     * Recupera i Component dalla Menubar e crea le View per il Navigator
     */
    public void configureFromMenubar(MenuBar mb) {
        List<MenuBar.MenuItem> items = mb.getItems();
        for (MenuBar.MenuItem item : items) {
            scanItem(item);
        } // fine del ciclo for
    }// end of method



    protected void scanItem(MenuBar.MenuItem item) {
        MenuBar.Command cmd = item.getCommand();
        if (cmd instanceof MenuCommand) {

            MenuCommand mcmd = (MenuCommand) cmd;
            String key = mcmd.getNavigatorAddress();
            Class clazz = mcmd.getClazz();
            boolean caching = mcmd.isCached();
            View view = mcmd.getView();

            // if the view class is specified, create a lazy (class-based) view provider
            // if the view is specified, create a heavyweight view provider
            ViewProvider provider;
            if(view==null){
                provider=new CachingViewProvider(key, clazz, caching);
            }else{
                provider = new StaticViewProvider(key, view);
            }
            addProvider(provider);

        }// fine del blocco if

        // esamina i children dell'item
        List<MenuBar.MenuItem> items = item.getChildren();
        if (items != null) {
            for (MenuBar.MenuItem childItem : items) {
                scanItem(childItem);
            } // fine del ciclo for
        }// fine del blocco if
    }




    /**
     * A class encapsulating an ErrorScreen in a View
     */
    class ErrorView extends CustomComponent implements View {

        ErrorScreen errScreen;

        public ErrorView() {
            super();
            setSizeFull();
            errScreen = new ErrorScreen();
            setCompositionRoot(errScreen);
        }// end of constructor

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            String msg = "Questa pagina non esiste: " + event.getViewName();
            if (errScreen!=null) {
                errScreen.setMessage(msg);
            }// fine del blocco if
        }// end of method

    }// end of inner class

}// end of class
