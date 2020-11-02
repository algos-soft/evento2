package it.algos.evento.ui.company;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.webbase.web.lib.LibSession;

import java.util.Locale;

/**
 * Created by alex on 12-12-2015.
 */
public class InfoBarLegend extends HorizontalLayout {

    private CompanyHome home;


    public InfoBarLegend(CompanyHome home) {
        this.home=home;
        setSizeUndefined();
        setSpacing(true);
        addComponent(new Element("grayGradientBg", InfoBar.TEXT_PREN_CONGELATE, EventoApp.KEY_MOSTRA_PREN_CONGELATE));
        addComponent(new Element("redGradientBg", InfoBar.TEXT_PREN_NOCONF, EventoApp.KEY_MOSTRA_PREN_NON_CONFERMATE));
        addComponent(new Element("orangeGradientBg",InfoBar.TEXT_PAGA_NOCONF, EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_NON_CONFERMATO));
        addComponent(new Element("goldenGradientBg",InfoBar.TEXT_PAGA_CONF, EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_CONFERMATO));
        addComponent(new Element("greenGradientBg",InfoBar.TEXT_PAGA_RIC, EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_RICEVUTO));
    }

    /**
     * Segmento interno alla barra
     */
    private class Element extends HorizontalLayout {

        public Element(String style, String text, String costante) {
            setSpacing(true);
            setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            Button button = new Button();
            button.addStyleName(style);

            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    // regola l'attributo che farà sì che il modulo Prenotazioni
                    // esegua la query quando diventa visibile
                    LibSession.setAttribute(costante, true);

                    // clicca sul menu Prenotazioni
                    clickMenuPren();

                }
            });




            Label label = new Label(text);
            addComponent(button);
            addComponent(label);
        }



    }

    /**
     * Clicca sul menu Prenotazioni
     */
    private void clickMenuPren() {
        MenuBar.MenuItem mi = home.getItemPrenotazioni();
        mi.getCommand().menuSelected(mi);
        if (mi.isCheckable()) {
            mi.setChecked(!mi.isChecked());
        }
    }



}
