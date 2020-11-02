package it.algos.webbase.web.component;

import com.vaadin.data.Property;
import it.algos.webbase.web.field.CheckBoxField;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.HorizontalLayout;

import java.util.ArrayList;

/**
 * Component to display and edit a yes/no option
 */
@SuppressWarnings("serial")
public class YesNoCheckboxComponent extends HorizontalLayout {
	private CheckBoxField yesCheck;
	private CheckBoxField noCheck;
	ArrayList<ValueChangeListener> vclisteners = new ArrayList();

	/**
	 * Constructs a component showing 2 yes/no checkboxes
	 */
	public YesNoCheckboxComponent(String caption) {
		super();
		setSpacing(true);
		setCaption(caption);

		yesCheck = new CheckBoxField("Si");
		yesCheck.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (yesCheck.getValue()) {
					noCheck.setValue(false);
				}
				fireVcEvent(true);
			}
		});

		noCheck = new CheckBoxField("No");
		noCheck.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (noCheck.getValue()) {
					yesCheck.setValue(false);
				}
				fireVcEvent(false);
			}
		});

		addComponent(yesCheck);
		addComponent(noCheck);
	}

	/**
	 * Constructs a component showing 2 yes/no checkboxes
	 */
	public YesNoCheckboxComponent() {
		this(null);
	}

	/**
	 * @creates a filter to select to the currently displayed boolean state
	 * @return the filter
	 */
	public Container.Filter getFilter(String attributeId) {
		Filter filter = null;
		boolean yesBool = yesCheck.getValue();
		boolean noBool = noCheck.getValue();
		if (yesBool || noBool) {
			filter = new Compare.Equal(attributeId, yesBool);
		}
		return filter;
	}

	public void setValue(Boolean value){
		yesCheck.setValue(value);
		noCheck.setValue(!value);
	}

	/**
	 * @return the value, true, false, or null if no checks selected
	 */
	public Boolean getValue(){
		Boolean out=null;
		boolean yesValue = yesCheck.getValue();
		boolean noValue = noCheck.getValue();

		if(yesValue | noValue){
			out=yesValue;
		}
		return out;
	}

	public void setYesText(String text){
		yesCheck.setCaption(text);
	}

	public void setNoText(String text){
		noCheck.setCaption(text);
	}


	public void addValueChangeListener(ValueChangeListener valueChangeListener) {
		vclisteners.add(valueChangeListener);
	}

	private void fireVcEvent(Boolean newValue){

		Property.ValueChangeEvent event = new ValueChangeEvent() {
			@Override
			public Property getProperty() {
				return null;
			}
		};

		for(ValueChangeListener l : vclisteners){
			l.valueChange(event);
		}
	}
}
