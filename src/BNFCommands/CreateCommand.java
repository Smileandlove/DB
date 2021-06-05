package BNFCommands;

import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;


public class CreateCommand implements Commands{
    private ArrayList<String> columnNames;

    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        int endIndex = listTokens.size()-1;
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));
        String structure = listTokens.get(1);
        parser.checkStructureName(structure);
        String name = listTokens.get(2);
        parser.checkName(name);
        if (listTokens.get(3).equals("(")){
           columnNames = parser.createAttributeList(4,endIndex-1);
        }
        if (structure.equals("DATABASE")){
            engine.createDB(name);
        }
        if (structure.equals("TABLE")){
            try {
                engine.createTB(name,columnNames);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
