    package by.bsu.dependency.example;



    import by.bsu.dependency.annotation.Bean;
    import by.bsu.dependency.annotation.Inject;
    import by.bsu.dependency.context.ApplicationContext;
    import by.bsu.dependency.context.HardCodedSingletonApplicationContext;

    public class HardCodedSingletonExample {
        public static void run() {
            //ApplicationContext context = new HardCodedSingletonApplicationContext(SimpleBean.class, DependentBean.class);
//            context.start();
//
//            DependentBean dependentBean = (DependentBean) context.getBean("dependentBean");
//            dependentBean.execute();
        }
    }