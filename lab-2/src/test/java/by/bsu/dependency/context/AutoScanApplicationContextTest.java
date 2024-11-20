package by.bsu.dependency.context;

import by.bsu.dependency.context.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.context.exception.NoSuchBeanDefinitionException;
import by.bsu.dependency.context.testbeans.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class AutoScanApplicationContextTest {

    private AutoScanApplicationContext context;
    private static final String TEST_PACKAGE = "by.bsu.dependency.context.testbeans";

    @BeforeEach
    void setUp() {
        context = new AutoScanApplicationContext(TEST_PACKAGE);
    }

    @Nested
    class ContextLifecycleTests {
        @Test
        void shouldNotBeRunningBeforeStart() {
            assertFalse(context.isRunning());
        }

        @Test
        void shouldBeRunningAfterStart() {
            context.start();
            assertTrue(context.isRunning());
        }

        @Test
        void shouldThrowExceptionWhenAccessingBeansBeforeStart() {
            assertThrows(ApplicationContextNotStartedException.class,
                    () -> context.getBean("singletonBean"));
        }
    }

    @Nested
    class BeanManagementTests {
        @BeforeEach
        void startContext() {
            context.start();
        }

        @Test
        void shouldContainScannedBeans() {
            assertTrue(context.containsBean("singletonBean"));
            assertTrue(context.containsBean("prototypeBean"));
        }

        @Test
        void shouldReturnSameSingletonInstance() {
            SingletonBean bean1 = context.getBean(SingletonBean.class);
            SingletonBean bean2 = context.getBean(SingletonBean.class);
            assertSame(bean1, bean2);
        }

        @Test
        void shouldReturnDifferentPrototypeInstances() {
            PrototypeBean bean1 = context.getBean(PrototypeBean.class);
            PrototypeBean bean2 = context.getBean(PrototypeBean.class);
            assertNotSame(bean1, bean2);
        }

        @Test
        void shouldRecognizeBeanScope() {
            assertTrue(context.isSingleton("singletonBean"));
            assertTrue(context.isPrototype("prototypeBean"));
        }

        @Test
        void shouldThrowExceptionForNonExistentBean() {
            assertThrows(NoSuchBeanDefinitionException.class,
                    () -> context.getBean("nonExistentBean"));
        }
    }

    @Nested
    class DependencyInjectionTests {
        @BeforeEach
        void startContext() {
            context.start();
        }

        @Test
        void shouldInjectDependencies() {
            DependentBean bean = context.getBean(DependentBean.class);
            assertNotNull(bean.getSingletonBean());
        }
    }

    @Nested
    class CustomBeanNameTests {
        @BeforeEach
        void startContext() {
            context.start();
        }

        @Test
        void shouldRespectCustomBeanNames() {
            assertTrue(context.containsBean("customNameBean"));
            assertNotNull(context.getBean("customNameBean"));
        }
    }

    @Nested
    class EdgeCaseTests {
        @BeforeEach
        void startContext() {
            context.start();
        }

        @Test
        void shouldHandleEmptyBeans() {
            assertNotNull(context.getBean("emptyBean"));
        }

        @Test
        void shouldInjectDependenciesInPrototypeBeans() {
            PrototypeBeanWithDependencies bean1 = context.getBean(PrototypeBeanWithDependencies.class);
            PrototypeBeanWithDependencies bean2 = context.getBean(PrototypeBeanWithDependencies.class);

            assertNotSame(bean1, bean2);
            assertSame(bean1.getSingletonBean(), bean2.getSingletonBean());
        }
    }
}