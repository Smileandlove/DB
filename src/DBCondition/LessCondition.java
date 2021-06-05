package DBCondition;

import DBException.QueryErrorException;
import Engine.Row;

import java.util.regex.Pattern;

public class LessCondition implements Condition{
    private String attributeName;
    private int value;

    public final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public boolean compareCondition(Row row) throws QueryErrorException {
        if (!checkNumber(row)){
            throw new QueryErrorException("This isn't number!");
        }

        return Integer.parseInt(row.selectValue(attributeName)) < value;
    }


    public void setAttributeName(String attributeName) {
        this.attributeName=attributeName;
    }


    public void setValue(String value) throws QueryErrorException {
        this.value=Integer.parseInt(value);
    }


    public String getAttributeName() {
        return attributeName;
    }

    public boolean checkNumber(Row row){
        return pattern.matcher(row.selectValue(attributeName)).matches();
    }
}
