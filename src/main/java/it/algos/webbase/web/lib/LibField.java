package it.algos.webbase.web.lib;

import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.field.*;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by gac on 18 ott 2016.
 * Creazione dei field dalla annotation
 */
public class LibField {

    /**
     * Create a single field.
     * The field type is chosen according to the annotation.
     *
     * @param attr the metamodel Attribute
     */
    @SuppressWarnings("all")
    public static Field createField(Attribute attr) {
        Field vaadinField = null;
        java.lang.reflect.Field javaField = null;
        Annotation annotation = null;
        String fieldType = "";
        AIField fieldAnnotation = null;

        try { // prova ad eseguire il codice
            javaField = attr.getJavaMember().getDeclaringClass().getDeclaredField(attr.getName());
        } catch (Exception unErrore) { // intercetta l'errore
            return createFieldJavaType(attr);
        }// fine del blocco try-catch

        if (javaField != null) {
            annotation = javaField.getAnnotation(AIField.class);
        } else {
            return createFieldJavaType(attr);
        }// end of if/else cycle

        if (annotation != null && annotation instanceof AIField) {
            fieldAnnotation = (AIField) annotation;
        } else {
            return createFieldJavaType(attr);
        }// end of if/else cycle

        if (fieldAnnotation != null) {
            switch (fieldAnnotation.type()) {
                case text:
                    vaadinField = new TextField();
                    ((TextField) vaadinField).setInputPrompt(fieldAnnotation.prompt());
                    ((TextField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((TextField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case integer:
                    vaadinField = new IntegerField();
                    ((IntegerField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((IntegerField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case email:
                    vaadinField = new EmailField();
                    ((EmailField) vaadinField).setInputPrompt(fieldAnnotation.prompt());
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((EmailField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case checkbox:
                    vaadinField = new CheckBoxField();
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((CheckBoxField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case area:
                    vaadinField = new TextArea();
                    ((TextArea) vaadinField).setInputPrompt(fieldAnnotation.prompt());
                    ((TextArea) vaadinField).setColumns(fieldAnnotation.columns());
                    ((TextArea) vaadinField).setRows(fieldAnnotation.rows());
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((TextArea) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case date:
                    vaadinField = new DateField();
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((DateField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case time:
                    vaadinField = new DateField();
                    vaadinField.setWidth("220px");
                    ((DateField) vaadinField).setResolution(DateField.RESOLUTION_SEC);
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((DateField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case password:
                    vaadinField = new PasswordField();
                    ((PasswordField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((PasswordField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case combo:
                    vaadinField = new RelatedComboField(fieldAnnotation.clazz());
                    ((RelatedComboField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    ((RelatedComboField) vaadinField).setNullSelectionAllowed(fieldAnnotation.nullSelectionAllowed());
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((RelatedComboField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                case enumeration:
                    Class clazz = fieldAnnotation.clazz();
                    Object[] values = clazz.getEnumConstants();
                    vaadinField = new ArrayComboField(values);
                    ((ArrayComboField) vaadinField).setEnabled(fieldAnnotation.enabled());
                    ((ArrayComboField) vaadinField).setNullSelectionAllowed(fieldAnnotation.nullSelectionAllowed());
                    if (AlgosApp.DISPLAY_TOOLTIPS) {
                        ((ArrayComboField) vaadinField).setDescription(fieldAnnotation.help());
                    }// end of if cycle
                    break;
                default: // caso non definito
                    vaadinField = createFieldJavaType(attr);
            } // fine del blocco switch

            vaadinField.setRequired(fieldAnnotation.required());
            vaadinField.setCaption(fieldAnnotation.caption());
            vaadinField.setWidth(fieldAnnotation.width());
            vaadinField.setRequiredError(fieldAnnotation.error());

            return vaadinField;
        } else {
            return createFieldJavaType(attr);
        }// end of if/else cycle
    }// end of static method

    /**
     * Create a single field.
     * The field type is chosen according to the Java type.
     *
     * @param attr the metamodel Attribute
     */
    public static Field createFieldJavaType(Attribute attr) {
        Field field = null;

        if (attr != null) {


            try {

                if (attr.isAssociation()) { // questo può fallire!!

                    // relazione OneToMany - usiamo un campo lista (?)
                    if (attr instanceof PluralAttribute) {
                        PluralAttribute pa = (PluralAttribute) attr;
                        Class clazz = pa.getElementType().getJavaType();
                        //field = new RelatedComboField(clazz);
                        //field=null;// todo qui ci vuole una lista
                    }

                    // relazione ManyToOne - usiamo un campo combo
                    if (attr instanceof SingularAttribute) {
                        SingularAttribute sa = (SingularAttribute) attr;
                        Class clazz = sa.getJavaType();
                        field = new RelatedComboField(clazz);
                    }

                } else {

                    Class clazz = attr.getJavaType();

                    if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                        field = new CheckBoxField();
                    }

                    if (clazz.equals(String.class)) {
                        field = new TextField();
                    }

                    if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                        field = new IntegerField();
                    }

                    if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                        field = new LongField();
                    }

                    if (clazz.equals(Date.class)) {
                        field = new DateField();
                    }

                    if (clazz.equals(BigDecimal.class)) {
                        field = new DecimalField();
                    }

                    if (clazz.equals(Timestamp.class)) {
                        field = new DateField();
                    }

                    if (clazz.isEnum()) {
                        Object[] values = clazz.getEnumConstants();
                        ArrayComboField acf = new ArrayComboField(values);
                        acf.setNullSelectionAllowed(true);
                        field = acf;
                    }

                }

                // create and assign the caption
                if (field != null) {
                    String caption = DefaultFieldFactory.createCaptionByPropertyId(attr.getName());
                    field.setCaption(caption);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return field;

    }// end of static method

    /**
     * Restituisce una singola Annotation.
     *
     * @param attr the metamodel Attribute
     * @return valore dell'Annotation - Valore di default, se non la trova
     */
    public static AIField getAnnotation(Attribute attr) {
        AIField fieldAnnotation = null;
        java.lang.reflect.Field javaField = null;
        Annotation annotation = null;

        try { // prova ad eseguire il codice
            javaField = attr.getJavaMember().getDeclaringClass().getDeclaredField(attr.getName());
            annotation = javaField.getAnnotation(AIField.class);
            fieldAnnotation = (AIField) annotation;
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return fieldAnnotation;
    }// end of static method

}// end of abstract static class
