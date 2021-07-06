package nk.demo.BlogAPIServer.CustomException;

public class AccessDeniedException extends NullPointerException{
    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException() {
        super();
    }
}
