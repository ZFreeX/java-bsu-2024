package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.CyclicDependencyException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    private enum VertexState {
        NOT_USED,
        USED,
        IN_PROCESS
    }

    private static class Vertex {
        public VertexState state = VertexState.NOT_USED;
        public List<String> descedants = new ArrayList<>();
    }

    protected final Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    protected final Map<String, Object> singletonBeans = new HashMap<>();
    protected ContextStatus status = ContextStatus.NOT_STARTED;
    private final Map<String, Vertex> graph = new HashMap<>(); //why not added previously???



    protected void preprocess(List<Class<?>> beans) {
        beans.forEach(clazz -> beanDefinitions.put(BeanDefinition.getName(clazz), new BeanDefinition(clazz)));
        beanDefinitions.forEach((name, beanDefinition) -> {
            graph.put(name, new Vertex());
            beanDefinition.deps.forEach(dependency -> {
                graph.get(name).descedants.add(BeanDefinition.getName(dependency.getType()));
            });
        });
    }

    private void checkGraph() {
        if (!graph.isEmpty()) {
            dfs(graph.keySet().iterator().next());
        }
    }

    private void dfs(String name) {
        Vertex node = graph.get(name);
        if (node.state == VertexState.IN_PROCESS) {
            throw new CyclicDependencyException(name);
        }
        if (node.state == VertexState.USED) {
            return;
        }
        node.state = VertexState.IN_PROCESS;
        for (String u : node.descedants) {
            dfs(u);
        }
        node.state = VertexState.USED;

    }


    AbstractApplicationContext(Class<?>... beans) {
        this(Arrays.asList(beans));
    }

    AbstractApplicationContext(List<Class<?>> beanClasses) {
        preprocess(beanClasses);
    }


    protected void checkContextStarted() {
        if (status == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException("Context is not started");
        }
    }

    @Override
    public void start() {
        checkGraph();
        status = ContextStatus.STARTED;
        beanDefinitions.forEach((name, beanInfo) -> {
            if (beanInfo.scope == BeanScope.SINGLETON) {
                singletonBeans.put(name, instantiateBean(beanInfo));
            }
        });
        singletonBeans.forEach((name, instance) -> injectDependencies(beanDefinitions.get(name), instance));
        singletonBeans.forEach((name, instance) -> runPost(beanDefinitions.get(name), instance));
    }

    protected Object instantiateBean(BeanDefinition beanDefinition) {
        try {
            return beanDefinition.beanClass.getConstructor().newInstance();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    public static void printHashMap(Map<String, BeanDefinition> map) {
        for (Map.Entry<String, BeanDefinition> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value name: " + entry.getValue().toString());
        }
    }


    private void injectDependencies(BeanDefinition beanDef, Object bean) {
        try {
            for (Field field : beanDef.deps) {
                field.setAccessible(true);
                field.set(bean, getBeanInstance(BeanDefinition.getName(field.getType())));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private void runPost(BeanDefinition beanDef, Object bean) {
        var posts = Arrays.stream(beanDef.beanClass.getDeclaredMethods())
                .filter(field -> field.isAnnotationPresent(PostConstruct.class))
                .collect(Collectors.toList());
        if (posts.size() > 1)
            throw new RuntimeException("Only one postConstruct field can be handled.");
        if (posts.isEmpty()) {
            return;
        }
        Method postConstruct = posts.get(0);
        try {
            postConstruct.setAccessible(true);
            postConstruct.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
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

    private Object getBeanInstance(String name) {
        if (isSingleton(name)) {
            return singletonBeans.get(name);
        }
        BeanDefinition def = beanDefinitions.get(name);
        var instance = instantiateBean(def);
        injectDependencies(def, instance);
        runPost(def, instance);
        return instance;
    }

    @Override
    public Object getBean(String name) {
        checkContextStarted();
        BeanDefinition def = beanDefinitions.get(name);
        if (def == null) {
            throw new NoSuchBeanDefinitionException("Bean with name '" + name + "' not found");
        }
        return getBeanInstance(name);
    }



    @Override
    public <T> T getBean(Class<T> clazz) {
        checkContextStarted();
        return clazz.cast(getBean(BeanDefinition.getName(clazz)));
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