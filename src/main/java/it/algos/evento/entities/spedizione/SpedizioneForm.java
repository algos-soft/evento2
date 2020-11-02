package it.algos.evento.entities.spedizione;

import com.vaadin.data.Item;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by Alex on 28/10/15.
 */
public class SpedizioneForm extends ModuleForm {

//    public SpedizioneForm(Item item) {
//        super(item);
//    }

    public SpedizioneForm(ModulePop module, Item item) {
        super(item, module);
    }

//    public SpedizioneForm(ModulePop module) {
//        super(module);
//    }

    @Override
    public void createFields() {

        addField(Spedizione_.dataSpedizione, new DateField("Data spedizione"));

        TextField tf = new TextField("Destinatario");
        tf.setColumns(22);
        addField(Spedizione_.destinatario, tf);

        addField(Spedizione_.lettera, new RelatedComboField(Lettera.class, "Lettera"));
        addField(Spedizione_.operatore, new TextField("Operatore"));
        addField(Spedizione_.spedita, new CheckBoxField("Spedita"));

        TextArea ta = new TextArea("Errore");
        ta.setColumns(22);
        ta.setRows(3);
        addField(Spedizione_.errore, ta);

    }// end of method

}
