package by.thekeenest.quizer;

import by.thekeenest.quizer.exceptions.NullTaskException;
import by.thekeenest.quizer.exceptions.QuizFinishedException;
import by.thekeenest.quizer.exceptions.QuizNonPositiveCountException;
import by.thekeenest.quizer.exceptions.QuizNotFinishedException;
import by.thekeenest.quizer.generators.TaskGenerator;
import by.thekeenest.quizer.tasks.Task;

/**
 * Class, который описывает один тест
 */
public class Quiz {
    /**
     * @param generator генератор заданий
     * @param taskCount количество заданий в тесте
     */
    private final TaskGenerator generator;
    private Task currentTask = null;
    private final int taskAmount;
    private boolean finished;
    private int incorrect;
    private int ok;
    private int wa;

    public Quiz(TaskGenerator generator, int taskCount) {
        if (taskCount <= 0) {
            throw new QuizNonPositiveCountException();
        }
        this.taskAmount = taskCount;
        this.generator = generator;
        this.currentTask = generator.generate();
    }

    /**
     * @return задание, повторный вызов вернет слелующее
     * @see Task
     */
    public Task nextTask() {
        if (finished) {
            throw new QuizFinishedException();
        }
        if (currentTask == null) {
            currentTask = generator.generate();
        }
        return currentTask;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    /**
     * Предоставить ответ ученика. Если результат {@link Result#INCORRECT_INPUT}, то счетчик неправильных
     * ответов не увеличивается, а {@link #nextTask()} в следующий раз вернет тот же самый объект {@link Task}.
     */

    public Result provideAnswer(String answer) {
        if (currentTask == null) {
            throw new NullTaskException();
        }
        if (finished) {
            throw new QuizFinishedException();
        }
        Result res = currentTask.validate(answer);
        System.out.println(String.format("Validating answer: %s, %s", answer, answer));

        if (res == Result.OK) {
            ok++;
        } else if (res == Result.WRONG) {
            wa++;
        } else {
            incorrect++;
        }
        if (!res.equals(Result.INCORRECT_INPUT)) {
            currentTask = null; //вот это и пофиксит мой issue =)
        }
        if (completedTasks() == taskAmount) {
            finished = true;
        }
        return res;
    }


    int completedTasks() {
        return ok + wa;
    }
    /**
     * @return завершен ли тест
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * @return количество правильных ответов
     */
    int getCorrectAnswerNumber() {
        return ok;
    }

    /**
     * @return количество неправильных ответов
     */
    int getWrongAnswerNumber() {
        return wa;
    }

    /**
     * @return количество раз, когда был предоставлен неправильный ввод
     */
    int getIncorrectInputNumber() {
        return incorrect;
    }

    /**
     * @return оценка, которая является отношением количества правильных ответов к количеству всех вопросов.
     *         Оценка выставляется только в конце!
     */
    double getMark() {
        if (!finished) {
            throw new QuizNotFinishedException();
        }
        return (double) ok / taskAmount;
    }
}