package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "firstBean")
public class FirstBean {

    void printSomething() {
        System.out.println("first baby");
    }

    void doS() {
        System.out.println("First bean is coming.");
    }

    @PostConstruct
    void postConstruct() {
        System.out.println("First bean post construct");
    }

}