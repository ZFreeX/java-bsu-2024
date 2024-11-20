package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    protected final Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    protected final Map<String, Object> singletonBeans = new HashMap<>();
    protected ContextStatus status = ContextStatus.NOT_STARTED;

    protected void checkContextStarted() {
        if (status == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException("Context is not started");
        }
    }

    protected Object instantiateBean(Class<?> beanClass) {
        try {
            return beanClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate bean: " + beanClass.getName(), e);
        }
    }

    protected void createSingletonBeans() {
        beanDefinitions.values().stream()
                .filter(BeanDefinition::isSingleton)
                .forEach(def -> {
                    Object bean = instantiateBean(def.getBeanClass());
                    singletonBeans.put(def.getName(), bean);
                    injectDependenciesIntoBean(bean);
                    invokePostConstruct(bean);
                });
    }

    protected void injectDependencies() {
        singletonBeans.values().forEach(this::injectDependenciesIntoBean);
    }

    protected void invokePostAll() {
        singletonBeans.values().forEach(this::invokePostConstruct);
    }

    public static void printHashMap(Map<String, BeanDefinition> map) {
        for (Map.Entry<String, BeanDefinition> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value name: " + entry.getValue().getName());
        }
    }


    protected void injectDependenciesIntoBean(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try {
                    Class<?> dependencyType = field.getType();
                    String dependencyName = dependencyType.getSimpleName().toLowerCase().charAt(0) + dependencyType.getSimpleName().substring(1);
                    Object dep = getBean(dependencyName);
                    if (!beanDefinitions.containsKey(dependencyName)) {
                        beanDefinitions.put(dependencyName, new BeanDefinition(dependencyType, dependencyName, BeanScope.SINGLETON));
                        injectDependenciesIntoBean(dep);
                        invokePostConstruct(dep);
                    }
                    field.set(bean, dep);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject dependency into field: " + field.getName(), e);
                }
            }
        }
    }

    protected void invokePostConstruct(Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                System.out.println("Goind to invoke init from " + bean.getClass());
                try {
                    method.invoke(bean);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return status == ContextStatus.STARTED;
    }

    @Override
    public boolean containsBean(String name) {
        checkContextStarted();
        return beanDefinitions.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        checkContextStarted();
        BeanDefinition def = beanDefinitions.get(name);
        if (def == null) {
            throw new NoSuchBeanDefinitionException("Bean with name '" + name + "' not found");
        }
        Object bean = def.getScope() == BeanScope.SINGLETON ? singletonBeans.get(name) : instantiateBean(def.getBeanClass());
        if (def.getScope() == BeanScope.PROTOTYPE) {
            injectDependenciesIntoBean(bean);
            invokePostConstruct(bean);
        }
        return bean;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        checkContextStarted();
        return beanDefinitions.values().stream()
                .filter(def -> clazz.isAssignableFrom(def.getBeanClass()))
                .findFirst()
                .map(def -> (T) getBean(def.getName()))
                .orElseThrow(() -> new NoSuchBeanDefinitionException("No bean found of type " + clazz.getName()));
    }

    @Override
    public boolean isPrototype(String name) {
        checkContextStarted();
        BeanDefinition def = beanDefinitions.get(name);
        if (def == null) {
            throw new NoSuchBeanDefinitionException("No such bean with name '" + name + "'. ");
        }
        return def.getScope() == BeanScope.PROTOTYPE;
    }

    @Override
    public boolean isSingleton(String name) {
        checkContextStarted();
        BeanDefinition def = beanDefinitions.get(name);
        if (def == null) {
            throw new NoSuchBeanDefinitionException("No such bean with name '" + name + "'. ");
        }
        return def.getScope() == BeanScope.SINGLETON;
    }
}