package DBCondition;

import DBException.QueryErrorException;
import Engine.Row;

public interface Condition {
    boolean compareCondition(Row row) throws QueryErrorException;
    void setAttributeName(String attributeName);
    void setValue(String value) throws QueryErrorException;
    String getAttributeName();
}
