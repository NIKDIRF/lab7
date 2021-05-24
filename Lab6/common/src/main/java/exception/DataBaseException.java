package exception;

public class DataBaseException extends RuntimeException{
    private final String dbErrorMessage;
    private final String errorCode;

    public DataBaseException(String errorCode, String dbErrorMessage) {
        super("[ERR-DB-" + errorCode + "]An error occurred while communicating with database. Contact the server administrator.");
        this.errorCode = errorCode;
        this.dbErrorMessage = dbErrorMessage;
    }

    public DataBaseException(int errorCode, String dbErrorMessage) {
        super("[ERR-DB-" + errorCode + "]An error occurred while communicating with database. Contact the server administrator.");
        this.errorCode = Integer.toString(errorCode);
        this.dbErrorMessage = dbErrorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDbErrorMessage() {
        return dbErrorMessage;
    }
}
