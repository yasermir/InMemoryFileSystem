package exceptions;

public class PathAlreadyExistsException extends RuntimeException {
    public PathAlreadyExistsException(String message) {
        super(message);
    }
}
