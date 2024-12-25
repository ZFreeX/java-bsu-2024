package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class BeanDefinition {
    public final Class<?> beanClass;
    public final BeanScope scope;
    public final List<Field> deps;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.scope = beanClass.isAnnotationPresent(Bean.class) ? beanClass.getAnnotation(Bean.class).scope() : BeanScope.SINGLETON;
        this.deps = Arrays.stream(beanClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public static String getName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Bean.class) && !clazz.getAnnotation(Bean.class).name().isEmpty()) {
            return clazz.getAnnotation(Bean.class).name();
        } else {
            String name = clazz.getSimpleName();
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            return name;
        }
    }

    public BeanScope getScope() {
        return scope;
    }

    public boolean isSingleton() {
        return scope == BeanScope.SINGLETON;
    }
}