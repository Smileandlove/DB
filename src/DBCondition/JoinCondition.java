package DBCondition;

import DBException.QueryErrorException;
import Engine.Row;

import java.util.ArrayList;
import java.util.Locale;

public class JoinCondition implements Condition{
    Condition condition1;
    Condition condition2;
    String relationship;
    private String attributeName;
    private String value;

    public JoinCondition(Condition condition1,Condition condition2,String relationship){
        this.condition1=condition1;
        this.condition2=condition2;
        this.relationship=relationship;
    }

    @Override
    public boolean compareCondition(Row row) throws QueryErrorException {
        switch (relationship.toUpperCase(Locale.ROOT)){
            case "AND":
                return condition1.compareCondition(row) && condition2.compareCondition(row);
            case "OR":
                return condition1.compareCondition(row) || condition2.compareCondition(row);
            default:
                throw new QueryErrorException("Wrong commands");
        }
    }


    public void setAttributeName(String attributeName) {
        this.attributeName=attributeName;
    }


    public void setValue(String value) throws QueryErrorException {
        this.value=value;
    }


    public String getAttributeName() {
        return attributeName;
    }
}
