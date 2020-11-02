package it.algos.webbase.web.component;

import com.vaadin.ui.FormLayout;
import it.algos.webbase.web.field.DateField;

import java.util.Date;

import org.joda.time.DateTime;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.HorizontalLayout;

/**
 * Component to display and edit a date range.
 * <p>
 * This components displays 2 editable dates.<br>
 * You can read and set both dates with getDate1(), getDate2(), setDate1(), setDate2()<br>
 * When the first date changes, onFirstDateChanged() method is invoked. By default this method sets the second date
 * equal to the first.<br>
 * You can override onFirstDateChanged() method if you need a different behaviour<br>
 * You can also completely disable this behaviour by setting setListenToFirstDateChanged(false).
 */
@SuppressWarnings("serial")
public class DateRangeComponent extends HorizontalLayout {

	private static final boolean DEBUG_GUI = false;

	private DateField fData1;
	private DateField fData2;
	private boolean listenToFirstDateChanged = true;

	/**
	 * Constructs a component showing 2 dates
	 */
	public DateRangeComponent() {
		this((String) null);
	}// end of constructor

	/**
	 * Constructs a component showing 2 dates
	 */
	public DateRangeComponent(boolean usaGennaioOggi) {
		this((String) null, usaGennaioOggi, false);
	}// end of constructor

	/**
	 * Constructs a component showing 2 dates
	 */
	public DateRangeComponent(String caption) {
		this(caption, false, false);
	}// end of constructor

	/**
	 * Constructs a component showing 2 dates
	 * @param caption for the component
	 * @param usaGennaioOggi to show dates 1st Jan - today
	 *  @param labelsLeft true for the labels on the left instead of top
	 */
	public DateRangeComponent(String caption, boolean usaGennaioOggi, boolean labelsLeft) {
		super();
		setSpacing(true);
		setCaption(caption);


		if (DEBUG_GUI) {
			this.addStyleName("greenBg");
		}// end of if cycle

		// Spesso non è desiderabile avere le date preimpostate.
		// Per esempio, impedisce di trovare tutti i record semplicemente premendo
		// ricerca - ok, inoltre se la gestione non va ad anno solare
		// non porta beneficio.
		// Questo comportamento eventualmente lo metterei opzionale e non di default.
		// alex 12/09/2014
		// fData1 = DateField.primoGennaio("Dal");
		if (usaGennaioOggi) {
			fData1 = DateField.primoGennaio("Dal");
			fData2 = DateField.oggi("Al");
		} else {
			fData1 = new DateField("Dal");
			fData2 = new DateField("Al");

		}// end of if/else cycle

		// adds a listener to first date changes
		fData1.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (isListenToFirstDateChanged()) {
					onFirstDateChanged();
				}// end of if cycle
			}// end of inner method
		});// end of anonymous inner class

		// labels left or labels above
		if(labelsLeft){
			FormLayout l1 = new FormLayout();
			l1.setMargin(false);
			l1.addComponent(fData1);
			FormLayout l2 = new FormLayout();
			l2.setMargin(false);
			l2.addComponent(fData2);
			addComponent(l1);
			addComponent(l2);
		}else{
			addComponent(fData1);
			addComponent(fData2);
		}


	}// end of constructor

	/**
	 * Invoked when the first date changes.
	 * <p>
	 * By default, sets the second date equal to the first.<br>
	 * Overide if you need a different behaviour.
	 */
	protected void onFirstDateChanged() {
		fData2.setValue(fData1.getValue());
	}// end of method

	/**
	 * @creates a filter to select to the currently displayed date range
	 * @return the filter
	 */
	public Container.Filter getFilter(String attributeId) {
		Filter filter = null;
		Object obj;
		Date date;

		Filter fLow = null;
		obj = fData1.getValue();
		if (obj != null) {
			date = (Date) obj;
			fLow = new Compare.GreaterOrEqual(attributeId, date);
		}// end of if cycle

		Filter fHigh = null;
		obj = fData2.getValue();
		if (obj != null) {
			date = (Date) obj;
			DateTime jDate = new DateTime(date).withTimeAtStartOfDay().plusDays(1).minusMillis(1);
			Date dateHigh = jDate.toDate();
			fHigh = new Compare.LessOrEqual(attributeId, dateHigh);
		}// end of if cycle

		// both filter are null, return null
		if ((fLow == null) && (fHigh == null)) {
			return null;
		}// end of if cycle

		// both filters are valid
		if ((fLow != null) && (fHigh != null)) {
			filter = new And(fLow, fHigh);
			return filter;
		}// end of if cycle

		// low is valid and high is null
		if ((fLow != null) && (fHigh == null)) {
			return fLow;
		}// end of if cycle

		// high is valid and low is null
		if ((fHigh != null) && (fLow == null)) {
			return fHigh;
		}// end of if cycle

		return null; // statement never reached

	}// end of method

	public Date getDate1() {
		return fData1.getValue();
	}// end of method

	public Date getDate2() {
		return fData2.getValue();
	}// end of method

	public void setDate1(Date newFieldValue) {
		fData1.setValue(newFieldValue);
	}// end of method

	public void setDate2(Date newFieldValue) {
		fData2.setValue(newFieldValue);
	}// end of method

	public boolean isListenToFirstDateChanged() {
		return listenToFirstDateChanged;
	}// end of method

	public void setListenToFirstDateChanged(boolean listenToFirstDateChanged) {
		this.listenToFirstDateChanged = listenToFirstDateChanged;
	}// end of method

}// end of class
