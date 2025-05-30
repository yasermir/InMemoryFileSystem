package exceptions;

public class NotATextFileException extends RuntimeException {
    public NotATextFileException(String message) {
        super(message);
    }
}
