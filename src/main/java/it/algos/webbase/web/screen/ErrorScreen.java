package it.algos.webbase.web.screen;

import com.vaadin.server.Resource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;

/**
 * A view to show a standard error message to the user.
 * The app logo and a big red message below
 */
@SuppressWarnings("serial")
public class ErrorScreen extends VerticalLayout{

    private Label msgLabel;

    /**
     * @param errorMsg the error description
     */
    public ErrorScreen(String errorMsg) {
        super();
        Label label;

        setSizeFull();

        // logoComponent: horizontal layout (label left + image + label right)
        HorizontalLayout logoComponent = new HorizontalLayout();
        logoComponent.setWidth("100%");

        label = new Label();
        logoComponent.addComponent(label);
        logoComponent.setExpandRatio(label, 1.0f);

        Resource res = LibResource.getImgResource("splash_image.png");
        if (res!=null) {
            Image img = LibImage.getImage(res);
            logoComponent.addComponent(img);
        }

        label = new Label();
        logoComponent.addComponent(label);
        logoComponent.setExpandRatio(label, 1.0f);


        // msgComponent: (label left + msg label + label right)
        HorizontalLayout msgComponent = new HorizontalLayout();
        msgComponent.setWidth("100%");

        label = new Label();
        msgComponent.addComponent(label);
        msgComponent.setExpandRatio(label, 1.0f);
        //label.addStyleName("blueBg");


        msgLabel = new Label(errorMsg);
        msgComponent.addComponent(msgLabel);
        msgLabel.setWidthUndefined();
        msgLabel.addStyleName("error-screen-msg");


        label = new Label();
        msgComponent.addComponent(label);
        msgComponent.setExpandRatio(label, 1.0f);
        //label.addStyleName("blueBg");


        // add components to this vertical layout
        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);

        addComponent(logoComponent);

        label = new Label();
        addComponent(label);
        setExpandRatio(label, 0.3f);

        addComponent(msgComponent);

        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);


    }

    public ErrorScreen() {
        this("");
    }


    public void setMessage(String msg){
        msgLabel.setValue(msg);
    }

}
