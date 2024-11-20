package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.SimpleApplicationContext;

public class SimpleContextExample {

    @Bean(name = "service")
    public class MyService {  // Make it public
        public MyService() {

        }
        public void serve() {
            System.out.println("Service is serving.");
        }
    }

    @Bean(name = "controller")
    public class MyController {  // Make it public
        public MyController() {

        }
        @Inject
        public MyService service;  // Change access to public

        public void process() {
            System.out.println("Controller is processing.");
            service.serve();
        }
    }

    public static void run() {
        ApplicationContext context = new SimpleApplicationContext(MyService.class, MyController.class);
        context.start();

        MyController controller = (MyController) context.getBean("controller");
        controller.process();
    }
}