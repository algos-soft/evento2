package it.algos.webbase.web.entity;

import java.util.ArrayList;

/**
 * Class to wrap sort properties and sort directions.
 * Created by alex on 18/12/15.
 */
public class SortProperties{

    private ArrayList<Object> properties=new ArrayList();
    private ArrayList<Boolean> directions=new ArrayList();

    public void addProperty(Object property, boolean direction){
        properties.add(property);
        directions.add(direction);
    }

    public Object[] getProperties(){
        return properties.toArray(new Object[0]);
    };

    public boolean[] getDirections(){
        boolean[] bools=new boolean[directions.size()];
        for(int i=0; i<bools.length; i++){
            bools[i]=directions.get(i);
        }
        return bools;
    };

    public boolean isEmpty(){
        return properties.size()==0;
    }

}
