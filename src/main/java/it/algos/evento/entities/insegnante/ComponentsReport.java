package it.algos.evento.entities.insegnante;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.datasource.DRDataSource;

public class ComponentsReport {

	public ComponentsReport() {
		super();
		JasperReportBuilder report = DynamicReports.report();
		// report.addColumn(Columns.column("Item", "item", DataTypes.stringType()));
		// report.addColumn(Columns.column("Quantity", "quantity", DataTypes.integerType()));
		report.addTitle(Components.text("Report title"));
		DRDataSource ds = new DRDataSource();
		report.setDataSource(ds);
		// try {
		// report.toPdf(outputStream);
		// } catch (DRException e) {
		// e.printStackTrace();
		// }
	}

}
