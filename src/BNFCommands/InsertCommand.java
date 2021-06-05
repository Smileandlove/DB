package BNFCommands;

import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class InsertCommand implements Commands{
    private String name;
    private ArrayList<String> values;
    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        parser.checkString(listTokens.get(1),"INTO");
        name = listTokens.get(2);
        parser.checkName(name);
        parser.checkString(listTokens.get(3),"VALUES");
        parser.checkBrackets(listTokens);
        int startIndex = listTokens.indexOf("(");
        int endIndex = listTokens.indexOf(")");
        values = parser.createValueList(startIndex+1,endIndex);
        try {
            engine.insertValues(name,values);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
