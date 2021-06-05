package Engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import DBException.QueryErrorException;


public class Table implements Serializable{
    ArrayList<String> columnNames;
    HashMap<Integer, Row> tableRows;
    Integer rowId;

    protected Table(){
        columnNames = new ArrayList<>();
        tableRows = new HashMap<>();
        rowId = 0;
    }

    protected Table(ArrayList<String> columnName)throws QueryErrorException{
        this();
        if (columnName==null){
            throw new QueryErrorException("No column name!");
        }
        if (!columnName.get(0).equals("id")){
            this.columnNames.add("id");
        }
        this.columnNames.addAll(columnName);
    }

    public void addRow(ArrayList<String> values) throws QueryErrorException {
        if (values.size()!=columnNames.size()-1) {
            throw new QueryErrorException("Wrong size of the row!");
        }
        rowId++;
        ArrayList<String> allValues = new ArrayList<>();
        allValues.add(Integer.toString(rowId));
        allValues.addAll(values);
        tableRows.put(rowId,new Row(columnNames,allValues));
    }

}
