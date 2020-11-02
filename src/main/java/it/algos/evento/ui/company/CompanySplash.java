package it.algos.evento.ui.company;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.lib.LibImage;

/**
 * Splash screen della Company.
 * E' costituito dalla dashboard e dal logo.
 */
@SuppressWarnings("serial")
public class CompanySplash extends VerticalLayout implements View {


    private CompanyHome home;
    private CompanyDashboard dashboard;

    public CompanySplash() {
        this(null);
    }

    public CompanySplash(CompanyHome home) {
        super();
        this.home = home;
        setWidth("100%");
        setHeight("100%");

        // crea la UI
        // personalizzazione Asteria per non fornire la dashboard
        Component comp;
        if (BaseCompany.getCurrent().getCompanyCode().equals(EventoApp.ASTERIA_COMPANY_CODE)) {
//            comp = createUI();
            comp = createUIComponentOld();
        } else {
            comp = createUI();
        }
        addComponent(comp);

    }

    private Component createUI() {
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("100%");
        dashboard = new CompanyDashboard(home);
        dashboard.setSizeFull();
        hLayout.addComponent(dashboard);
//        dashboard.setWidth("100%");
//        hLayout.setExpandRatio(dashboard, 1);

//        Component logo = createLogo();
//        hLayout.addComponent(logo);
//        hLayout.setExpandRatio(logo, 0);

        return hLayout;
    }



    private Component createUIComponentOld() {
        Label label;

        VerticalLayout main = new VerticalLayout();
        main.setWidth("100%");
        main.setHeight("100%");

        // horizontal: label left + image + label right
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        Resource res= CompanyPrefs.splashImage.getResource();
        if (res != null) {
            Image img = LibImage.getImage(res);
            layout.addComponent(img);
        }

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        // vertical: label top + image layout + label bottom
        label = new Label();
        main.addComponent(label);
        main.setExpandRatio(label, 1.0f);

        main.addComponent(layout);

        label = new Label();
        main.addComponent(label);
        main.setExpandRatio(label, 1.0f);

        return main;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(dashboard!=null){
            dashboard.update();
        }
    }

}
