package it.algos.evento.ui.test;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alex on 19/12/15.
 */
public class TestUI extends UI {

    private final static Logger logger = Logger.getLogger(TestUI.class.getName());

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Button button = new Button("Close UI");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI ui = UI.getCurrent();
                logger.log(Level.INFO, "UI closed: " + ui);
                ui.close();
            }
        });

        setContent(button);


        int interval = VaadinService.getCurrent().getDeploymentConfiguration().getHeartbeatInterval();
        logger.log(Level.INFO, "HB Interval: " + interval+" sec");

        addDetachListener(new DetachListener() {
            @Override
            public void detach(DetachEvent detachEvent) {
                logger.log(Level.INFO, "TestUI detached: " + TestUI.this);
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    logger.log(Level.INFO, "Last Heartbeat: " + TestUI.this.getLastHeartbeatTimestamp() / 1000);
//                    logger.log(Level.INFO, "is closing: " + TestUI.this.isClosing());
//                }
//
//            }
//        }).start();

    }


//    @Override
//    public void detach() {
//        super.detach();
//        logger.log(Level.INFO, "Detach called: " + TestUI.this);
//    }


}
