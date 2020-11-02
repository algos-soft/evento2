package it.algos.webbase.web.dialog;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Abstract base class for modal dialogs. <br>
 * To add Components (buttons, etc.) to the toolbar, call: getToolbar().addComponent(Component comp). <br>
 * To add Components to the detail area, call: addComponent(Component comp) (the detail area is a Vertical layout, you
 * can add one or more components.)
 * <p>
 * You can also set a message to be displayed in the detail area calling <code>setMessage(String message)</code>. The
 * message string can contain simple HTML.
 * <p>
 * Once the dialog is prepared, show it by calling <code>show(UI ui)</code>
 * <p>
 * Sizing: By default the dialog is not resizable and its size depends on the content.<br>
 * You can set it to be resizable with <code>setResizable(true)</code> but in this case a initial size is also needed.
 * Failing to provide initial size will cause incorrect sizing. e.g.<br>
 * <code>
 * dialog.setResizable(true);
 * dialog.setWidth("300px");
 * dialog.setHeight("200px");
 * </code>
 */
@SuppressWarnings("serial")
public abstract class BaseDialog extends Window {
	protected Component detail;
	private DialogToolbar toolbar;
	private VerticalLayout mainLayout;
	private Label label;
	String messageText;

	public BaseDialog(String title, String message) {
		super();
		setTitle(title);
		setMessageText(message);
		init();
	}

	public BaseDialog() {
		this(null, null);
	}

	private void init() {

		setModal(true);
		setClosable(false);
		setResizable(false);

		toolbar = createToolbarComponent();
		detail = createDetailComponent();
		setMessage(messageText);

		mainLayout = new VerticalLayout();
		syncResizable();

		mainLayout.addComponent(detail);

		detail.setHeight("100%");
		mainLayout.setExpandRatio(detail, 1f);

		mainLayout.addComponent(toolbar);
		toolbar.setWidth("100%");

		setContent(mainLayout);

		center();

	}


	/**
	 * Creates the component shown in the detail area.
	 * By default, a VerticalLayout
	 * @return the component shown in the detail area.
	 */
	protected Component createDetailComponent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		// add a label hosting the text to the default VerticalLayout
		label = new Label();
		label.setWidth("100%");
		label.setContentMode(ContentMode.HTML);
		layout.addComponent(label);

		return layout;
	}

	/**
	 * The component shown in the toolbar area.
	 */
	protected DialogToolbar createToolbarComponent() {
		return new DialogToolbar();
	}

	@Override
	public void setResizable(boolean resizable) {
		super.setResizable(resizable);
		syncResizable();
	}

	private void syncResizable() {
		if (mainLayout != null) {
			if (isResizable()) {
				mainLayout.setSizeFull();
			} else {
				mainLayout.setWidth("100%");
				mainLayout.setHeightUndefined();
			}
		}
	}

	/**
	 * Adds a component to the Detail area.<br>
	 * (the detail area is a Vertical layout)
	 * 
	 * @param comp
	 *            the component to add
	 */
	public void addComponent(Component comp) {
		if (detail != null) {
			if(detail instanceof ComponentContainer){
				ComponentContainer container = (ComponentContainer)detail;
				container.addComponent(comp);
			}
		}
	}

	/**
	 * Sets the message text
	 */
	public void setMessage(String message) {
		if (label != null) {
			if(message!=null && !message.equals("")){
				label.setValue(message);
				label.setVisible(true);
			}else{
				label.setValue(null);
				label.setVisible(false);
			}
		}
	}

	/**
	 * Sets the visibility of the detail area
	 * 
	 * @param visible
	 *            the visibility
	 */
	public void setDetailAreaVisible(boolean visible) {
		if (detail != null) {
			detail.setVisible(visible);
		}
	}

	/**
	 * @return the component hosting the detail area
	 */
	public Component getDetailComponent(){
		return detail;
	}

	/**
	 * Remove all components from inside the Detail component
	 */
	public void removeAllDetail(){
		Component comp=getDetailComponent();
		if(comp instanceof ComponentContainer){
			((ComponentContainer)comp).removeAllComponents();;
		}
	}

	public void show(UI ui) {
		ui.addWindow(this);
	}

	public void show() {
		show(UI.getCurrent());
	}


	public DialogToolbar getToolbar() {
		return toolbar;
	}

	public void setTitle(String title) {
		setCaption(title);
	}

	public String getMessageText() {
		return messageText;
	}

	private void setMessageText(String messageText) {
		this.messageText = messageText;
	}

}
