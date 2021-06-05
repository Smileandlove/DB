package BNFCommands;

import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.util.ArrayList;


public class UseCommand implements Commands{
    private String name;
    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        name = listTokens.get(1);
        parser.checkName(name);
        engine.useDB(name);
    }
}
