package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;

@Bean(name = "simpleBean")
public class SimpleBean {
    public SimpleBean() {
        // Constructor
    }

    public void display() {
        System.out.println("SimpleBean is active.");
    }
}