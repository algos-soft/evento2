package it.algos.webbase.web.entity;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by alex on 17-12-2015.
 * Annotation to declare default sort fields in BaseEntities.
 * Pass an array of strings with the property names.
 * Each string can be composed by property name only, or property name and direction flag.
 * e.g. "name" or "name, false" for descending sort direction.
 * Default sort direction is ascending.
 * Usage examples:
 * @DefaultSort({"sex, false", "lastname, true","firstname, true"})
 * sorts by sex(descending), lastname(ascending), and firstname(ascending)
 * @DefaultSort({"lastname","firstname, false"})
 * sorts by lastname(ascending), and firstname(descending)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class only.
public @interface DefaultSort {

    String[] value() default {};
    
}
