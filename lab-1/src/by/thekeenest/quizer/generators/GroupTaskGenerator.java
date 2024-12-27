package by.thekeenest.quizer.generators;

import by.thekeenest.quizer.tasks.Task;

import java.util.*;

public class GroupTaskGenerator<T extends Task> implements TaskGenerator<T> {

    private final List<TaskGenerator<T>> generators;
    private final Random random = new Random();

    @SafeVarargs
    public GroupTaskGenerator(TaskGenerator<T>... generators) {
        if (generators == null || generators.length == 0) {
            throw new IllegalArgumentException("Generators must not be empty");
        }
        this.generators = Arrays.asList(generators);
    }

    public GroupTaskGenerator(Collection<TaskGenerator<T>> generators) {
        if (generators == null || generators.isEmpty()) {
            throw new IllegalArgumentException("Generators collection cannot be null or empty");
        }
        this.generators = new ArrayList<>(generators);
    }

    @Override
    public T generate() {
        while (!generators.isEmpty()) {
            var index = random.nextInt(generators.size());
            TaskGenerator<T> gen = generators.get(index);
            try {
                return gen.generate();
            } catch (Exception e) {
                generators.remove(index);
            }
        }
        throw new IllegalStateException("All the gens has been thrown exceptions. ");
    }

}
