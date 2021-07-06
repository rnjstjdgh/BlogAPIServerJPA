package nk.demo.BlogAPIServer.CustomException;

public class CustomNullPointException extends NullPointerException{
    public CustomNullPointException(String msg) {
        super(msg);
    }

    public CustomNullPointException() {
        super();
    }
}
