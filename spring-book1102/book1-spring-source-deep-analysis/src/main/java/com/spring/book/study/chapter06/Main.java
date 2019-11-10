package com.spring.book.study.chapter06;

import com.spring.book.study.chapter04.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.GregorianCalendar;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {

//        testCustomProperties();

//        testPropertiesEditor();

//        testCustomProcessor();

//        testMessageSource();
        
//        testListener();

        testDateConverter();

    }

    private static void testDateConverter() {
        DefaultConversionService conversionService = new DefaultConversionService();
//        conversionService.addConverter(new StringToPhone);


    }

    private static void testListener() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("listenerTest.xml");
        TestEvent testEvent = new TestEvent("hello", "msg");
        ctx.publishEvent(testEvent);

    }

    private static void testMessageSource() {
        String[] configs = {"messageResourceTest.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(configs);

        Object[] parms = {"John",new GregorianCalendar().getTime()};

        String str1 = ctx.getMessage("test", parms, Locale.US);
        String str2 = ctx.getMessage("test", parms, Locale.CHINA);
        System.out.println(str1);
        System.out.println(str2);
    }

    private static void testCustomProcessor() {
        ApplicationContext app = new MyClassPathXmlApplicationContext("customPostProcessor.xml");
        // 实例化的时候才会调用后处理器
        app.getBean("testBean");
    }

    private static void testPropertiesEditor() {
        // 不进行适当的处理报错：java.lang.IllegalStateException: Cannot convert value of type [java.lang.String] to required type [java.util.Date] for property 'dateValue': no matching editors or conversion strategy found

        ApplicationContext app = new MyClassPathXmlApplicationContext("propertiesEditorTest.xml");
        UserManager userManager = (UserManager) app.getBean("userManager");
        System.out.println(userManager);

    }

    private static void testCustomProperties() {

        // 启动过程中若是定义了系统环境变量，则正常加载
        // 若是没有定义，则抛出异常
        // Exception in thread "main" org.springframework.core.env.MissingRequiredPropertiesException: The following properties were declared as required but could not be resolved: [VAR]
        ApplicationContext app = new MyClassPathXmlApplicationContext("customPropertiesTest.xml");
        User user = (User) app.getBean("testBean");
        System.out.println(user.getEmail());

    }
}
