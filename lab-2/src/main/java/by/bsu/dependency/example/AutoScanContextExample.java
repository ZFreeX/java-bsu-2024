package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.AutoScanApplicationContext;


public class AutoScanContextExample {

    @Bean(name = "databaseService")
    public static class DatabaseService {  // Make it public
        public DatabaseService() {}
        public void connect() {
            System.out.println("Database connected.");
        }
    }

    @Bean(name = "userService")
    public static class UserService {  // Make it public
        public UserService() {

        }
        @Inject
        public DatabaseService databaseService;  // Change access to public

        public void performUserAction() {
            System.out.println("User action performed.");
            databaseService.connect();
        }
    }

    public static void run() {
        ApplicationContext context = new AutoScanApplicationContext("by.bsu.dependency.example");
        context.start();

        UserService userService = (UserService) context.getBean("userService");
        userService.performUserAction();
    }
}
