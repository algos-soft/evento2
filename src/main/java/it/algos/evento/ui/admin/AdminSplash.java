package it.algos.evento.ui.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;


@SuppressWarnings("serial")
public class AdminSplash extends VerticalLayout implements View {


    private Resource res;

    public AdminSplash() {
        super();
        this.res = LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME, "splash_image.png");
        setWidth("100%");
        setHeight("100%");

        addComponent(createUI());

    }


    private Component createUI() {
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

    }
}
