// by/bsu/dependency/context/testbeans/PrototypeBeanWithDependencies.java
package by.bsu.dependency.context.testbeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(scope = BeanScope.PROTOTYPE)
public class PrototypeBeanWithDependencies {
    @Inject
    private SingletonBean singletonBean;

    public SingletonBean getSingletonBean() {
        return singletonBean;
    }
}