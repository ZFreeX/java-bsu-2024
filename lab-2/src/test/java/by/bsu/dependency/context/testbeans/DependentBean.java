// by/bsu/dependency/context/testbeans/DependentBean.java
package by.bsu.dependency.context.testbeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;

@Bean
public class DependentBean {
    @Inject
    private SingletonBean singletonBean;

    public SingletonBean getSingletonBean() {
        return singletonBean;
    }
}