package by.bsu.dependency.example;


import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.SimpleApplicationContext;
import by.bsu.dependency.context.AutoScanApplicationContext;

public class MainExample {
    public static void main(String[] args) {
        System.out.println("=== Simple Application Context ===");
        SimpleContextExample.run();

        System.out.println("\n=== Auto Scan Application Context ===");
        AutoScanContextExample.run();

        System.out.println("\n=== Hard Coded Singleton Application Context ===");
        HardCodedSingletonExample.run();

        System.out.println("\n=== PostConstruct Example ===");
        PostConstructExample.run();
    }
}