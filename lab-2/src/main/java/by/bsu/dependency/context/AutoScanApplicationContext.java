package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutoScanApplicationContext extends AbstractApplicationContext {
    private final String packageName;

    public AutoScanApplicationContext(String packageName) {
        this.packageName = packageName;
        scanPackage();
    }

    private void scanPackage() {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> beanClasses = reflections.getTypesAnnotatedWith(Bean.class);
        preprocess(new ArrayList<>(beanClasses));
    }


}