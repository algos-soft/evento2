package it.algos.evento.config;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public abstract class BaseConfigPanel extends FormLayout implements ConfigComponent {

	private FieldGroup group;
	protected PrefSetItem item;

	public BaseConfigPanel() {
		super();

		setMargin(true);
		setSpacing(true);

		group = new FieldGroup();

	}

	public abstract PrefSetItem createItem();

	/**
	 * Create the button panel
	 */
	protected Component createButtonPanel() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		Button bSave = new Button("Registra");
		bSave.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				try {
					group.commit();
					item.persist();
				} catch (CommitException e) {
					e.printStackTrace();
				}
			}
		});
		layout.addComponent(bSave);
		
		
		return layout;

	}

	public FieldGroup getGroup() {
		return group;
	}

	interface PrefSetItem extends Item {

		/**
		 * Persists the item properties to the storage
		 */
		public void persist();

	}

	@Override
	public void loadContent() {
		item = createItem();
		group.setItemDataSource(item);
	}
	
	@Override
	public Component getUIComponent() {
		return this;
	}

	public PrefSetItem getItem() {
		return item;
	}
}
