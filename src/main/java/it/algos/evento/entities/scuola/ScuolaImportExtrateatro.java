package it.algos.evento.entities.scuola;

import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.insegnante.InsegnanteImport;
import it.algos.webbase.web.table.ATable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Personalizzazione per Extrateatro
 */
public class ScuolaImportExtrateatro extends ScuolaImport {


    public ScuolaImportExtrateatro(ATable table, DoneListener listener) {
        super(table, listener);
    }

    @Override
    public String[] getColumnNames() {

        String[] baseNames = super.getColumnNames();
        ArrayList<String> names = new ArrayList<String>();
        for (String name : baseNames) {
            names.add(name);
        }

        String[] extraNames = Columns.getColumnNames();
        for (String name : extraNames) {
            names.add(name);
        }

        return names.toArray(new String[0]);
    }


    @Override
    public Scuola scuolaFromExcel(HashMap<String, String> valueMap) {
        Scuola scuola = super.scuolaFromExcel(valueMap);
        if (scuola != null) {
            String s = valueMap.get(Columns.dirigente.getTitoloColonna()).trim();
            if(!s.isEmpty()){
                scuola.setNote("Dirigente: "+s);
            }
        }
        return scuola;
    }



    /**
     * Colonne aggiuntive specifiche di Extrateatro
     */
    private enum Columns {

        dirigente("DIRIGENTE");

        private String titoloColonna;

        Columns(String name) {
            this.titoloColonna = name;
        }

        public String getTitoloColonna() {
            return titoloColonna;
        }

        public static String[] getColumnNames() {
            String[] columnNames = new String[Columns.values().length];
            for (int i = 0; i < columnNames.length; i++) {
                Columns c = Columns.values()[i];
                columnNames[i] = c.titoloColonna;
            }
            return columnNames;
        }


    }
}
