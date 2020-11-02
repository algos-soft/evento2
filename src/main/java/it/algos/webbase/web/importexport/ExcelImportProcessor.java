package it.algos.webbase.web.importexport;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import it.algos.webbase.web.entity.BaseEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelImportProcessor {

	private final static Logger logger = Logger.getLogger(BaseEntity.class.getName());

	private Path file;
	private ExcelImportListener listener;
	private String[] columnNames;

	/**
	 * @param file
	 *            the Excel file to process
	 * @param columnNames
	 *            the column names to search
	 * @param listener
	 *            the listener for import events
	 */
	public ExcelImportProcessor(Path file, String[] columnNames, ExcelImportListener listener) {
		super();
		this.file = file;
		this.columnNames = columnNames;
		this.listener = listener;
	}

	/**
	 * Start processing the Excel file.
	 * <p>
	 * Notifies the listener for each new row<br>
	 * Notifies the listener at the end of the import
	 * process
	 */
	public void start() {
		try {

			InputStream stream = Files.newInputStream(file, StandardOpenOption.READ);
			Workbook workbook = WorkbookFactory.create(stream);

			Sheet worksheet = workbook.getSheetAt(0);
			Row firstRow = worksheet.getRow(0);

			// log the validation errors
			ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();
			int success = 0; // records added
			int failed = 0; // records failed

			// read the first row and map the required columns
			if (firstRow != null) {

				// a map: desired column -> excel column index
				// (index = -1 if column is not present in excel)
				HashMap<String, Integer> columnMap = createColumnMap(firstRow);

				Iterator<Row> rows = worksheet.rowIterator();
				rows.next(); // skip the header row
				int rowIdx = 1; // in excel, rows start from 1. Row 1 (header) is already read so the next is 2
				while (rows.hasNext()) {
					rowIdx++;
					HashMap<String, String> valueMap = new HashMap<String, String>();
					Row row = (Row) rows.next();
					for (String columnName : columnMap.keySet()) {
						String value = "";
						int colIdx = columnMap.get(columnName);
						if (colIdx >= 0) {
							try {
								Cell cell = row.getCell(colIdx);
								cell.setCellType(Cell.CELL_TYPE_STRING); // force conversion to string
								value = row.getCell(colIdx).getStringCellValue();
							} catch (Exception e) {
								logger.log(Level.WARNING, "The value at row " + rowIdx + ", column " + columnName
										+ " could not be read.", e);
							}
						}
						valueMap.put(columnName, value);

					}

					// check if the value map contains something
					boolean mapIsEmpty = true;
					for (String key : valueMap.keySet()) {
						String sValue = valueMap.get(key);
						if (!sValue.equals("")) {
							mapIsEmpty=false;
							break;
						}
					}
					
					// Invoke the listeners passing the row data.
					// The listeners return a ConstraintViolationException if something failed.
					// Create a ValidationError and add it to the list.
					if (!mapIsEmpty) {
						ConstraintViolationException exception = listener.rowReceived(valueMap);
						if (exception != null) {
							ValidationError error = new ValidationError(rowIdx, valueMap, exception);
							validationErrors.add(error);
							failed++;
						} else {
							success++;
						}
					}

				}

			}

			stream.close();

			// notify the listeners about the outcome
			ImportReport report = new ImportReport(success, failed, validationErrors);
			listener.importTerminated(report);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a map to associate desired column names with Excel column indexes
	 * 
	 * @param firstRow
	 *            the first row of the file
	 * @return a map: desired column name -> Excel column index The map has entries for all the desired column names.
	 *         The value can be -1, if the desired column has not been found in excel.
	 */
	private HashMap<String, Integer> createColumnMap(Row firstRow) {

		// create a map: excel column name - excel column index
		// first column = 0
		HashMap<String, Integer> excelMap = new HashMap<String, Integer>();
		int lastCell = firstRow.getLastCellNum();
		for (int i = 0; i < lastCell; i++) {
			Cell cell = firstRow.getCell(i);
			String name = cell.getStringCellValue();
			name = name.trim();
			if (!name.equals("")) {
				excelMap.put(name.toLowerCase(), i);
			}
		}

		// create a map: desired column name - excel column index
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (String columnName : columnNames) {
			String key = columnName.toLowerCase();
			int idx = -1;
			if (excelMap.containsKey(columnName.toLowerCase())) {
				idx = excelMap.get(key);
			}
			map.put(columnName, idx);
		}

		return map;
	}

	/**
	 * Listener interface for import events
	 */
	public interface ExcelImportListener {

		/**
		 * A row has been received from the Excel file.
		 * <p>
		 * 
		 * @param valueMap
		 *            a map with requested column names and values
		 * @return a ConstraintViolationException if any, or null if all went good.
		 */
		public ConstraintViolationException rowReceived(HashMap<String, String> valueMap);

		/**
		 * An import operation is terminated.
		 * 
		 * @param report
		 *            report with the outcome of the import operation
		 */
		public void importTerminated(ImportReport report);
	}

	/**
	 * Wrapper class for validation errors
	 */
	private class ValidationError {
		private int rowIndex;
		private HashMap<String, String> valueMap;
		private ConstraintViolationException exception;

		public ValidationError(int rowIndex, HashMap<String, String> valueMap, ConstraintViolationException exception) {
			super();
			this.rowIndex = rowIndex;
			this.valueMap = valueMap;
			this.exception = exception;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		public HashMap<String, String> getValueMap() {
			return valueMap;
		}

		public ConstraintViolationException getException() {
			return exception;
		}

		public String toString() {
			String report = "";
			ConstraintViolationException exception = getException();
			report += "Row #: " + getRowIndex();
			report += "\nValue map: " + getValueMap();
			for (ConstraintViolation violation : exception.getConstraintViolations()) {
				report += "\nViolation: " + violation.getMessage();
			}
			return report;
		}

	}

	/**
	 * Import report. Carries information about the outcome of an import operation.
	 */
	public class ImportReport {
		private int success;
		private int failed;
		private ArrayList<ValidationError> errors;

		public ImportReport(int success, int failed, ArrayList<ValidationError> errors) {
			super();
			this.success = success;
			this.failed = failed;
			this.errors = errors;
		}

		public int getSuccess() {
			return success;
		}

		public int getFailed() {
			return failed;
		}

		public ArrayList<ValidationError> getErrors() {
			return errors;
		}

		public String getReport() {
			String report = "Import report ";
			report += "\n" + "Date: " + new Date();
			report += "\n" + "Successfully imported records: " + success;

			if (failed > 0) {
				report += "\n" + "Records failed (not imported): " + failed;
				report += "\n";
				report += "\n" + "Details follow;";
				report += "\n";
				for (ValidationError error : errors) {
					report += "\n\n";
					report += error.toString();
				}
			}

			return report;
		}

	}

}
