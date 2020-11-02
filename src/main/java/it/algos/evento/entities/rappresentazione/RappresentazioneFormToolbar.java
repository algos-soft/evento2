package it.algos.evento.entities.rappresentazione;

import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.toolbar.FormToolbar;

/**
 * Created by alex on 17-01-2016.
 */
public class RappresentazioneFormToolbar extends FormToolbar {

    public RappresentazioneFormToolbar(AForm form) {
        super(form);
    }

    @Override
    protected void addButtons() {
        super.addButtons();

//        MenuBar.MenuItem button = addButton("Esporta", RappresentazioneModulo.ICON_MEMO_EXPORT, new MenuBar.Command() {
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                Object id=getForm().getItemId();
//                RappresentazioneModulo.esportaRappresentazione(id);
//            }// end of method
//        });// end of anonymous inner class
//
//        button.setDescription(RappresentazioneModulo.CMD_PRENOTAZIONI_EXPORT);

    }
}
