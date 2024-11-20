package by.bsu.dependency.context;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;


public class HardCodedSingletonApplicationContext extends AbstractApplicationContext {

    public HardCodedSingletonApplicationContext(Class<?>... beanClasses) {
        Arrays.stream(beanClasses).forEach(this::registerBeanDefinition);
    }

    private void registerBeanDefinition(Class<?> beanClass) {
        Bean annotation = beanClass.getAnnotation(Bean.class);
        String beanName = annotation.name();
        beanDefinitions.put(beanName, new BeanDefinition(beanClass, beanName, BeanScope.SINGLETON));
    }

    @Override
    public void start() {
        if (status == ContextStatus.NOT_STARTED) {
            status = ContextStatus.STARTED;
            createSingletonBeans();
        }
    }

    @Override
    public boolean isRunning() {
        return status == ContextStatus.STARTED;
    }
}