package by.bsu.dependency.example;


import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.SimpleApplicationContext;


public class PostConstructExample {

    @Bean(name = "postConstructService")
    public static class PostConstructService {
        public PostConstructService() {

        }
        private String status;

        @PostConstruct
        public void init() {
            this.status = "Initialized";
            System.out.println("PostConstructService initialized.");
        }

        public String getStatus() {
            return status;
        }
    }

    public static void run() {
        ApplicationContext context = new SimpleApplicationContext(PostConstructService.class);
        context.start();

        PostConstructService service = (PostConstructService) context.getBean("postConstructService");
        System.out.println("Service status: " + service.getStatus());
    }
}