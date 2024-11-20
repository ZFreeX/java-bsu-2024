package by.bsu.dependency.context;

import by.bsu.dependency.annotation.BeanScope;

class BeanDefinition {
    private final Class<?> beanClass;
    private final String name;
    private final BeanScope scope;

    public BeanDefinition(Class<?> beanClass, String name, BeanScope scope) {
        this.beanClass = beanClass;
        this.name = name;
        this.scope = scope;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public String getName() {
        return name;
    }

    public BeanScope getScope() {
        return scope;
    }

    public boolean isSingleton() {
        return scope == BeanScope.SINGLETON;
    }
}