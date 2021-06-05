package BNFCommands;

import DBCondition.Condition;
import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class DeleteCommand implements Commands{
    private String name;
    private Condition condition;

    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        parser.checkString(listTokens.get(1),"FROM");
        name = listTokens.get(2);
        parser.checkName(name);
        parser.checkString(listTokens.get(3),"WHERE");
        condition = parser.createCondition(listTokens,4);
        try {
            engine.deleteRowFromTable(name,condition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
