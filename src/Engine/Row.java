package Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.util.Set;

public class Row implements Serializable {
    protected final HashMap<String,String> rowValues;

    protected Row(){
        rowValues = new HashMap<>();
    }

    protected Row(ArrayList<String> columns, ArrayList<String> values){
        this();
        for (int i = 0; i < columns.size(); i++){
            rowValues.put(columns.get(i),values.get(i));
        }
    }

    public String selectValue(String column){
        return rowValues.get(column);
    }

}
