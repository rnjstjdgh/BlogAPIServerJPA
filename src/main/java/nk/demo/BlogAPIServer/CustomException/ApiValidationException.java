package nk.demo.BlogAPIServer.CustomException;

public class ApiValidationException extends RuntimeException{
    public ApiValidationException(String msg, Throwable t) {
        super(msg, t);
    }

    public ApiValidationException(String msg) {
        super(msg);
    }

    public ApiValidationException() {
        super();
    }
}