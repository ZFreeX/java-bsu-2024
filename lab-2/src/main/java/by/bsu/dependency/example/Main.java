package by.bsu.dependency.example;

import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.SimpleApplicationContext;
import by.bsu.dependency.context.HardCodedSingletonApplicationContext;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SimpleApplicationContext(
                FirstBean.class, OtherBean.class, RandomBean.class
        );
        applicationContext.start();
        FirstBean firstBean = (FirstBean) applicationContext.getBean("firstBean");
        OtherBean otherBean = (OtherBean) applicationContext.getBean(OtherBean.class);
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < 4; i++) {
            RandomBean prot = (RandomBean) applicationContext.getBean("rand");
            set.add(RandomBean.c);
        }
        firstBean.doS();
        otherBean.doS();
        System.out.println("PostConstruct run in rand: : " + set.size());
        otherBean.doFirst();

    }
}
