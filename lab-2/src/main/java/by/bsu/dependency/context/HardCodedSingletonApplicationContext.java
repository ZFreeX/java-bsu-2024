package by.bsu.dependency.context;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;

import java.util.Map;
import java.util.HashMap;

public class HardCodedSingletonApplicationContext extends AbstractApplicationContext {
    private final Map<String, Class<?>> beanDefinitions;
    private final Map<String, Object> beans = new HashMap<>();

    public HardCodedSingletonApplicationContext(Class<?>... beanClasses) {
        this.beanDefinitions = Arrays.stream(beanClasses).collect(
                Collectors.toMap(this::checkName, Function.identity()));
    }

    public String checkName(Class<?> beanClass) {
        if (beanClass.getAnnotation(Bean.class).scope() != BeanScope.SINGLETON) {
            throw new RuntimeException("Prototypes are banned");
        }
        return beanClass.getAnnotation(Bean.class).name();
    }

    @Override
    public void start() {
        beanDefinitions.forEach((beanName, beanClass) -> beans.put(beanName, instantiateBean(beanClass)));
        status = ContextStatus.STARTED;
    }

    @Override
    public boolean containsBean(String name) {
        checkContextStarted();
        return beans.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        checkContextStarted();
        if (!containsBean(name)) {
            throw new NoSuchBeanDefinitionException("No bean with name: " + name);
        }
        return beans.get(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(getBean(clazz.getAnnotation(Bean.class).name()));
    }

    @Override
    public boolean isPrototype(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return true;
    }

    private <T> T instantiateBean(Class<T> beanClass) {
        try {
            return beanClass.getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}