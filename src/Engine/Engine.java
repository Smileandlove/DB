package Engine;

import DBCondition.Condition;
import DBException.QueryErrorException;

import java.io.*;
import java.util.ArrayList;

public class Engine {
    File currentDB;
    ArrayList<String> output;

    final String extension = ".tb";

    public Table getTableFromFile(File file) throws QueryErrorException,IOException{
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Table table = (Table) ois.readObject();
            ois.close();
            fis.close();
            return table;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new QueryErrorException("Couldn't get data from the file!");
        }
    }

    public void putTableToFile(Table table,File file) throws QueryErrorException {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(table);
            oos.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
            throw new QueryErrorException("Couldn't write data to the file!");
        }
    }

    //to serialize a table to the appropriate file path
    public File createTableFile(String name) throws QueryErrorException {
        if (currentDB == null){
            throw new QueryErrorException("No database now!");
        }
        String path = currentDB.getName() + File.separator + name + extension;
        return new File(path);
    }

    public void useDB(String name)throws QueryErrorException {
        currentDB = new File(name);
        if (!currentDB.exists()){
            throw new QueryErrorException("Unknown database");
        }
        output=defaultOutput();
    }

    public void createDB(String name)throws QueryErrorException {
        File db = new File(name);
        if (db.exists()){
            throw new QueryErrorException("Database '" + name + "' has exited!");
        }
        if (!db.mkdir()){
            throw new QueryErrorException("Couldn't create the new database!");
        }
        output=defaultOutput();
    }

    public void createTB(String name, ArrayList<String> columnNames) throws QueryErrorException,IOException{
        File tableFile = createTableFile(name);
        if (!tableFile.createNewFile()){
            throw new QueryErrorException("Table couldn't create!");
        }
        Table table = new Table(columnNames);
        putTableToFile(table,tableFile);
        output=defaultOutput();
    }

    public void dropTB(String name) throws QueryErrorException, IOException {
        File table = createTableFile(name);
        if (!table.delete()){
            throw new QueryErrorException("Table could not be dropped!");
        }
        output=defaultOutput();
    }

    public void dropDB(String name)throws QueryErrorException {
        File database = new File(name);
        if (!database.exists()){
            throw new QueryErrorException("The database '"+ name + "' doesn't exit!");
        }
        File[] tables = database.listFiles();
        if (tables==null){
            throw new QueryErrorException("No tables in this database!");
        }
        for (File table : tables){
            if (!table.delete()){
                throw new QueryErrorException("Couldn't delete table in database!");
            }
        }
        if (!database.delete()){
            throw new QueryErrorException("Couldn't delete this database!");
        }
        output=defaultOutput();
    }

    public void insertValues(String name, ArrayList<String> values)throws IOException,QueryErrorException{
        File tableFile = createTableFile(name);
        Table table = getTableFromFile(tableFile);
        table.addRow(values);
        putTableToFile(table,tableFile);
        output=defaultOutput();
    }

    public void selectFromTable(String name, ArrayList<String> columnNames, Condition condition)
            throws IOException,QueryErrorException{
        File tableFile = createTableFile(name);
        if (!tableFile.exists()){
            throw new QueryErrorException("Table does not exist");
        }
        Table table = getTableFromFile(tableFile);

        if (columnNames==null){
            columnNames=table.columnNames;
        }else{
            for (String column : columnNames){
                if (!table.columnNames.contains(column)){
                    throw new QueryErrorException("Attribute does not exist");
                }
            }
        }

        ArrayList<String> index = new ArrayList<>();
        StringBuilder out = new StringBuilder();
        for (String columnName:columnNames){
            out.append(columnName).append("\t");
        }
        index.add(out.toString());

        for (Row row : table.tableRows.values()){
            if (condition==null||condition.compareCondition(row)){
                out.setLength(0);
                for (String columnName : columnNames){
                    out.append(row.rowValues.get(columnName)).append("\t");
                }
                index.add(out.toString());
            }
        }
        output=index;
    }

    public void deleteRowFromTable(String name,Condition condition)throws QueryErrorException,IOException{
        File tableFile = createTableFile(name);
        Table table = getTableFromFile(tableFile);
        ArrayList<Integer> allRowIds = new ArrayList<>(table.tableRows.keySet());

        for (Integer i : allRowIds){
            if (condition.compareCondition(table.tableRows.get(i))){
                table.tableRows.remove(i,table.tableRows.get(i));
            }
        }

        putTableToFile(table,tableFile);
        output=defaultOutput();
    }

    public void updateToTable(String name, ArrayList<String> columnNames,
                              ArrayList<String>rowValues, Condition condition)throws QueryErrorException,IOException{
        File tableFile = createTableFile(name);
        Table table = getTableFromFile(tableFile);

        if (!table.columnNames.containsAll(columnNames)){
            throw new QueryErrorException("Couldn't find this column!");
        }

        for (Row row : table.tableRows.values()){
            if (condition.compareCondition(row)){
                for (int i = 0; i < columnNames.size() ; i++){
                    row.rowValues.replace(columnNames.get(i),rowValues.get(i));
                }
            }
        }

        putTableToFile(table,tableFile);
        output=defaultOutput();
    }

    public void alterTable(String name, String columnName,String alterationType)
            throws QueryErrorException,IOException{
        File tableFile = createTableFile(name);
        Table table = getTableFromFile(tableFile);

        if (alterationType.equalsIgnoreCase("ADD")){
            if (table.columnNames.contains(columnName)){
                throw new QueryErrorException("This column has existed!");
            }
            table.columnNames.add(columnName);
            for (Row row: table.tableRows.values()){
                row.rowValues.put(columnName,"");
            }
        }
        else if(alterationType.equalsIgnoreCase("DROP")){
            if (!table.columnNames.contains(columnName)){
                throw new QueryErrorException("This column has not existed!");
            }
            table.columnNames.remove(columnName);
            for (Row row: table.tableRows.values()){
                row.rowValues.remove(columnName);
            }
        }
        
        putTableToFile(table,tableFile);
        output=defaultOutput();
    }
    
    public void joinTables(String tableName1,String tableName2, String column1, String column2)
            throws IOException,QueryErrorException{
        File tableFile1 = createTableFile(tableName1);
        File tableFile2 = createTableFile(tableName2);
        Table table1 = getTableFromFile(tableFile1);
        Table table2 = getTableFromFile(tableFile2);

        Table joinTable = createJoinTable(table1,table2,tableName1,tableName2);
        for (Row row1:table1.tableRows.values()){
            for (Row row2:table2.tableRows.values()){
                if (row1.rowValues.get(column1).equals(row2.rowValues.get(column2))){
                    ArrayList<String> values = new ArrayList<>();
                    for (String column: table1.columnNames){
                        if (!column.equals("id")){
                            values.add(row1.rowValues.get(column));
                        }
                    }
                    for (String column:table2.columnNames){
                        if (!column.equals("id")){
                            values.add(row2.rowValues.get(column));
                        }
                    }
                    joinTable.addRow(values);
                }
            }
        }
        ArrayList<String> index = new ArrayList<>();
        StringBuilder out = new StringBuilder();
        for (String columnName:joinTable.columnNames){
            out.append(columnName).append("\t");
        }
        index.add(out.toString());

        for (Row row : joinTable.tableRows.values()){
                out.setLength(0);
                for (String columnName : joinTable.columnNames){
                    out.append(row.rowValues.get(columnName)).append("\t");
                }
                index.add(out.toString());
        }
        output=index;
    }

    public Table createJoinTable(Table table1, Table table2,String name1,String name2)throws QueryErrorException{
        ArrayList<String> newColumnNames = new ArrayList<>();
        for (String column : table1.columnNames){
            if (!column.equals("id")){
                newColumnNames.add(name1+"."+column);
            }
        }
        for (String column : table2.columnNames){
            if (!column.equals("id")){
                newColumnNames.add(name2+"."+column);
            }
        }

        return new Table(newColumnNames);
    }

    private ArrayList<String> defaultOutput() {
        ArrayList<String> out = new ArrayList<>();
        out.add("[OK]");
        return out;
    }

    public ArrayList<String> getOutput(){
        return output;
    }

}
