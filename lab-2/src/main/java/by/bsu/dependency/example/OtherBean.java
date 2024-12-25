package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "otherBean")
public class OtherBean {

    @Inject
    private FirstBean firstBean;

    void doS() {
        System.out.println("other ones");
    }

    void doFirst() {
        System.out.println("interaction with first");
        firstBean.doS();
    }
}