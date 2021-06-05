package BNFCommands;

import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class DropCommand implements Commands{
    private String name;
    private String structure;
    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        structure = listTokens.get(1);
        parser.checkStructureName(structure);
        name = listTokens.get(2);
        parser.checkName(name);
        if (structure.equals("DATABASE")){
            engine.dropDB(name);
        }
        if (structure.equals("TABLE")){
            try {
                engine.dropTB(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
