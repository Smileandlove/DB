package DBException;

public class QueryErrorException extends Throwable {
    String message;

    public QueryErrorException(String message){
        this.message=message;
    }
    public String toString() {
        return "[ERROR]: " + message;
    }
}
