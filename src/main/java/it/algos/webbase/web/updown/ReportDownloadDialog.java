package it.algos.webbase.web.updown;

import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.updown.OnDemandFileDownloader.OnDemandStreamResource;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

@SuppressWarnings("serial")
public class ReportDownloadDialog extends ConfirmDialog {

	private JasperReportBuilder report;
	private String filename;

	/**
	 * Creates a dialog containing a button to download a given report with a given filename. The reason is that
	 * Vaadin's FileDownloader must be hooked to a Component (usually a button) to have it working.
	 * 
	 * @see 'Vaadin FileDownloader'
	 * @param report
	 *            the report to download
	 * @param filename
	 *            the name for the downloaded file
	 */
	public ReportDownloadDialog(JasperReportBuilder report, String filename) {
		super("Download report", null, null);
		this.report = report;
		this.filename = filename;
		setConfirmButtonText("Scarica");
		setCancelButtonText("Chiudi");
		setDetailAreaVisible(false);
		createDownloader();
	}

	/**
	 * Downloads a report with a default filename
	 * 
	 * @param report
	 *            the report to download
	 */
	public ReportDownloadDialog(JasperReportBuilder report) {
		this(report, "report.pdf");
	}

	private void createDownloader() {
		OnDemandStreamResource streamRes = new ReportStreamResource(report, filename);
		OnDemandFileDownloader downloader = new OnDemandFileDownloader(streamRes);
		downloader.extend(getConfirmButton());
	}

	@Override
	protected void onConfirm() {
	}

}
