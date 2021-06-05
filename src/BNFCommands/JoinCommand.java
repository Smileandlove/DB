package BNFCommands;

import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class JoinCommand implements Commands{
    private String tableName1;
    private String tableName2;
    private String column1;
    private String column2;

    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        tableName1 = listTokens.get(1);
        parser.checkName(tableName1);
        parser.checkString(listTokens.get(2),"AND");
        tableName2 = listTokens.get(3);
        parser.checkName(tableName2);
        parser.checkString(listTokens.get(4),"ON");
        column1 = listTokens.get(5);
        parser.checkName(column1);
        parser.checkString(listTokens.get(6),"AND");
        column2 = listTokens.get(7);
        parser.checkName(column2);
        try {
            engine.joinTables(tableName1,tableName2,column1,column2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
