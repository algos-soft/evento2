package it.algos.webbase.web.field;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import it.algos.webbase.web.lib.DateConvertUtils;

import java.time.LocalDate;
import java.util.Date;

@SuppressWarnings("serial")
public class DateField extends com.vaadin.ui.DateField implements FieldInterface<Date> {

    public DateField() {
        this(null);
    }// end of constructor

    public DateField(String caption) {
        this(caption, null);
    }// end of constructor

    public DateField(String caption, Date value) {
        super(caption, value);
        init();
    }// end of constructor

    // restituisce la data corrente del giorno
    public static DateField oggi(String caption) {
        DateField dataCorrente = null;
        Date data = new Date();

        dataCorrente = new DateField(caption, data);

        return dataCorrente;
    }// end of method

    // restituisce la data del 1 gennaio dell'anno corrente
    public static DateField primoGennaio(String caption) {
        DateField dataCorrente = null;
        Date data = new Date();
        data.setDate(1);
        data.setMonth(0);

        // provvisorio
        dataCorrente = new DateField(caption, data);

        return dataCorrente;
    }// end of method

    private void init() {
        initField();
        setWidth("8em");
    }// end of method

    public void initField() {
        FieldUtil.initField(this);
    }// end of method

    public void setAlignment(FieldAlignment alignment) {
    }// end of method

//    public void setValue(LocalDate date)  {
//       setValue(DateConvertUtils.asUtilDate(date));
//    }

}// end of class
