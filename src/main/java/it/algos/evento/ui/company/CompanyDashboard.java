package it.algos.evento.ui.company;

import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.multiazienda.EQuery;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.lib.LibSession;

import java.util.Date;

/**
 * Dashboard della Company
 */
public class CompanyDashboard extends VerticalLayout {

    private StringToIntegerConverter intConverter = new StringToIntegerConverter();
    private StringToBigDecimalConverter bdConverter = new StringToBigDecimalConverter();
    private CompanyHome home;

    private HorizontalLayout titlePlaceholder = new HorizontalLayout();
    private HorizontalLayout pPrenScadPlaceholder = new HorizontalLayout();
    private HorizontalLayout pConfPagScadPlaceholder = new HorizontalLayout();
    private HorizontalLayout pPagScadPlaceholder = new HorizontalLayout();
    private HorizontalLayout rappresentazioniPlaceholder = new HorizontalLayout();
    private HorizontalLayout prenotazioniRicevutePlaceholder = new HorizontalLayout();
    private HorizontalLayout postiPrenotatiPlaceholder = new HorizontalLayout();

    private InfoBar barNumeri;
    private InfoBar barPosti;
    private InfoBar barImporti;


    public CompanyDashboard(CompanyHome home) {
        this.home = home;

        //addStyleName("lightGrayBg");

        setWidthUndefined();

        setMargin(false);
        setSpacing(false);

        barNumeri = new InfoBar("Numero di prenotazioni", home, false);
        barPosti = new InfoBar("Numero di posti", home, false);
        barImporti = new InfoBar("Importo", home, true);

        // create the UI
        createUI();

    }

    /**
     * Costruzione della UI
     */
    private void createUI() {
         Component spacer;

        // titolo
        VerticalLayout titlePanel=new VerticalLayout();
        //titlePanel.addStyleName("redBg");
        titlePanel.setHeightUndefined();
        titlePanel.setWidthUndefined();
        titlePanel.addComponent(titlePlaceholder);
        addComponent(titlePanel);
        setExpandRatio(titlePanel, 0);

        //spacer
        spacer = new VSpacer();
        addComponent(spacer);
        setExpandRatio(spacer, 1);

        // riga scaduti
        HorizontalLayout scadPanel = new HorizontalLayout();
        //scadPanel.addStyleName("pinkBg");
        scadPanel.setHeightUndefined();
        scadPanel.setWidthUndefined();
        scadPanel.setSpacing(true);
        scadPanel.addComponent(pPrenScadPlaceholder);
        scadPanel.addComponent(pConfPagScadPlaceholder);
        scadPanel.addComponent(pPagScadPlaceholder);
        addComponent(scadPanel);
        setExpandRatio(scadPanel,0);

        //spacer
        spacer = new VSpacer();
        addComponent(spacer);
        setExpandRatio(spacer, 1);

        // barre grafiche
        VerticalLayout graphPanel=new VerticalLayout();
        graphPanel.setSpacing(true);
        graphPanel.setHeightUndefined();
        graphPanel.setWidth("100%");
        //graphPanel.addStyleName("yellowBg");
        graphPanel.addComponent(barNumeri);
        graphPanel.addComponent(barPosti);
        graphPanel.addComponent(barImporti);
        graphPanel.addComponent(new InfoBarLegend(home));
        addComponent(graphPanel);
        setExpandRatio(graphPanel,0);

        //spacer
        spacer = new VSpacer();
        addComponent(spacer);
        setExpandRatio(spacer, 1);


        // inizio pannello inferiore
        HorizontalLayout bottomPanel=new HorizontalLayout();
        bottomPanel.setWidth("100%");
        bottomPanel.setHeightUndefined();
        bottomPanel.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(bottomPanel);
        setExpandRatio(bottomPanel, 0);

        // pannello altre info
        VerticalLayout infoPanel = new VerticalLayout();
        infoPanel.setHeightUndefined();
        infoPanel.setWidthUndefined();
        infoPanel.addComponent(rappresentazioniPlaceholder);
        infoPanel.addComponent(prenotazioniRicevutePlaceholder);
        infoPanel.addComponent(postiPrenotatiPlaceholder);
        bottomPanel.addComponent(infoPanel);
        bottomPanel.setExpandRatio(infoPanel, 0);

        // spacer orizzontale
        spacer = new HSpacer();
        bottomPanel.addComponent(spacer);
        bottomPanel.setExpandRatio(spacer, 1);

        // logo
        Resource res=CompanyPrefs.splashImage.getResource();
        Image img = new Image(null, res);
        img.setHeight(6, Unit.EM);
        bottomPanel.addComponent(img);
        bottomPanel.setExpandRatio(img, 0);
        // fine pannello inferiore


        //spacer
        spacer = new VSpacer();
        addComponent(spacer);
        setExpandRatio(spacer, 1);


    }


    /**
     * Aggiorna tutti i valori visualizzati
     */
    public void update() {

        creaTitoloDashboard();
        createPrenScadute();
        createConfPagaScadute();
        createPagaScadute();

        // barra n. prenotazioni
        barNumeri.update(EQuery.countPrenotazioniCongelate(),
                EQuery.countPrenotazioniNonConfermate(),
                EQuery.countPrenotazioniPagamentoNonConfermato(),
                EQuery.countPrenotazioniPagamentoConfermato(),
                EQuery.countPrenotazioniPagamentoRicevuto());

        // barra n. posti
        barPosti.update(EQuery.sumPostiPrenotazioniCongelate(),
                EQuery.sumPostiPrenotazioniNonConfermate(),
                EQuery.sumPostiPrenotazioniPagamentoNonConfermato(),
                EQuery.sumPostiPrenotazioniPagamentoConfermato(),
                EQuery.sumPostiPrenotazioniPagamentoRicevuto());

        // barra importi
        barImporti.update(EQuery.sumImportoPrenotazioniCongelate().intValue(),
                EQuery.sumImportoPrenotazioniNonConfermate().intValue(),
                EQuery.sumImportoPrenotazioniPagamentoNonConfermato().intValue(),
                EQuery.sumImportoPrenotazioniPagamentoConfermato().intValue(),
                EQuery.sumImportoPrenotazioniPagamentoRicevuto().intValue());

        createRappresentazioni();
        createPrenotazioniRicevute();
        createPostiPrenotati();

    }

    /**
     * Crea il componente UI che rappresenta il titolo della dashboard
     */
    private void creaTitoloDashboard() {
        Stagione stag = Stagione.getStagioneCorrente();
        String sName="";
        if (stag!=null) {
            sName=stag.toString();
        }else{
            sName="stagione corrente non definita!";
        }
        String s = "Andamento stagione " + sName;
        HTMLLabel label = new HTMLLabel();
        label.setValue(s);
        label.addStyleName("label-big");

        titlePlaceholder.removeAllComponents();
        titlePlaceholder.addComponent(label);
    }


    /**
     * Crea il componente UI che rappresenta le conferme prenotazione scadute
     */
    private void createPrenScadute() {
        int quante = EQuery.countPrenotazioniScadute();
        Component comp = new CompScadute(quante, "prenotazioni scadute", EventoApp.KEY_MOSTRA_PREN_SCADUTE);
        pPrenScadPlaceholder.removeAllComponents();
        pPrenScadPlaceholder.addComponent(comp);
    }


    /**
     * Crea il componente UI che rappresenta le conferme di pagamento scadute
     */
    private void createConfPagaScadute() {
        int quante = EQuery.countPrenRitardoPagamento1();
        Component comp = new CompScadute(quante, "conferme pagamento scadute", EventoApp.KEY_MOSTRA_PREN_RITARDO_PAGAMENTO_1);
        pConfPagScadPlaceholder.removeAllComponents();
        pConfPagScadPlaceholder.addComponent(comp);

    }

    /**
     * Crea il componente UI che rappresenta le prenotazioni con pagamento scaduto
     */
    private void createPagaScadute() {
        int quante = EQuery.countPrenPagamentoScaduto();
        Component comp = new CompScadute(quante, "pagamenti scaduti", EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_SCADUTO);
        pPagScadPlaceholder.removeAllComponents();
        pPagScadPlaceholder.addComponent(comp);
    }



    /**
     * Crea il componente UI che rappresenta le rappresentazioni
     */
    private void createRappresentazioni() {

        int totRapp = EQuery.countRappresentazioni(getStagione());
        int rappPassate = EQuery.countRappresentazioni(getStagione(), new Date());
        int percent =0;
        if(totRapp!=0){
            percent=Math.round(rappPassate * 100 / totRapp);
        }

        HTMLLine line = new HTMLLine();
        line.add("rappresentazioni effettuate:", HTMLLine.SMALL);
        line.add(getString(rappPassate), HTMLLine.BIG);
        line.add("su", HTMLLine.SMALL);
        line.add(getString(totRapp), HTMLLine.BIG);
        line.add("("+percent+"%)", HTMLLine.SMALL);

        rappresentazioniPlaceholder.removeAllComponents();
        rappresentazioniPlaceholder.addComponent(line);
    }



    /**
     * Crea il componente UI che rappresenta le prenotazioni ricevute
     */
    private void createPrenotazioniRicevute() {
        int prenRicevute = EQuery.countPrenotazioni();
        HTMLLine line = new HTMLLine();
        line.add("prenotazioni ricevute:", HTMLLine.SMALL);
        line.add(getString(prenRicevute), HTMLLine.BIG);

        prenotazioniRicevutePlaceholder.removeAllComponents();
        prenotazioniRicevutePlaceholder.addComponent(line);

    }

    /**
     * Crea il componente UI che rappresenta i posti prenotati
     */
    private void createPostiPrenotati() {

        int prenotati = EQuery.countPostiPrenotati(getStagione());
        int disponibili = EQuery.countCapienza(getStagione());
        int percent=0;
        if(disponibili!=0){
            percent = Math.round(prenotati * 100 / disponibili);
        }

        HTMLLine line = new HTMLLine();
        line.add("posti prenotati:", HTMLLine.SMALL);
        line.add(getString(+prenotati), HTMLLine.BIG);
        line.add("su", HTMLLine.SMALL);
        line.add(getString(disponibili), HTMLLine.BIG);
        line.add("("+percent+"%)", HTMLLine.SMALL);

        postiPrenotatiPlaceholder.removeAllComponents();
        postiPrenotatiPlaceholder.addComponent(line);

    }


    /**
     * Ritorna la stagione corrente
     */
    private Stagione getStagione() {
        return Stagione.getStagioneCorrente();
    }


    private String getString(int num) {
        return intConverter.convertToPresentation(num, String.class, null);
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


    /**
     * HTML Label
     */
    private class HTMLLabel extends Label {

        public HTMLLabel(String content) {
            super(content, ContentMode.HTML);
            //addStyleName("greenBg");
        }

        public HTMLLabel() {
            this("");
        }
    }



    /**
     * Scritta con parti piccole e grandi
     */
    private class HTMLLine extends HorizontalLayout{
        private static final int SMALL=1;
        private static final int BIG=2;

        public HTMLLine() {
            setSpacing(true);
        }

        public void add(String s, int size){
            Component comp=new HTMLLabel(s);
            switch (size){
                case SMALL:
                    comp.addStyleName("label-middle");break;
                case BIG:
                    comp.addStyleName("label-big");break;
            }

            addComponent(comp);
            setComponentAlignment(comp, Alignment.MIDDLE_CENTER);

        }


    }




    /**
     * Vertical Spacer
     */
    private class VSpacer extends Label {

        VSpacer() {
            setHeight("100%");
            setWidth("2em");
            //addStyleName("darkGrayBg");
        }


    }


    /**
     * Vertical Spacer
     */
    private class HSpacer extends Label {

        HSpacer() {
            setWidth("100%");
            setHeight("1em");
            //addStyleName("darkGrayBg");
        }


    }



    /**
     * Componente indicatore di prenotazioni scadute
     */
    private class CompScadute extends HorizontalLayout{

        /**
         * @param quanti  il numero di prenotazioni
         * @param desc la descrizione
         * @param costante la costante da registrare nella sessione
         *                 prima di lanciare il modulo Prenotazioni
         */
        public CompScadute(int quanti, String desc, String costante) {
            setSpacing(true);
            HTMLLabel label = new HTMLLabel();
            label.addStyleName("label-middle");
            label.setValue(desc);
            Button button = new NumButton(quanti, desc, costante);

            addComponent(label);
            addComponent(button);
            setComponentAlignment(label, Alignment.MIDDLE_LEFT);
            setComponentAlignment(button, Alignment.MIDDLE_LEFT);


        }

    }


    /**
     * Bottone indicatore di prenotazioni scadute
     */
    private class NumButton extends Button {

        /**
         * @param quanti  il numero
         * @param tooltip il testo per il tooltip
         */
        public NumButton(int quanti, String tooltip, String costante) {
            super("" + quanti);

            addStyleName("infoBarSegment");

            if (quanti > 0) {
                addStyleName("redGradientBg");

                addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {
                        // regola l'attributo che farà sì che il modulo Prenotazioni
                        // esegua la query quando diventa visibile
                        LibSession.setAttribute(costante, true);

                        // clicca sul menu Prenotazioni
                        clickMenuPren();

                    }
                });

            } else {
                addStyleName("greenGradientBg");
            }
            setHeight("2em");
            setHtmlContentAllowed(true);

            String description=tooltip + ": " + quanti;
            if (quanti>0) {
                description+="<br><strong>clicca per vedere</strong>";
            }
            setDescription(description);
        }
    }


}
