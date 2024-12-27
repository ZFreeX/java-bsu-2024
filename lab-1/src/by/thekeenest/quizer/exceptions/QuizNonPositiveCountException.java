package by.thekeenest.quizer.exceptions;

public class QuizNonPositiveCountException extends RuntimeException {
    public QuizNonPositiveCountException() {
        super("Counter should be positive. ");
    }
}
