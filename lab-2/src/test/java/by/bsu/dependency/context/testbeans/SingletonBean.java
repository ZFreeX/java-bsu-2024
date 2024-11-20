package by.bsu.dependency.context.testbeans;

import by.bsu.dependency.annotation.Bean;

@Bean
public class SingletonBean {
    private String value = "singleton";

    public String getValue() {
        return value;
    }
}