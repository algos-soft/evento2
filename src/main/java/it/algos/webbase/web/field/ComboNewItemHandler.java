package it.algos.webbase.web.field;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.AForm.FormListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.metamodel.Attribute;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.webbase.web.module.ModulePop;

/**
 * Handler for new creating new items from a RelatedComboField. <br>
 * When a new string is typed in the combo, the addNewItem method is called and the string is received. <br>
 * Then, a form is displayed to fill the data for the new item. <br>
 * When the for is confirmed, the new item is saved and the combo is updated.
 */
@SuppressWarnings("serial")
public class ComboNewItemHandler implements NewItemHandler {

	private ArrayList<RecordEditedListener> listeners = new ArrayList<RecordEditedListener>();

	private final static Logger logger = Logger.getLogger(ComboNewItemHandler.class.getName());

	private RelatedComboField field;
	private Class<? extends AForm> formClass;
	@SuppressWarnings("rawtypes")
	private Attribute attribute;

	/**
	 * Creates a new NewItemHandler
	 * 
	 * @param field
	 *            the referenced field
	 * @param formClass
	 *            - the class for the form which will be used for editing the new item (must subclass AForm)
	 * @param attribute
	 *            - the attribute to fill with the text coming from the combo
	 */
	@SuppressWarnings({ "rawtypes" })
	public ComboNewItemHandler(RelatedComboField field, Class<? extends AForm> formClass, Attribute attribute) {
		super();
		this.field = field;
		this.formClass = formClass;
		this.attribute = attribute;

		// add a listener, invoked when the editing is committed
		addRecordEditedListener(new ComboNewItemHandler.RecordEditedListener() {

			@Override
			public void save_(Item item, boolean newRecord) {
				field.fire(item, newRecord);
			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addNewItem(String newItemCaption) {

		// create the new item
		Object bean = createBean();
		BeanItem item = new BeanItem(bean);
//		Object itemId=field.getContainerDataSource().addItem();
//		Item item=field.getContainerDataSource().getItem(itemId);

		// write the caption to the appropriate property
		if(newItemCaption!=null){
			if (attribute != null) {
				Property prop = item.getItemProperty(attribute.getName());
				if (prop != null) {
					prop.setValue(newItemCaption);
				}
			}
		}

		// edit the item in the form
		editItem(item, true);


	}

	/**
	 * Creates the new empty bean to be shown in the edit form.
	 * Override this in a subclass to provide default values.
	 * @return the bean
	 */
	protected Object createBean(){
		 return BaseEntity.createBean(this.field.getEntityClass());
	}
	
	
	protected void editItem(Item item, final boolean newRecord){

		// check that the form class exists
		if (formClass == null) {
			logger.log(Level.WARNING, "The class for the form is null.");
			return;
		}

		// instantiate the form from the form class by reflection
		AForm form = instantiateForm(item);
		if(form==null){
			logger.log(Level.WARNING, "The form could not be instantiated from class "+formClass.getSimpleName());
			return;
		}

		// create the window
		final Window window = new Window();

		// adds listeners to the form
		form.addFormListener(new FormListener() {

			@Override
			public void commit_() {

				Item item = form.getItem();
				BaseEntity bean = itemToBean(item);
				bean.save(field.getEntityManager());
				window.close();

				Container cont = field.getContainerDataSource();

				if (cont instanceof JPAContainer) {
					JPAContainer jpac=(JPAContainer)cont;
					jpac.refresh();
				}

				// select the newly created entity in the combo
				field.setValue(bean.getId());

				fire(item, newRecord);
			}

			@Override
			public void cancel_() {
				window.close();
			}

		});

		// show the UI
		window.setCaption(""+field.getEntityClass().getSimpleName());
		window.setResizable(false);
		window.setModal(true);
		form.setWidth("100%");
		form.setHeightUndefined();
		window.setContent(form);
		window.center();
		UI ui = UI.getCurrent();
		ui.addWindow(window);
	}


	/**
	 * Instantiate the form by reflection from the given form class.
	 * @param item - the item to be edited
	 * @return the form object
	 */
	private AForm instantiateForm(Item item){
		AForm form=null;

		Constructor constr=null;
		Object[] args=null;

		// retrieve a constructor with Item
		try {
			constr = formClass.getConstructor(Item.class);
			args = new Object[] { item };
		} catch (NoSuchMethodException e) {
			// if not found, retrieve a constructor with Item and Module
			try {
				constr = formClass.getConstructor(Item.class, ModulePop.class);
				args = new Object[] { item , null };
			} catch (NoSuchMethodException e1) {
				logger.log(Level.WARNING, "Can't find suitable Constructor.", e);
				return null;
			}
		}

		// instantiate the form by reflection
		try {
			form = (AForm) constr.newInstance(args);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.log(Level.WARNING, "Can't instantiate the Form.", e);
		}

		return form;
	}


	public void edit(BeanItem bi){
		editItem(bi, false);
	}

	/**
	 * Extract the Bean from a BeanItem
	 */
	@SuppressWarnings("rawtypes")
	public BaseEntity itemToBean(Item item) {
		BaseEntity entity = null;
		if (item instanceof BeanItem) {
			Object bean = ((BeanItem) item).getBean();
			if (bean instanceof BaseEntity) {
				entity = (BaseEntity) bean;
			}

		}
		return entity;
	}

	public void addRecordEditedListener(RecordEditedListener listener) {
		this.listeners.add(listener);
	}// end of method
	
	protected void fire(Item item, boolean newRecord) {
		for (RecordEditedListener l : listeners) {
			l.save_(item, newRecord);
		}
	}// end of method

	
	public interface RecordEditedListener {
		public void save_(Item item, boolean newRecord);
	}

}
