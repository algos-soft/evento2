package it.algos.evento.entities.insegnante;

import it.algos.webbase.web.table.ATable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Personalizzazione per Extrateatro
 */
public class InsegnanteImportExtrateatro extends InsegnanteImport {


    public InsegnanteImportExtrateatro(ATable table, DoneListener listener) {
        super(table, listener);
    }

    @Override
    public String[] getColumnNames() {

        String[] baseNames = super.getColumnNames();
        ArrayList<String> names = new ArrayList<String>();
        for(String name : baseNames){
            names.add(name);
        }

        String[] extraNames = Columns.getColumnNames();
        for(String name : extraNames){
            names.add(name);
        }

        return names.toArray(new String[0]);
    }

    @Override
    public Insegnante insegnanteFromExcel(HashMap<String, String> valueMap) {
        Insegnante ins = super.insegnanteFromExcel(valueMap);
        if (ins != null) {
            String s;
            StringBuilder sb;


            // mette tutto l'indirizzo (1 e 2) nell'indirizzo1
            sb = new StringBuilder();
            String ind1 = ins.getIndirizzo1();
            String ind2 = ins.getIndirizzo2();
            sb.append(ind1);
            if(sb.length()>0){
                sb.append(" ");
            }
            sb.append(ind2);
            ins.setIndirizzo1(sb.toString());
            ins.setIndirizzo2("");


            // compone la stringa per Indirizzo2 con i dati della scuola
            sb = new StringBuilder();

            s = valueMap.get(Columns.tipo.getTitoloColonna()).trim();
            appendPart(sb, "Tipo:", s);

            s = valueMap.get(Columns.scuola.getTitoloColonna()).trim();
            appendPart(sb, "Scuola:", s);

            s = valueMap.get(Columns.plessosede.getTitoloColonna()).trim();
            appendPart(sb, "Plesso/Sede:", s);

            s = valueMap.get(Columns.fax.getTitoloColonna()).trim();
            appendPart(sb, "Fax:", s);

            s = valueMap.get(Columns.note.getTitoloColonna()).trim();
            appendPart(sb, "Note:", s);

            ins.setIndirizzo2(sb.toString());


            // compone la stringa per le note con i dati dello storico
            sb = new StringBuilder();

            s = valueMap.get(Columns.stor1516.getTitoloColonna()).trim();
            appendLine(sb, "2015-2016:", s);

            s = valueMap.get(Columns.stor1415.getTitoloColonna()).trim();
            appendLine(sb, "2014-2015:", s);

            s = valueMap.get(Columns.stor1314.getTitoloColonna()).trim();
            appendLine(sb, "2013-2014:", s);

            s = valueMap.get(Columns.stor1213.getTitoloColonna()).trim();
            appendLine(sb, "2012-2013:", s);

            s = valueMap.get(Columns.stor1112.getTitoloColonna()).trim();
            appendLine(sb, "2011-2012:", s);

            s = sb.toString();
            ins.setNote(s);


        }
        return ins;
    }

    private void append(StringBuilder sb, String t, String s, String sep) {
        if (!s.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(sep);
            }
            sb.append(t+" "+s);
        }
    }

    private void appendLine(StringBuilder sb, String t, String s) {
        append(sb, t, s, "\n");
    }

    private void appendPart(StringBuilder sb, String t, String s) {
        append(sb, t, s, " - ");
    }




    /**
     * Colonne aggiuntive specifiche di Extrateatro
     */
    private enum Columns {

        tipo("TIPO"),

        scuola("SCUOLA"),

        plessosede("PLESSO/SEDE"),

        note("NOTE"),

        fax("FAX"),

        stor1516("STOR-2015-2016"),

        stor1415("STOR-2014-2015"),

        stor1314("STOR-2013-2014"),

        stor1213("STOR-2012-2013"),

        stor1112("STOR-2011-2012"),;

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
