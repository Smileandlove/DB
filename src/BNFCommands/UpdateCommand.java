package BNFCommands;

import DBCondition.Condition;
import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateCommand implements Commands{
    private String name;
    private ArrayList<String> columnName;
    private ArrayList<String> value;
    private Condition condition;

    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        columnName = new ArrayList<>();
        value = new ArrayList<>();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        name = listTokens.get(1);
        parser.checkName(name);
        parser.checkString(listTokens.get(2),"SET");
        columnName.add(listTokens.get(3));
        parser.checkName(listTokens.get(3));
        value.add(listTokens.get(5));
        int index = listTokens.indexOf("WHERE");
        if (index == -1){
            throw new QueryErrorException("No WHERE!");
        }
        index++;
        condition=parser.createCondition(listTokens,index);
        try {
            engine.updateToTable(name,columnName,value,condition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
