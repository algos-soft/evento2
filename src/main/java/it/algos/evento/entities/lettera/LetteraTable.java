package it.algos.evento.entities.lettera;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.webbase.multiazienda.ETable;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class LetteraTable extends ETable {

    // id della colonna generata "tipo modello di lettera"
    private static final String COL_TIPO = "tipo";

    public LetteraTable(ModulePop modulo) {
        super(modulo);
        setColumnAlignment(COL_TIPO, Align.CENTER);
    }// end of constructor

    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_TIPO, new TipoColumnGenerator());
    }// end of method

    @Override
    protected Object[] getDisplayColumns() {
        return new Object[]{Lettera_.sigla, Lettera_.oggetto, COL_TIPO, Lettera_.allegati};
    }// end of method

    /**
     * Genera la colonna del Tipo.
     */
    private class TipoColumnGenerator implements ColumnGenerator {
        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Image image;
            String sigla;
            Lettera lettera = Lettera.find((long) itemId);
            String description = "";
            FontIcon resource=null;

            if (lettera != null) {
                sigla = lettera.getSigla();
                if (ModelliLettere.getAllDbCode().contains(sigla)) {
                    resource=FontAwesome.LOCK;
                    description = "Lettera standard";
                } else {
                    resource=FontAwesome.UNLOCK;
                    description = "Lettera extra";
                }
            }

            Label label = new Label(resource.getHtml() + " "+ description, ContentMode.HTML);
            return label;
        }
    }

}
