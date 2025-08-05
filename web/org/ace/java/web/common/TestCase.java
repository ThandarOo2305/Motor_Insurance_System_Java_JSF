package org.ace.java.web.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.dto.ReportStatementDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.report.reportStatement.service.interfaces.IReportStatementService;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class TestCase {
	private static Logger logger = Logger.getLogger(TestCase.class);
	private static IReportStatementService reportStatementService;

	@BeforeClass
	public static void init() {
		logger.info("MotorProposalReportTest is started.........................................");
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
		BeanFactory factory = context;
		reportStatementService = (IReportStatementService) factory.getBean("ReportStatementService");
		logger.info("MotorProposalReportTest instance has been loaded.");
	}

	@Test
	public void test() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(2019, 1, 1);
			Date reportDate = cal.getTime();
			List<ReportStatementDto> dtoList = reportStatementService.previewProcedure(true, ReportType.PL, CurrencyType.HOMECURRENCY, null, null, reportDate, "TEST");
			InputStream inputStream = new FileInputStream("D:/temp/reportStatementReportHorizontal.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String branchString = "All Branches";
			String currencyString = "All Currencies";
			parameters.put("FORMAT_NAME", "test");
			parameters.put("BRANCH", branchString);
			parameters.put("CURRENCY", currencyString);
			parameters.put("NEWDATE", newDate);
			parameters.put("LOGO", "pdf-report/logo_1.jpg");
			parameters.put("REPORT_MONTH", "test");
			parameters.put("HEADING", "test");
			parameters.put("DTOLIST", new JRBeanCollectionDataSource(dtoList));
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			JasperExportManager.exportReportToPdfFile(jasperPrint, "D:/temp/test.pdf");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main(TestCase.class.getName());
	}
}
