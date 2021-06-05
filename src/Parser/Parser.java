package Parser;

import DBCondition.*;
import DBException.QueryErrorException;

import java.util.ArrayList;

public class Parser {
    int valueIndex;
    ArrayList<String> tokens;

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
        valueIndex=0;
    }

    public String getTokenAtIndex(){
        return tokens.get(valueIndex);
    }


    public Condition createCondition(ArrayList<String> tokens, int index)throws QueryErrorException {
        this.valueIndex=index;
        String attributeName = tokens.get(this.valueIndex);
        this.valueIndex++;
        String operator = tokens.get(this.valueIndex);
        Condition condition = createOperatorCondition(operator);
        this.valueIndex++;
        String value = checkValue();
        if (condition!=null){
            condition.setAttributeName(attributeName);
            condition.setValue(value);
        }
        return condition;
    }

    public Condition createOperatorCondition(String operator){
        switch (operator) {
            case "==":
                return new EqualCondition();
            case ">":
                return new GreaterCondition();
            case ">=":
                return new GreaterOrEqualCondition();
            case "<":
                return new LessCondition();
            case "<=":
                return new LessOrEqualCondition();
            case "LIKE":
                return new LikeCondition();
            case "!=":
                return new NotCondition();
            default:
                return null;
        }
    }

    public ArrayList<String> createValueList(int startIndex, int endIndex)throws QueryErrorException{
        ArrayList<String> valueList = new ArrayList<>();

        for (valueIndex = startIndex; valueIndex < endIndex; valueIndex++){
            if (!getTokenAtIndex().equals(",")){
                valueList.add(checkValue());
            }
        }
        return valueList;
    }

    public ArrayList<String> createAttributeList(int startIndex, int endIndex)throws QueryErrorException{
        ArrayList<String> AttributeList = new ArrayList<>();
        boolean checkCommas = false;
        int index = startIndex + 1;

        if (tokens.get(index).equals(",")){
            for (int i=startIndex; i < endIndex;i++){
                String token = tokens.get(i);
                if (token.equals(",")){
                    checkCommas = false;
                }
                if (checkCommas){
                    throw new QueryErrorException("Wrong numbers of commas!");
                }
                if (!tokens.get(i).equals(",")){
                    checkName(token);
                    AttributeList.add(token);
                    checkCommas = true;
                }
            }
        }else {
            if (!tokens.get(index).equals("FROM")){
                throw new QueryErrorException("Invalid query");
            }
            AttributeList.add(tokens.get(startIndex));
        }
        return AttributeList;
    }

    public void checkSemicolon(String token) throws QueryErrorException {
        if (!token.equals(";")){
            throw new QueryErrorException("No Semicolon");
        }
    }

    public void checkName(String token) throws QueryErrorException{
        String pattern = "^[a-zA-Z_]*$";
        if (!token.matches(pattern)){
            throw new QueryErrorException("Wrong name!");
        }
    }

    public void checkStructureName(String token) throws QueryErrorException{
        if (!token.equals("DATABASE") && !token.equals("TABLE")){
            throw new QueryErrorException("Structure type is wrong!");
        }
    }

    public void checkAlterationType(String token) throws QueryErrorException{
        if (!token.equals("ADD") && !token.equals("DROP")){
            throw new QueryErrorException("Command type is wrong!");
        }
    }

    public String checkValue() throws QueryErrorException{
        String token = tokens.get(valueIndex);
        boolean valueIsValid = false;

        if (token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false")){
            valueIsValid = true;
        }

        if (token.matches("[-+]?[0-9]*\\.[0-9]+") || token.matches("[-+]?[0-9]+")){
            valueIsValid = true;
        }

        if (token.equals("'")){
            valueIndex++;
            token = "";
            boolean checkSemicolon = false;
            boolean checkApostrophe = false;
            while (!checkApostrophe && !checkSemicolon){
                if (tokens.get(valueIndex).equals("'")){
                    valueIsValid=true;
                    checkApostrophe=true;
                }
                else if (tokens.get(valueIndex).equals(";")||tokens.get(valueIndex).equals(")")){
                    checkSemicolon=true;
                }
                else {
                    token=token.concat("\t");
                    token=token.concat(tokens.get(valueIndex));
                    valueIndex++;
                }
            }
            if (!checkApostrophe){
                throw new QueryErrorException("Invalid query");
            }
        }

        if (!valueIsValid){
            throw new QueryErrorException("Invalid value!");
        }
        return token;
    }

    public void checkString(String token,String signal) throws QueryErrorException {
        if (!token.equals(signal)){
            throw new QueryErrorException("Invalid query");
        }
    }

    public void checkBrackets(ArrayList<String> tokens) throws QueryErrorException{
        boolean checkLeft = false;
        boolean checkRight = false;
        int leftIndex = 0;
        int rightIndex = 0;

        for (String token:tokens){
            if (token.equals("(")){
                checkLeft=true;
                leftIndex++;
            }
            if (token.equals(")")){
                checkRight=true;
                rightIndex++;
            }
        }

        if (!checkLeft && !checkRight || leftIndex!=rightIndex) {
            throw new QueryErrorException("Wrong numbers of brackets!");
        }
    }
}
