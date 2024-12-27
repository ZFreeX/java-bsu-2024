package by.thekeenest.quizer.exceptions;

public class BlankTextException extends RuntimeException {
    public BlankTextException() {
        super("Text in TextTask cannot be null or blank. ");
    }
}
