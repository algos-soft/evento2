package it.algos.evento.config;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.field.ImageField;

@SuppressWarnings("serial")
public class PersonalConfigComponent extends BaseConfigPanel {
	
	private static final String KEY_SPLASHIMAGE = "splashimage";
	private static final String KEY_MENUBAR_ICON = "menubaricon";

	public PersonalConfigComponent() {
		super();

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		// create and add fields and other components
		ImageField splashImage = new ImageField("Splash image");
		layout.addComponent(splashImage);
		
		ImageField menubarIcon = new ImageField("Icona menubar");
		layout.addComponent(menubarIcon);

		layout.addComponent(createButtonPanel());

		addComponent(layout);
		
		// bind fields to properties
		getGroup().bind(splashImage, KEY_SPLASHIMAGE);
		getGroup().bind(menubarIcon, KEY_MENUBAR_ICON);

	}

	@Override
	public String getTitle() {
		return "Personalizzazione";
	}

	@Override
	public PrefSetItem createItem() {
		return new PersonalizzazioneItem();
	}
	
	/**
	 * Item containing form data
	 */
	private class PersonalizzazioneItem extends PropertysetItem implements PrefSetItem {

		public PersonalizzazioneItem() {
			super();
            addItemProperty(KEY_SPLASHIMAGE, new ObjectProperty<byte[]>(CompanyPrefs.splashImage.getBytes()));
            addItemProperty(KEY_MENUBAR_ICON, new ObjectProperty<byte[]>(CompanyPrefs.menubarIcon.getBytes()));
		}

		public void persist() {
			byte[] bytes;
			
			bytes = (byte[])getItemProperty(KEY_SPLASHIMAGE).getValue();
			if ((bytes!=null) && (bytes.length>0)) {
				CompanyPrefs.splashImage.put(bytes);
			} else {
				CompanyPrefs.splashImage.reset();
			}
			
			bytes = (byte[])getItemProperty(KEY_MENUBAR_ICON).getValue();
			if ((bytes!=null) && (bytes.length>0)) {
				CompanyPrefs.menubarIcon.put(bytes);
			} else {
				CompanyPrefs.menubarIcon.reset();
			}

		}

	}


}
