package it.algos.evento.ui.company;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.webbase.web.lib.LibSession;

import java.util.Locale;

/**
 * Barra informativa grafica sullo stato delle prenotazioni.
 */
public class InfoBar extends VerticalLayout {

    private CompanyHome home;
    private boolean euro;
    private HorizontalLayout segmentsLayout;
    private BarSegment segCongelate;
    private BarSegment segNonConf;
    private BarSegment segPagaNonConf;
    private BarSegment segPagaConf;
    private BarSegment segPagaRic;

    // testi per tooltips, legenda ecc...
    public static final String TEXT_PREN_CONGELATE="prenotazioni congelate";
    public static final String TEXT_PREN_NOCONF="prenotazioni non confermate";
    public static final String TEXT_PAGA_NOCONF="prenotazioni confermate";
    public static final String TEXT_PAGA_CONF="pagamenti confermati";
    public static final String TEXT_PAGA_RIC="pagamenti ricevuti";

    public InfoBar(String titolo, CompanyHome home, boolean euro) {
        this.home=home;
        this.euro=euro;

        setWidth("100%");
        setSpacing(false);

        segCongelate=new BarSegment(TEXT_PREN_CONGELATE,"grayGradientBg",EventoApp.KEY_MOSTRA_PREN_CONGELATE);
        segNonConf=new BarSegment(TEXT_PREN_NOCONF,"redGradientBg",EventoApp.KEY_MOSTRA_PREN_NON_CONFERMATE);
        segPagaNonConf=new BarSegment(TEXT_PAGA_NOCONF,"orangeGradientBg",EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_NON_CONFERMATO);
        segPagaConf=new BarSegment(TEXT_PAGA_CONF,"goldenGradientBg",EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_CONFERMATO);
        segPagaRic=new BarSegment(TEXT_PAGA_RIC, "greenGradientBg",EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_RICEVUTO);

        Label label = new Label(titolo);
        label.addStyleName("infoBarTitle");
        addComponent(label);

        segmentsLayout = new HorizontalLayout();
        segmentsLayout.setWidth("100%");
        segmentsLayout.setHeight("4em");
        segmentsLayout.addComponent(segCongelate);
        segmentsLayout.addComponent(segNonConf);
        segmentsLayout.addComponent(segPagaNonConf);
        segmentsLayout.addComponent(segPagaConf);
        segmentsLayout.addComponent(segPagaRic);
        addComponent(segmentsLayout);

    }


    /**
     * Assegna i pesi ai vari segmenti
     */
    public void update(int congelate, int nonConf, int pagaNonConf, int pagaConf, int pagaRic){
        segmentsLayout.setExpandRatio(segCongelate, congelate);
        segCongelate.setValue(congelate);

        segmentsLayout.setExpandRatio(segNonConf, nonConf);
        segNonConf.setValue(nonConf);

        segmentsLayout.setExpandRatio(segPagaNonConf, pagaNonConf);
        segPagaNonConf.setValue(pagaNonConf);

        segmentsLayout.setExpandRatio(segPagaConf, pagaConf);
        segPagaConf.setValue(pagaConf);

        segmentsLayout.setExpandRatio(segPagaRic, pagaRic);
        segPagaRic.setValue(pagaRic);

    }


    /**
     * Segmento interno alla barra
     */
    private class BarSegment extends HorizontalLayout {

        private String title;
        private final StringToIntegerConverter intConv = new StringToIntegerConverter();
        private Label label;

        public BarSegment(String title, String style, final String key) {
            this.title=title;
            addStyleName(style);
            addStyleName("infoBarSegment");
            setHeight("2.3rem");
            setWidth("100%");
            setMargin(false);
            setSpacing(false);

            // Label per mostrare il numero al centro della barra
            label=new Label();
            label.setWidthUndefined();
            label.setContentMode(ContentMode.HTML);
            addComponent(label);
            setComponentAlignment(label, Alignment.MIDDLE_CENTER);

            addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                    // regola l'attributo di sessione che farà sì che il modulo esegua la query quando diventa visibile
                    LibSession.setAttribute(key, true);
                    // clicca sul menu Prenotazioni
                    clickMenuPren();

                }
            });



        }

        public void setValue(int value){
            String s = intConv.convertToPresentation(value, String.class, Locale.getDefault());
            if (euro){
                s+="&euro;";
            }
            label.setValue(s);
            //setCaption(s);
            setDescription(title + ": " + s + "<br><strong>clicca per vedere</strong>");
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

}
