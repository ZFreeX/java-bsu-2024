package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

        for (Class<?> beanClass : beanClasses) {
            registerBeanDefinition(beanClass);
        }
    }

    private void registerBeanDefinition(Class<?> beanClass) {
        Bean annotation = beanClass.getAnnotation(Bean.class);
        String beanName = annotation.name().isEmpty() ?
                Character.toLowerCase(beanClass.getSimpleName().charAt(0)) + beanClass.getSimpleName().substring(1) :
                annotation.name();
        BeanScope scope = annotation.scope().equals(BeanScope.SINGLETON) ?
                BeanScope.SINGLETON : BeanScope.PROTOTYPE;

        beanDefinitions.put(beanName, new BeanDefinition(beanClass, beanName, scope));
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