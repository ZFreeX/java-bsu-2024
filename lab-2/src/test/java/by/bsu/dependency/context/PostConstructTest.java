package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostConstructTest {

    private TestApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new TestApplicationContext();
        context.start();
    }

    @Test
    public void testSingletonPostConstruct() {
        SingletonBean bean = (SingletonBean) context.getBean("singletonBean");
        assertEquals("Initialized with dependency: Dependency Info", bean.getState());
    }

    @Test
    public void testPrototypePostConstruct() {
        PrototypeBean bean = (PrototypeBean) context.getBean("prototypeBean");
        assertEquals("Initialized with dependency: Dependency Info", bean.getState());

        // Ensure a new instance is created for prototype
        PrototypeBean anotherBean = (PrototypeBean) context.getBean("prototypeBean");
        assertNotSame(bean, anotherBean);
    }

    @Test
    public void testNoSuchBeanPostConstruct() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> {
            context.getBean("nonExistentBean");
        });
    }

    static class TestApplicationContext extends AbstractApplicationContext {

        public TestApplicationContext() {
            // Registering beans
            beanDefinitions.put("dependencyBean", new BeanDefinition(DependencyBean.class));
            beanDefinitions.put("singletonBean", new BeanDefinition(SingletonBean.class));
            beanDefinitions.put("prototypeBean", new BeanDefinition(PrototypeBean.class));
        }
    }

    public static class SingletonBean {
        public SingletonBean() {

        }
        @Inject
        private DependencyBean dependency;

        private String state;

        @PostConstruct
        public void init() {
            state = "Initialized with dependency: " + dependency.getInfo();
        }

        public String getState() {
            return state;
        }
    }

    @Bean(scope = BeanScope.PROTOTYPE)
    public static class PrototypeBean {
        public PrototypeBean() {}
        @Inject
        private DependencyBean dependency;

        private String state;

        @PostConstruct
        public void init() {
            state = "Initialized with dependency: " + dependency.getInfo();
        }

        public String getState() {
            return state;
        }
    }

    public static class DependencyBean {
        public DependencyBean() {

        }
        public String getInfo() {
            return "Dependency Info";
        }
    }
}