package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class SimpleApplicationContext extends AbstractApplicationContext {

    public SimpleApplicationContext(Class<?>... beanClasses) {
        for (Class<?> bean : beanClasses) {
            registerBeanDefinition(bean);
        }
    }

    private void registerBeanDefinition(Class<?> bean) {
        Bean annotation = bean.getAnnotation(Bean.class);
        String beanName = annotation != null && !annotation.name().isEmpty() ? annotation.name() :
                Character.toLowerCase(bean.getSimpleName().charAt(0)) + bean.getSimpleName().substring(1);
        BeanScope scope = annotation != null && annotation.scope() == BeanScope.PROTOTYPE ?
                BeanScope.PROTOTYPE : BeanScope.SINGLETON;
        beanDefinitions.put(beanName, new BeanDefinition(bean, beanName, scope));
    }

    @Override
    public void start() {
        if (status == ContextStatus.NOT_STARTED) {
            status = ContextStatus.STARTED;
            createSingletonBeans();
            injectDependencies();
        }
    }
}