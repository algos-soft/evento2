package it.algos.evento.info;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import it.algos.evento.EventoApp;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.module.Module;

@SuppressWarnings("serial")
public class InfoModulo extends Module {

	public InfoModulo() {
		super();

		Resource resource = new ThemeResource("info/info_programma.html");

		// personalizzazione Asteria per non mostrare il sito web
		BaseCompany company = BaseCompany.getCurrent();
		if (company.getCompanyCode().equals(EventoApp.ASTERIA_COMPANY_CODE)) {
			resource = new ThemeResource("info/info_programma_asteria.html");
		}
		// end personalizzazione Asteria

		Component comp = new BrowserFrame("Help", resource);
		comp.setWidth("100%");
		comp.setHeight("100%");
		setCompositionRoot(comp);

	}



}
