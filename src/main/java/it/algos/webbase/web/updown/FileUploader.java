package it.algos.webbase.web.updown;

import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.bootstrap.ABootStrap;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Show a window allowing the user to upload a file.
 * <p>
 * The file is uploaded to the UPLOAD folder in the container
 * <p>
 * If the file already exists:<br>
 * <li>if overwriteExisting=true, the file is overwritten</li>
 * <li>if overwriteExisting=false, a timestamp is appended to the filename</li>
 */
@SuppressWarnings("serial")
public class FileUploader implements Receiver, SucceededListener {

	private Path file;
	private Window window;
	private Upload upload;
	private Label label;
	private boolean overwriteExisting = false;

	/**
	 * Name of the folder for uploaded files
	 */
	private String uploadFolderName;

	private ArrayList<UploadFinishedListener> listeners = new ArrayList<UploadFinishedListener>();

	public FileUploader() {
		this(AlgosApp.UPLOAD_FOLDER_NAME);
	}// end of constructor

	public FileUploader(String uploadFolderName) {
		super();
		this.setUploadFolderName(uploadFolderName);

		upload = new Upload("", this);
		setButtonText("Carica");
		upload.addFinishedListener(new FinishedListener() {

			@Override
			public void uploadFinished(FinishedEvent event) {
				fireFinished();
				window.close();
			}
		});

		window = new Window();
		window.setModal(true);
		window.setDraggable(false);
		window.setResizable(false);

		window.setCaption("Carica file");

		label = new Label();
		label.setContentMode(ContentMode.HTML);

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);
		layout.addComponent(upload);
		window.setContent(layout);
		window.center();

	}// end of constructor

	public void show(UI ui) {
		ui.addWindow(window);
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		OutputStream stream = null; // Stream to write to
		try {
			// Open the file for writing.
			ServletContext sc = ABootStrap.getServletContext();

			// The real path returned will be in a form appropriate to the
			// computer and operating system on which the servlet container
			// is running, including the proper path separators.
			// Path uploadPath = Paths.get(sc.getRealPath("/" + Application.UPLOAD_FOLDER_NAME));
			Path uploadPath = Paths.get(sc.getRealPath("/" + getUploadFolderName()));

			// check if exists, or create
			if (!Files.exists(uploadPath, LinkOption.NOFOLLOW_LINKS)) {
				Files.createDirectory(uploadPath);
			}
			file = Paths.get(uploadPath.toString(), filename);

			// if the file exists, add the timestamp suffix to the name
			if (!overwriteExisting) {
				if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
					java.util.Date date = new java.util.Date();
					Timestamp ts = new Timestamp(date.getTime());
					String suffix = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS").format(ts);
					file = Paths.get(file.toString() + "_" + suffix);
				}
			}

			stream = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (final IOException e) {
			new Notification("Impossibile aprire il file", e.getMessage(), Notification.Type.ERROR_MESSAGE).show(Page
					.getCurrent());
		}
		return stream; // Return the output stream to write to
	}

	public void uploadSucceeded(SucceededEvent event) {
	}

	public void setTitle(String title) {
		window.setCaption(title);
	}

	public void setMessage(String message) {
		label.setValue(message);
	}

	public void setButtonText(String text) {
		upload.setButtonCaption(text);
	}

	/**
	 * @return the uploadFolderName
	 */
	public String getUploadFolderName() {
		return uploadFolderName;
	}

	/**
	 * @param uploadFolderName
	 *            the uploadFolderName to set
	 */
	public void setUploadFolderName(String uploadFolderName) {
		this.uploadFolderName = uploadFolderName;
	}

	/**
	 * If the uploaded file already exist, overwrite or add timestamp to the filename
	 */
	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

	public void addUploadFinishedListener(UploadFinishedListener listener) {
		listeners.add(listener);
	}

	private void fireFinished() {
		for (UploadFinishedListener listener : listeners) {
			listener.uploadFinished(file);
		}
	}

	public interface UploadFinishedListener {
		public abstract void uploadFinished(Path file);
	}

}
