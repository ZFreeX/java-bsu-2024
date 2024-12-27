package by.thekeenest.quizer.exceptions;

public class NullTaskException extends RuntimeException{
    public NullTaskException() {
        super("Task is null. ");
    }
}
