package by.thekeenest.quizer.exceptions;

public class QuizFinishedException extends RuntimeException {
    public QuizFinishedException() {
        super("Quiz has been already finished. ");
    }
}
