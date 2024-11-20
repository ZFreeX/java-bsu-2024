// by/bsu/dependency/context/testbeans/PrototypeBean.java
package by.bsu.dependency.context.testbeans;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;

@Bean(scope = BeanScope.PROTOTYPE)
public class PrototypeBean {
    private String value = "prototype";

    public String getValue() {
        return value;
    }
}