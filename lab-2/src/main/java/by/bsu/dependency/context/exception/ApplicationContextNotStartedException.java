package by.bsu.dependency.context.exception;

public class ApplicationContextNotStartedException extends RuntimeException {

    public ApplicationContextNotStartedException(String message) {
        super(message);
    }

    public ApplicationContextNotStartedException(String message, Throwable cause) {
        super(message, cause);
    }
}