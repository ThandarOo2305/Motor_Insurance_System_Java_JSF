// package org.ace.accounting.web.report;
//
// import java.io.FileInputStream;
// import java.io.InputStream;
// import java.math.BigDecimal;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// import org.ace.accounting.common.CurrencyType;
// import org.ace.accounting.common.utils.DateUtils;
// import org.ace.accounting.dto.CashBookDTO;
// import org.ace.accounting.report.CashBookCriteria;
// import
// org.ace.accounting.report.listingReport.service.interfaces.IListingReportService;
// import org.apache.log4j.Logger;
// import org.junit.AfterClass;
// import org.junit.BeforeClass;
// import org.junit.Test;
// import org.springframework.beans.factory.BeanFactory;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
//
// import net.sf.jasperreports.engine.JREmptyDataSource;
// import net.sf.jasperreports.engine.JasperCompileManager;
// import net.sf.jasperreports.engine.JasperExportManager;
// import net.sf.jasperreports.engine.JasperFillManager;
// import net.sf.jasperreports.engine.JasperPrint;
// import net.sf.jasperreports.engine.JasperReport;
// import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
// import net.sf.jasperreports.engine.design.JasperDesign;
// import net.sf.jasperreports.engine.xml.JRXmlLoader;
//
// public class JasperTest {
// private static Logger logger = Logger.getLogger(JasperTest.class);
// private static IListingReportService listingReportService;
//
// @BeforeClass
// public static void init() {
// logger.info("CommonInformRejectLetterTest is
// started.........................................");
// ApplicationContext context = new
// ClassPathXmlApplicationContext("spring-beans.xml");
// BeanFactory factory = context;
// listingReportService = (IListingReportService)
// factory.getBean("ListingReportService");
// logger.info("CommonInformRejectLetterTest instance has been loaded.");
//
// }
//
// @AfterClass
// public static void finished() {
// logger.info("CommonInformRejectLetterTest has been
// finished.........................................");
// }
//
// @Test
// public void genetateLifeRejectLetter() throws Exception {
// InputStream inputStream = new
// FileInputStream("D:/temp/cashBookListing.jrxml");
// String outputFilePdf = "D:/temp/cashBookListingReport.pdf";
// CashBookCriteria criteria = new CashBookCriteria();
// criteria.setCurrencyType(CurrencyType.HOMECURRENCY);
// criteria.setFromDate(DateUtils.formatStringToDate("1-10-2015"));
// criteria.setToDate(DateUtils.formatStringToDate("10-10-2015"));
// List<CashBookDTO> cashBookList =
// listingReportService.findCashbookListingReport(criteria);
// JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
// JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
// int count = 1;
// for (CashBookDTO cashBook : cashBookList) {
// Map<String, Object> paramMap = new HashMap<String, Object>();
// paramMap.put("branchName", "All Branches");
// paramMap.put("currencyName", "MMK");
// paramMap.put("reportDate", new Date());
// paramMap.put("date", cashBook.getCashBookDate());
// paramMap.put("fromDate", cashBook.getCashBookDate());
// paramMap.put("toDate", cashBook.getCashBookDate());
// paramMap.put("acName", cashBook.getAcName());
// paramMap.put("openingBalance", cashBook.getOpeningBalance());
// paramMap.put("initialValue", BigDecimal.ZERO);
// paramMap.put("records", cashBook.getDetails().size());
// paramMap.put("detailReports", new
// JRBeanCollectionDataSource(cashBook.getDetails()));
// JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
// paramMap, new JREmptyDataSource());
// JasperExportManager.exportReportToPdfFile(jasperPrint, outputFilePdf);
// break;
// }
// }
//
// public static void main(String[] args) {
// org.junit.runner.JUnitCore.main(JasperTest.class.getName());
// }
//
// }
