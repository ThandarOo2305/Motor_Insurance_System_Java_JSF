// package org.ace.accounting.system.webPage;
//
// import java.io.File;
// import java.util.ArrayList;
// import java.util.List;
//
// import org.ace.java.component.service.interfaces.IDataRepService;
// import org.springframework.beans.factory.BeanFactory;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
//
// public class WebPageGenerator{
//
// private static IDataRepService<WebPage> dataRepService;
//
// @SuppressWarnings("unchecked")
// public static void main(String[] args) {
// ApplicationContext context = new
// ClassPathXmlApplicationContext("spring-beans.xml");
// BeanFactory factory = context;
// dataRepService = (IDataRepService<WebPage>)
// factory.getBean("DataRepService");
//
// // String path = "D:\\develope\\ibrb\\WebContent\\manage";
// // String path =
// // "D:\\Java Workspace\\Latest New of IBRB\\ibrb\\WebContent\\manage";
// String path = "D:\\Workspace\\IBRB Workspace\\accounting";
// List<String> result = printFnames(path, new ArrayList<String>());
// for (String s : result) {
// String[] temp = s.split("\\\\");
// String[] key = temp[temp.length - 1].split(".xhtml");
//
// WebPage webPage = new WebPage();
// webPage.setName(key[0]);
// webPage.setDescription(key[0]);
// webPage.setUrl(s.replace("\\", "/"));
//
// System.out.println("////////////////////////////");
// System.out.println(webPage.getName());
// System.out.println(webPage.getDescription());
// System.out.println(webPage.getUrl());
// System.out.println("////////////////////////////");
// dataRepService.insert(webPage);
// System.out.println("DONE DONE DONE");
// }
//
// }
//
// public static List<String> printFnames(String sDir, List<String> list) {
// File[] faFiles = new File(sDir).listFiles();
// for (File file : faFiles) {
// if (file.getName().matches("^(.*?)")) {
// if (file.getAbsolutePath().contains("xhtml")) {
// // here "ibrb" is project name
// String[] value = file.getAbsolutePath().split("WebContent");
// list.add(value[1]);
//
// // Just for testing
// String[] temp = value[1].split("\\\\");
// @SuppressWarnings("unused")
// String[] key = temp[temp.length - 1].split(".xhtml");
// }
// }
// if (file.isDirectory()) {
// printFnames(file.getAbsolutePath(), list);
// }
// }
//
// return list;
// }
// }
