package BNFCommands;

import DBCondition.Condition;
import DBCondition.JoinCondition;
import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class SelectCommand implements Commands{
    private String name;
    private ArrayList<String> columnNames;
    private Condition condition;
    @Override
    public void interpret(Parser parser, Engine engine) throws QueryErrorException {
        ArrayList<String> listTokens = parser.getTokens();
        parser.checkSemicolon(listTokens.get(listTokens.size()-1));

        int index = listTokens.indexOf("FROM");
        if (listTokens.get(1).equals("*")){
            columnNames = null;
        }else {
            columnNames=parser.createAttributeList(1,listTokens.size()-1);
        }
        index++;
        name = listTokens.get(index);
        parser.checkName(name);
        index++;
        if (!listTokens.get(index).equals(";")){
            parser.checkString(listTokens.get(index),"WHERE");
            index++;
            if (!listTokens.get(index).equals("(")){
                condition = parser.createCondition(listTokens,index);
            }else {
                ArrayList<Condition> listConditions = new ArrayList<>();
                ArrayList<String> relationships = new ArrayList<>();
                for (int i = index;i<listTokens.size()-1;i++){
                    String token = listTokens.get(i);
                    if (token.equals("AND") || token.equals("OR")){
                        relationships.add(token);
                    }else if (!token.equals("(") && !token.equals(")") && !token.equals("'")){
                        Condition condition = parser.createCondition(listTokens, i);
                        i = i+3;
                        if(listTokens.get(i).equals(")")) {
                            i--;
                        }
                        listConditions.add(condition);
                    }
                }
                JoinCondition joinCondition = new JoinCondition(listConditions.get(0),
                        listConditions.get(1),relationships.get(0));
                for (int j = 2;j<listConditions.size();j++){
                        joinCondition = new JoinCondition(joinCondition,listConditions.get(j),relationships.get(j-1));
                }
                condition = joinCondition;
            }
        }
        else {
            condition = null;
        }

        try {
            engine.selectFromTable(name,columnNames,condition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
