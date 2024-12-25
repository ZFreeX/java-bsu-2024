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
        super(beanClasses);
    }
}