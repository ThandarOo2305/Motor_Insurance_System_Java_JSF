// package org.ace.accounting.user;
//
// import org.ace.java.component.service.PasswordCodecHandler;
// import org.ace.java.component.service.interfaces.IDataRepService;
// import org.springframework.beans.factory.BeanFactory;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
//
// public class UserGenerator {
//
// private static PasswordCodecHandler codecHandler;
// private static IDataRepService<User> dataRepService;
//
// @SuppressWarnings("unchecked")
// public static void main(String[] args) {
// ApplicationContext context = new
// ClassPathXmlApplicationContext("spring-beans.xml");
// BeanFactory factory = context;
// dataRepService = (IDataRepService<User>) factory.getBean("DataRepService");
// codecHandler = (PasswordCodecHandler)
// factory.getBean("PasswordCodecHandler");
//
// User user = new User();
// user.setUserCode("admin");
// user.setPassword("ace");
//
// codecHandler = new PasswordCodecHandler();
// user.setPassword(codecHandler.encode(user.getPassword()));
// dataRepService.insert(user);
// System.out.println("DONE");
// }
//
// }
