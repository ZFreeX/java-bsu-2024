package by.thekeenest.quizer.exceptions;


public class QuizNotFinishedException extends RuntimeException{
    public QuizNotFinishedException() {
        super("Quiz is not finished");
    }
}
