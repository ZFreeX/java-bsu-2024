package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleApplicationContextTest {

    private ApplicationContext applicationContext;

    // Тестовые классы

    static class TestSingletonBean {
        @Inject
        private TestPrototypeBean prototypeBean;

        public TestPrototypeBean getPrototypeBean() {
            return prototypeBean;
        }
    }

    @Bean(scope = BeanScope.PROTOTYPE)
    static class TestPrototypeBean {}

    static class TestBeanWithoutAnnotation {}

    @Bean(name = "customNameBean")
    static class TestBeanWithCustomName {
        public TestBeanWithCustomName() {}
    }

    @Bean(name = "bothBean", scope=BeanScope.SINGLETON)
    static class BothBean {
        @Inject
        private TestPrototypeBean prototypeBean;

        public TestPrototypeBean getPrototypeBean() {
            return prototypeBean;
        }
    }

    @BeforeEach
    void init() {
        applicationContext = new SimpleApplicationContext(
                TestSingletonBean.class,
                TestPrototypeBean.class,
                TestBeanWithoutAnnotation.class,
                TestBeanWithCustomName.class, BothBean.class
        );
    }

    @Test
    void testIsRunning() {
        assertThat(applicationContext.isRunning()).isFalse();
        applicationContext.start();
        assertThat(applicationContext.isRunning()).isTrue();
    }

    @Test
    void testContextContainsNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.containsBean("testSingletonBean")
        );
    }

    @Test
    void testContextContainsBeans() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("testSingletonBean")).isTrue();
        assertThat(applicationContext.containsBean("testPrototypeBean")).isTrue();
        assertThat(applicationContext.containsBean("testBeanWithoutAnnotation")).isTrue();
        assertThat(applicationContext.containsBean("customNameBean")).isTrue();
        assertThat(applicationContext.containsBean("randomName")).isFalse();
    }

    @Test
    void testDefaultBeanNaming() {
        applicationContext.start();
        assertThat(applicationContext.containsBean("testBeanWithoutAnnotation")).isTrue();
    }

    @Test
    void testCustomBeanNaming() {
        applicationContext.start();
        assertThat(applicationContext.containsBean("customNameBean")).isTrue();
    }

    @Test
    void testGetBeanNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.getBean("testSingletonBean")
        );
    }

    @Test
    void testGetBeanByName() {
        applicationContext.start();

        Object singleton1 = applicationContext.getBean("testSingletonBean");
        Object singleton2 = applicationContext.getBean("testSingletonBean");
        assertThat(singleton1).isNotNull()
                .isInstanceOf(TestSingletonBean.class)
                .isSameAs(singleton2);

        Object prototype1 = applicationContext.getBean("testPrototypeBean");
        Object prototype2 = applicationContext.getBean("testPrototypeBean");
        assertThat(prototype1).isNotNull()
                .isInstanceOf(TestPrototypeBean.class)
                .isNotSameAs(prototype2);
    }

    @Test
    void testGetBeanByType() {
        applicationContext.start();

        TestSingletonBean singleton1 = applicationContext.getBean(TestSingletonBean.class);
        TestSingletonBean singleton2 = applicationContext.getBean(TestSingletonBean.class);
        assertThat(singleton1).isNotNull().isSameAs(singleton2);

        TestPrototypeBean prototype1 = applicationContext.getBean(TestPrototypeBean.class);
        TestPrototypeBean prototype2 = applicationContext.getBean(TestPrototypeBean.class);
        assertThat(prototype1).isNotNull().isNotSameAs(prototype2);
    }

    @Test
    void testDependencyInjection() {
        applicationContext.start();

        TestSingletonBean singleton = applicationContext.getBean(TestSingletonBean.class);
        TestPrototypeBean prototype1 = singleton.getPrototypeBean();
        TestPrototypeBean prototype2 = singleton.getPrototypeBean();

        assertThat(prototype1).isNotNull();
        assertThat(prototype1).isSameAs(prototype2); // Инъекция происходит только один раз
    }

    @Test
    void testGetBeanThrows() {
        applicationContext.start();

        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean("randomName")
        );
    }

    @Test
    void testIsSingletonReturns() {
        applicationContext.start();
        assertThat(applicationContext.isSingleton("testSingletonBean")).isTrue();
        assertThat(applicationContext.isSingleton("testPrototypeBean")).isFalse();
    }

    @Test
    void testIsSingletonThrows() {
        applicationContext.start();
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.isSingleton("randomName")
        );
    }

    @Test
    void testIsPrototypeReturns() {
        applicationContext.start();
        assertThat(applicationContext.isPrototype("testSingletonBean")).isFalse();
        assertThat(applicationContext.isPrototype("testPrototypeBean")).isTrue();
    }

    @Test
    void testIsPrototypeThrows() {
        applicationContext.start();
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.isPrototype("randomName")
        );
    }

    @Test
    void testMultipleContextStarts() {
        applicationContext.start();
        TestSingletonBean firstInstance = applicationContext.getBean(TestSingletonBean.class);

        applicationContext.start(); // повторный старт не должен создавать новые инстансы
        TestSingletonBean secondInstance = applicationContext.getBean(TestSingletonBean.class);

        assertThat(firstInstance).isSameAs(secondInstance);
    }

    @Test
    void testBothFieldsBean() {
        applicationContext.start();
        assertThat(applicationContext.isSingleton("bothBean")).isTrue();
        assertThat(applicationContext.isPrototype("testPrototypeBean")).isTrue();

    }

}