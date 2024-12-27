package by.thekeenest.quizer.generators;

import by.thekeenest.quizer.exceptions.BlankTextException;
import by.thekeenest.quizer.tasks.Task;
import by.thekeenest.quizer.Result;
import by.thekeenest.quizer.tasks.TextTask;

public class TextTaskGenerator implements TaskGenerator<Task> {
    private final String question;
    private final String correctAnswer;

    public TextTaskGenerator(String question, String correctAnswer) {
        if (question == null || question.isBlank()) {
            throw new BlankTextException();
        }
        if (correctAnswer == null || correctAnswer.isBlank()) {
            throw new BlankTextException();
        }
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public Task generate() {
        return new TextTask(question, correctAnswer);
    }
}