package it.algos.webbase.web.component;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import it.algos.webbase.web.field.IntegerField;

/**
 * Editor di ore e minuti
 */
public class HHMMComponent extends HorizontalLayout {

    private IntegerField fHours;
    private IntegerField fMinutes;

    public HHMMComponent(String caption, int h, int m) {
        setSpacing(true);

        fHours = new IntegerField();
        fHours.setWidth("2.5em");
        fHours.setValue(h);

        fMinutes = new IntegerField();
        fMinutes.setWidth("2.5em");
        fMinutes.setValue(m);

        addComponent(fHours);
        Label label=new Label(":");
        addComponent(label);
        addComponent(fMinutes);

        setComponentAlignment(label, Alignment.MIDDLE_CENTER);

        setCaption(caption);

    }

    public HHMMComponent(String caption) {
        this(caption, 0,0);
    }

    public HHMMComponent(int h, int m) {
        this(null, h,m);
    }

    public HHMMComponent() {
        this(null, 0,0);
    }


    public void setHours(int hours) {
        fHours.setValue(hours);
    }

    public void setMinutes(int minutes) {
        fMinutes.setValue(minutes);
    }

    public int getHours(){
        return fHours.getValue();
    }

    public int getMinutes(){
        return fMinutes.getValue();
    }


    public void setHoursMinutes(int minutes) {
        int hours = (int)(minutes/60);
        int mins=minutes%60;
        setHours(hours);
        setMinutes(mins);
    }


    public int getTotalMinutes(){
        return (getHours()*60)+getMinutes();
    }


}
