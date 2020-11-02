package it.algos.webbase.web.dialog;

import it.algos.webbase.web.field.ArrayComboField;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PopDialog extends ConfirmDialog {

	private PopDialogSelectedListener listener;
	private Object[] values;
	private ArrayComboField combo;

	public PopDialog(PopDialogSelectedListener listener) {
		this("", "", (Object[]) null, listener);
	}// end of constructor

	public PopDialog(String title, String message, PopDialogSelectedListener listener) {
		this(title, message, (Object[]) null, listener);
	}// end of constructor

	public PopDialog(Object[] values, PopDialogSelectedListener listener) {
		this("", "", values, listener);
	}// end of constructor

	public PopDialog(String title, Object[] values, PopDialogSelectedListener listener) {
		this(title, "", values, listener);
	}// end of constructor

	public PopDialog(String title, String message, ArrayList<?> values, PopDialogSelectedListener listener) {
		this(title, message, (Object[]) values.toArray(), listener);
	}// end of constructor

	public PopDialog(String title, String message, Object[] values, PopDialogSelectedListener listener) {
		super(title, message, null);
		this.setValues(values);
		this.setListener(listener);
		addPopValues();
	}// end of constructor

	/**
	 * The component shown in the detail area.
	 */
	protected void addPopValues() {
		Object[] values = getValues();
		ArrayComboField combo = null;

		if (values != null) {
			combo = new ArrayComboField(values);
			combo.setWidth("250px");
			addComponent(combo);
			this.setCombo(combo);
		}// end of if cycle
	}// end of method

	/**
	 * @return the values
	 */
	public Object[] getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(Object[] values) {
		this.values = values;
	}

	/**
	 * @return the valueListener
	 */
	public PopDialogSelectedListener getListener() {
		return listener;
	}

	/**
	 * @param listener the valueListener to set
	 */
	public void setListener(PopDialogSelectedListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the combo
	 */
	public ArrayComboField getCombo() {
		return combo;
	}

	/**
	 * @param combo
	 *            the combo to set
	 */
	public void setCombo(ArrayComboField combo) {
		this.combo = combo;
	}

	protected void onCancel() {
		if (listener != null) {
			listener.onPopupSelected("");
		}// end of if cycle
		close();
	}// end of method

	protected void onConfirm() {
		Object value = "";
		ArrayComboField combo = getCombo();

		if (combo != null) {
			value = combo.getValue();
		}// end of if cycle

		if (listener != null) {
			listener.onPopupSelected(value);
		}// end of if cycle
		close();
	}// end of method

	public interface PopDialogSelectedListener {
		public void onPopupSelected(Object valueSelected);
	}// end of method

}// end of class
