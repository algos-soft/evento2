package it.algos.webbase.web.updown;

import it.algos.webbase.web.updown.OnDemandFileDownloader.OnDemandStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import com.vaadin.server.StreamResource.StreamSource;

/**
 * Stream resource for JasperReportBuilder reports
 */
@SuppressWarnings("serial")
public class ReportStreamResource implements OnDemandStreamResource, StreamSource {

	private JasperReportBuilder report;
	private String filename;

	public ReportStreamResource(JasperReportBuilder report, String filename) {
		super();
		this.report = report;
		this.filename = filename;
	}

	@Override
	public InputStream getStream() {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			report.toPdf(os);
			is = new ByteArrayInputStream(os.toByteArray());
		} catch (DRException e) {
			e.printStackTrace();
		}
		return is;
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

}
