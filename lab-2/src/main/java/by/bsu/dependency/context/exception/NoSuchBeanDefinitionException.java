package by.bsu.dependency.context.exception;

public class NoSuchBeanDefinitionException extends RuntimeException{
    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }

    public NoSuchBeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
