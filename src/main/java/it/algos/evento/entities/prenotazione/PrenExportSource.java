package it.algos.evento.entities.prenotazione;

import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.updown.ExportStreamSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Custom ExportStreamSource needed to append totals row to the worksheet
 */
public class PrenExportSource extends ExportStreamSource {

    // values to be injected
    private int totPosti;
    private float totImporto;

    public PrenExportSource(ExportConfiguration config) {
        super(config);
    }

    @Override
    protected void populateWorkbook() {
        super.populateWorkbook();

        Cell cell;
        Row row;

        // add totals

        row = addRow();

        cell = row.createCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Totale posti");

        cell = row.createCell(1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(totPosti);

        row = addRow();

        cell = row.createCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Totale importo");

        cell = row.createCell(1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        BigDecimal bd = new BigDecimal(totImporto);
        BigDecimal scaled = bd.setScale(2, RoundingMode.HALF_UP);
        cell.setCellValue(scaled.doubleValue());

    }

    public void setTotPosti(int totPosti) {
        this.totPosti = totPosti;
    }

    public void setTotImporto(float totImporto) {
        this.totImporto = totImporto;
    }
}
