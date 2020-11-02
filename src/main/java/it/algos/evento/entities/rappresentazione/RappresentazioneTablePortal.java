package it.algos.evento.entities.rappresentazione;

import com.vaadin.data.Container;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.importexport.ExportProvider;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import java.util.Arrays;

@SuppressWarnings("serial")
public class RappresentazioneTablePortal extends TablePortal {

	public RappresentazioneTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	@Override
	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		MenuBar.MenuItem subItem;

		// bottone Altro...
		MenuBar.MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);

		item.addItem(Rappresentazione.CMD_EXPORT, Rappresentazione.ICON_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {

                Container container = getTable().getContainerDataSource();
                ExportProvider provider = new RappExportProvider();
                ExportConfiguration conf = new ExportConfiguration(Rappresentazione.class, "rappresentazioni.xls", container, provider);
                new ExportManager(conf, RappresentazioneTablePortal.this).show();

			}
		});// end of anonymous class

		subItem=item.addItem(Rappresentazione.CMD_PRENOTAZIONI_EXPORT, Rappresentazione.ICON_PRENOTAZIONI_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				doExport(Rappresentazione.CMD_PRENOTAZIONI_EXPORT);
			}// end of method
		});// end of anonymous class
		subItem.setDescription("Esporta il riepilogo delle prenotazioni per tutte le rappresentazioni selezionate");

		subItem=item.addItem(Rappresentazione.CMD_PARTECIPANTI_EXPORT, Rappresentazione.ICON_MEMO_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				doExport(Rappresentazione.CMD_PARTECIPANTI_EXPORT);
			}// end of method
		});// end of anonymous class
		subItem.setDescription("Esporta il riepilogo dei partecipanti per tutte le rappresentazioni in selezionate");



//		// bottone Test...
//		toolbar.addButton("Test Window...", FontAwesome.BARS, new MenuBar.Command() {
//			@Override
//			public void menuSelected(MenuItem menuItem) {
//				test();
//			}
//		});

		return toolbar;
	}// end of method


	private void test(){

        VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

        Component c1 = new Label("expanding component");
		c1.addStyleName("yellowBg");
		layout.addComponent(c1);

		c1.setHeight("100%");
		layout.setExpandRatio(c1,1);

		Component button = new Button("new window3", clickEvent -> {
            test();
        });
		button.setWidth("100%");
		layout.addComponent(button);

		Window window = new Window("My Window", layout);
        window.setSizeUndefined();

		getUI().addWindow(window);
//        window.setSizeFull();
//        window.setWidth(layout.getWidth(), layout.getWidthUnits());
//        window.setHeight("100px");
        window.center();

	}


	/**
	 * Controlla la selezione ed esegue l'esportazione.
	 * @param cmd la costante che identifica il tipo di esportazione
	 * */
	private void doExport(String cmd){
		BaseEntity[] entities = getTable().getSelectedEntities();
		if(entities.length>0) {
			Rappresentazione[] rapps = Arrays.copyOf(entities, entities.length, Rappresentazione[].class);

			if(cmd.equals(Rappresentazione.CMD_PRENOTAZIONI_EXPORT)){
				RappresentazioneModulo.esportaPrenotazioni(rapps);
			}

			if(cmd.equals(Rappresentazione.CMD_PARTECIPANTI_EXPORT)){
				RappresentazioneModulo.esportaPartecipanti(rapps);
			}


		}else{
			Notification.show("Devi selezionare le rappresentazioni da esportare");
		}
	}




}// end of class
