package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.PostConstruct;

import java.util.Random;

@Bean(name = "rand", scope = BeanScope.PROTOTYPE)
public class RandomBean {
    private Random rand = new Random();
    public static int c = 0;

    @PostConstruct
    public void generate() {
        c = rand.nextInt(1000);
    }
}