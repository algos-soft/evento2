package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.EmailField;

/**
 * Dialogo conferma invio email di qualsiasi tipo, relativamente a una prenotazione.
 * Fornisce le funzionalità di gestione indirizzi di spedizione a referente e scuola.
 * Presenta un pannello con gli indirizzi del referente e della scuola con
 * relativi checkboxes per abilitarli e disabilitarli.
 * Alla conferma esegue le validazioni degli indirizzi inseriti.
 * E' anche possibile <strong>non</strong> inserire automaticamente il pannello indirizzi nel dialogo
 * usando l'apposito flag del costruttore. In tal caso la sottoclasse potrà
 * recuperare il componente con getEmailComponent() e disporlo graficamente
 * dove desiderato.
 */
class DialogoConfermaInvioManuale extends ConfirmDialog {
    private Prenotazione pren;

    protected CheckBoxField sendRef;
    protected CheckBoxField sendScuola;
    protected EmailField mailRef;
    protected EmailField mailScuola;
    private GridLayout gridLayout;
    private Component component;

    /**
     * Costruttore.
     *
     * @param pren      - la prenotazione
     * @param titolo    - il titolo del dialogo
     * @param messaggio - eventuale messaggio
     * @param addPanel  - false per non inserire graficamente il pannello indirizzi nel dialogo
     *                  (in tal caso lo si potrà poi recuperare con getEmailComponent())
     */
    public DialogoConfermaInvioManuale(Prenotazione pren, String titolo, String messaggio, boolean addPanel) {
        super(null);
        this.pren = pren;
        setTitle(titolo);
        setMessage(messaggio);
        setConfirmButtonText("Invia");

        sendRef = new CheckBoxField("Invia e-mail al referente");
        sendRef.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncUI();
            }
        });

        sendScuola = new CheckBoxField("Invia e-mail alla scuola");
        sendScuola.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncUI();
            }
        });

        mailRef = new EmailField();
        mailRef.setCaption(null);
        mailScuola = new EmailField();
        mailScuola.setCaption(null);

        component = createUI();
        if (addPanel) {
            addComponent(component);
        }
        populateUI();
        syncUI();

    }


    /**
     * Costruttore.
     * Inserisce automaticamente il pannello indirizzi.
     *
     * @param pren      - la prenotazione
     * @param titolo    - il titolo del dialogo
     * @param messaggio - eventuale messaggio
     */
    public DialogoConfermaInvioManuale(Prenotazione pren, String titolo, String messaggio) {
        this(pren, titolo, messaggio, true);
    }


    protected Component createUI() {
        gridLayout = new GridLayout(2, 2);
        gridLayout.setMargin(false);
        gridLayout.setSpacing(true);
        gridLayout.addComponent(sendRef, 0, 0);
        gridLayout.setComponentAlignment(sendRef, Alignment.MIDDLE_LEFT);
        gridLayout.addComponent(mailRef, 1, 0);
        gridLayout.setComponentAlignment(mailRef, Alignment.MIDDLE_LEFT);
        gridLayout.addComponent(sendScuola, 0, 1);
        gridLayout.setComponentAlignment(sendScuola, Alignment.MIDDLE_LEFT);
        gridLayout.addComponent(mailScuola, 1, 1);
        gridLayout.setComponentAlignment(mailScuola, Alignment.MIDDLE_LEFT);
        return gridLayout;
    }


    protected void populateUI() {
        String s;
        s = pren.getEmailRiferimento();
        if (s != null && !s.equals("")) {
            sendRef.setValue(true);
            mailRef.setValue(s);
        }

        if (!pren.isPrivato()) {
            Scuola scuola = pren.getScuola();
            if (scuola != null) {
                s = scuola.getEmail();
                if (s != null && !s.equals("")) {
                    sendScuola.setValue(true);
                    mailScuola.setValue(s);
                }
            }
        }

    }

    protected void syncUI() {
        mailRef.setEnabled(sendRef.getValue());
        mailScuola.setEnabled(sendScuola.getValue());
    }

    @Override
    protected void onConfirm() {

        String err = "";
        if (sendRef.getValue()) {
            if (!mailRef.isValid()) {
                if (!err.equals("")) {
                    err += "<br>";
                }
                err += "e-mail referente non valida";
            }
            if (mailRef.isEmpty()) {
                if (!err.equals("")) {
                    err += "<br>";
                }
                err += "e-mail referente non specificata";
            }
        }

        if (sendScuola.getValue()) {
            if (!mailScuola.isValid()) {
                if (!err.equals("")) {
                    err += "<br>";
                }
                err += "e-mail scuola non valida";
            }
            if (mailScuola.isEmpty()) {
                if (!err.equals("")) {
                    err += "<br>";
                }
                err += "e-mail scuola non specificata";
            }
        }


        if (err.equals("")) {
            super.onConfirm();
        } else {
            Notification n = new Notification(err);
            n.setHtmlContentAllowed(true);
            n.show(Page.getCurrent());
        }

    }


    /**
     * @return l'array degli indirizzi dei destinatari
     * (elenco indirizzi separati da virgola)
     */
    public String getDestinatari() {
        String str = "";

        if (sendRef.getValue()) {
            if (!mailRef.isEmpty()) {
                if (!str.equals("")) {
                    str += ", ";
                }
                str += mailRef.getValue();
            }
        }
        if (sendScuola.getValue()) {
            if (!mailScuola.isEmpty()) {
                if (!str.equals("")) {
                    str += ", ";
                }
                str += mailScuola.getValue();
            }
        }

        return str;
    }


    public Prenotazione getPrenotazione() {
        return pren;
    }

    public GridLayout getGridLayout() {
        return gridLayout;
    }

    /**
     * Ritorna il pannello con indirizzi e checkboxes
     */
    public Component getEmailComponent() {
        return component;
    }

}
