package exceptions;

public class IllegalFileSystemOperationException extends RuntimeException {
    public IllegalFileSystemOperationException(String message) {
        super(message);
    }
}
