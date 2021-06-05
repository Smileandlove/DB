package BNFCommands;

import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class AlterCommand implements Commands{
    private String name;
    private String columnName;
    private String alterationType;
    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        parser.checkString(listTokens.get(1),"TABLE");
        name = listTokens.get(2);
        parser.checkName(name);
        alterationType=listTokens.get(3);
        parser.checkName(listTokens.get(3));
        parser.checkAlterationType(alterationType);
        columnName= listTokens.get(4);
        parser.checkName(columnName);
        try {
            engine.alterTable(name,columnName,alterationType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
