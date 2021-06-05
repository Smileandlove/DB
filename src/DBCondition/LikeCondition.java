package DBCondition;

import DBException.QueryErrorException;
import Engine.Row;

import java.util.regex.Pattern;

public class LikeCondition implements Condition{
    private String attributeName;
    private String value;

    public final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    @Override
    public boolean compareCondition(Row row) {
        return row.selectValue(attributeName).contains(value);
    }


    public void setAttributeName(String attributeName) {
        this.attributeName=attributeName;
    }


    public void setValue(String value) throws QueryErrorException {
        if (checkNumber(value)){
            throw new QueryErrorException("String expected");
        }
        this.value=value.trim();
    }


    public String getAttributeName() {
        return attributeName;
    }

    public boolean checkNumber(String value){
        return pattern.matcher(value).matches();
    }
}
