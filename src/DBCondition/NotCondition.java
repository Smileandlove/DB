package DBCondition;

import DBException.QueryErrorException;
import Engine.Row;

public class NotCondition implements Condition{
    private String attributeName;
    private String value;

    @Override
    public boolean compareCondition(Row row) throws QueryErrorException {
        return !row.selectValue(attributeName).equals(value);
    }


    public void setAttributeName(String attributeName) {
        this.attributeName=attributeName;
    }


    public void setValue(String Value) throws QueryErrorException {
        this.value=Value;
    }


    public String getAttributeName() {
        return attributeName;
    }
}
