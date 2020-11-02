package it.algos.webbase.domain.log;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import java.util.HashMap;

/**
 * Created by gac on 16 set 2015.
 * Sovrascrive la classe standard per allargare il Form ed i Campi
 */
public class LogForm extends ModuleForm {
    @SuppressWarnings("all")
    private static String LAR_FORM = "1000px";

    @SuppressWarnings("all")
    private static String LAR_CAMPO = "800px";

//    /**
//     * Constructor
//     *
//     * @param module the referenced module
//     */
//    public LogForm(ModulePop module) {
//        this(module, (Item)null);
//    }// end of constructor

    /**
     * Constructor
     *
     * @param module the reference module
     * @param item   the item with the properties
     */
    public LogForm(Item item, ModulePop module) {
        super(item, module);
        doInit();
    }// end of constructor

    private void doInit() {
        setWidth(LAR_FORM);

        for(Field field : getFields()){
            field.setWidth(LAR_CAMPO);
        }

//        for (HashMap.Entry prop : bindMap.entrySet()) {
//            field = (Field) prop.getValue();
//            field.setWidth(LAR_CAMPO);
//        }// end of for cycle

    }// end of method

}// end of class
