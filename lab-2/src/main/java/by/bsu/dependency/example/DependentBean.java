package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;

@Bean(name = "dependentBean")
class DependentBean {
    @Inject
    public SimpleBean simpleBean;  // Change access to public

    public void execute() {
        System.out.println("DependentBean is executing.");
        simpleBean.display();
    }
}