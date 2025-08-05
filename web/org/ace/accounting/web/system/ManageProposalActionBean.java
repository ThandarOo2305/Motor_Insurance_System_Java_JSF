package org.ace.accounting.web.system;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.context.PartialViewContext;
import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.Utils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.excelUpload.ExcelExporter;
import org.ace.accounting.excelUpload.ExcelParser;
import org.ace.accounting.excelUpload.IProposalService;
import org.ace.accounting.excelUpload.Proposal;
import org.ace.accounting.excelUpload.ProposalDTO;
import org.ace.accounting.excelUpload.ProposalReportExcel;
import org.ace.accounting.excelUpload.ProposalSearchDTO;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "ManageProposalActionBean")
@ViewScoped
public class ManageProposalActionBean extends BaseBean {

	@ManagedProperty(value = "#{ProposalService}")
	private IProposalService proposalService;

	public IProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	private boolean createNew;
	private boolean update;
	private Proposal proposal;
	private List<Proposal> proposalList;
	private ProposalDTO proposalDTO;
	private List<ProposalDTO> proposalDTOList = new ArrayList<ProposalDTO>();
	private List<ProposalDTO> excelProposalDTOList = new ArrayList<ProposalDTO>();
	private UploadedFile uploadedFile;
	private String searchText;
	private List<Proposal> groupNameList = new ArrayList<Proposal>();
	private boolean showExcel = false;
	private ProposalSearchDTO proposalSearchDTO = new ProposalSearchDTO();
	private Date maxDate;
	private List<Proposal> proposalSearchList = new ArrayList<Proposal>();
	private final String reportName = "proposal";
	private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
	private final String dirPath = getWebRootPath() + pdfDirPath;
	private String fileName = reportName;

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		createNewProposal();

		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		proposalDTOList = (List<ProposalDTO>) flash.get("proposalDTOList");
		searchText = (String) flash.get("searchText");
		System.out.println("=============" + searchText);

//		proposalDTO = new ProposalDTO();
//		proposalDTO.setId((String) flash.get("id"));
//		if(proposalDTO.getId() != null) {
//			createNew = false;
//			update = true;
//			proposalDTO.setGroupName((String) flash.get("groudName"));
//			proposalDTO.setBank((String) flash.get("bank"));
//			proposalDTO.setInsuredName((String) flash.get("insuredName"));
//			proposalDTO.setPolicyNo((String) flash.get("policyNo"));
//			proposalDTO.setSumInsured((double) flash.get("sumInsured"));
//			proposalDTO.setRemark((String) flash.get("remark"));
//			proposalDTO.setRate((double) flash.get("rate"));
//			proposalDTO.setPremium((double) flash.get("premium"));
//			proposalDTO.setAddress((String) flash.get("address"));
//			proposalDTO.setStartDate((Date) flash.get("startDate"));
//			proposalDTO.setEndDate((Date) flash.get("endDate"));
//			proposalDTO.setVersion((int) flash.get("version"));
//			System.out.println("version" + proposalDTO.getVersion());
//			proposalDTO.setBasicEntity((BasicEntity) flash.get("basicEntity"));
//			System.out.println("basicEntity "+ proposalDTO.getBasicEntity().getCreatedUserId());
//			System.out.println("basicEntity "+ proposalDTO.getBasicEntity().getCreatedDate());
//			System.out.println("basicEntity "+ proposalDTO.getBasicEntity().getUpdatedUserId());
//			System.out.println("basicEntity "+ proposalDTO.getBasicEntity().getUpdatedDate());
//			
//		}
		maxDate = new Date();
		rebindData();

	}

	private void rebindData() {
		proposalList = proposalService.findAll();
	}

	public void addNewProposal() {
		System.out.println("saving................");
		proposalList = new ArrayList<Proposal>();
		if (proposalDTOList != null) {
			for (ProposalDTO proposalDTO : proposalDTOList) {
				System.out.println(proposalDTO.getInsuredName());
				proposal = proposalService.changeDTOToProposal(proposalDTO);
				if (searchText != null) {
					proposal.setStartDate(newStartDate(proposal));
				}
				proposalList.add(proposal);

			}
		} else {
			for (ProposalDTO proposalDTO : excelProposalDTOList) {
				showExcel = false;
				System.out.println(proposalDTO.getInsuredName());
				proposal = proposalService.changeDTOToProposal(proposalDTO);
				proposalList.add(proposal);
			}
		}
		proposalService.addNewProposal(proposalList);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "File uploaded and processed successfully."));
		System.out.println("saved................");
		createNewProposal();
		proposalDTOList = null;
		excelProposalDTOList = null;
	}

	public void softInserted() {
		if (proposalDTOList == null) {
			proposalDTOList = new ArrayList<ProposalDTO>();
		}
		proposalDTOList.add(proposalDTO);
		createNewProposal();
	}

	public void softUpdated() { /// check any error
		if (proposalDTOList != null) {
			int index = proposalDTOList.indexOf(proposalDTO);
			System.out.println(index);
			if (index != -1) {
				proposalDTOList.set(index, proposalDTO);
			} else {
				System.out.println("proposalDTO is not found in the list");
			}

		} else {
			System.out.println("=============");
			int index = excelProposalDTOList.indexOf(proposalDTO);
			if (index != -1) {
				excelProposalDTOList.set(index, proposalDTO);
			} else {
				System.out.println("excelProposalDTO is not found in the list");
			}

		}
		createNewProposal();
	}

	public void softPrepareUpdateProposal(ProposalDTO proposalDTO) {
		showExcel = true;
		createNew = false;
		System.out.println("show excel " + showExcel);
		this.proposalDTO = proposalDTO;
	}

	public String prepareUpdateProposal(Proposal proposal) {
		System.out.println("proposal id " + proposal.getId());
		proposalDTO = proposalService.changeProposalToDTO(proposal);
		System.out.println("==================" + proposalDTO.getId());
		// proposalService.updateProposal(proposal);
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("id", proposalDTO.getId());
		System.out.println("++++++++++++++++" + proposalDTO.getId());
		flash.put("groudName", proposalDTO.getGroupName());
		flash.put("bank", proposalDTO.getBank());
		flash.put("insuredName", proposalDTO.getInsuredName());
		flash.put("policyNo", proposalDTO.getPolicyNo());
		flash.put("sumInsured", proposalDTO.getSumInsured());
		flash.put("remark", proposalDTO.getRemark());
		flash.put("rate", proposalDTO.getRate());
		flash.put("premium", proposalDTO.getPremium());
		flash.put("address", proposalDTO.getAddress());
		flash.put("startDate", proposalDTO.getStartDate());
		flash.put("endDate", proposalDTO.getEndDate());
		flash.put("version", proposalDTO.getVersion());
		flash.put("basicEntity", proposalDTO.getBasicEntity());
		return "manageProposal";
	}

	public String updateProposal() {
		proposal = proposalService.changeDTOToProposal(proposalDTO);
		proposalService.updateProposal(proposal);
		System.out.println(proposal.getInsuredName());
		rebindData();
		return "proposalList";
	}

	public void softDeleteProposal(ProposalDTO proposalDTO) {
		if (proposalDTOList != null) {
			proposalDTOList.remove(proposalDTO);
		} else {
			excelProposalDTOList.remove(proposalDTO);
		}
		createNewProposal();
	}

	public String deleteProposal(Proposal proposal) {
		proposalService.deleteProposal(proposal);
		rebindData();
		return "proposalList";
	}

	public void searchByGroupName() {
		groupNameList = proposalService.searchByGroupName(searchText);
		// rebindData();

	}

	public String renewProposal() {
		proposalDTOList = new ArrayList<ProposalDTO>();
		for (Proposal proposal : groupNameList) {
			ProposalDTO proposalDTO = proposalService.changeProposalToDTO(proposal);
			System.out.println(proposalDTO.getInsuredName());
			proposalDTOList.add(proposalDTO);
		}
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("proposalDTOList", proposalDTOList);
		flash.put("searchText", searchText);
		searchText = null;
		return "manageProposal";

	}

	public void createNewProposal() {
		createNew = true;
		proposalDTO = new ProposalDTO();
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public List<Proposal> getProposalList() {
		return proposalList;
	}

	public void setProposalList(List<Proposal> proposalList) {
		this.proposalList = proposalList;
	}

	public ProposalDTO getProposalDTO() {
		return proposalDTO;
	}

	public void setProposalDTO(ProposalDTO proposalDTO) {
		this.proposalDTO = proposalDTO;
	}

	public List<ProposalDTO> getProposalDTOList() {
		return proposalDTOList;
	}

	public void setProposalDTOList(List<ProposalDTO> proposalDTOList) {
		this.proposalDTOList = proposalDTOList;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<Proposal> getGroupNameList() {
		return groupNameList;
	}

	public void setGroupNameList(List<Proposal> groupNameList) {
		this.groupNameList = groupNameList;
	}

	public boolean isShowExcel() {
		return showExcel;
	}

	public void setShowExcel(boolean showExcel) {
		this.showExcel = showExcel;
	}

	public List<ProposalDTO> getExcelProposalDTOList() {
		return excelProposalDTOList;
	}

	public void setExcelProposalDTOList(List<ProposalDTO> excelProposalDTOList) {
		this.excelProposalDTOList = excelProposalDTOList;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public List<Proposal> getProposalSearchList() {
		return proposalSearchList;
	}

	public void setProposalSearchList(List<Proposal> proposalSearchList) {
		this.proposalSearchList = proposalSearchList;
	}

	public ProposalSearchDTO getProposalSearchDTO() {
		return proposalSearchDTO;
	}

	public void setProposalSearchDTO(ProposalSearchDTO proposalSearchDTO) {
		this.proposalSearchDTO = proposalSearchDTO;
	}

	public void upload(FileUploadEvent event) {
		this.excelProposalDTOList = new ArrayList<ProposalDTO>();
		try {
			InputStream inputStream = event.getFile().getInputstream();
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			Iterator<Row> rowIterator = sheet.iterator();
			// Skip the header row if necessary
			if (rowIterator.hasNext()) {
				rowIterator.next();
			}

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				proposalDTO = new ProposalDTO(); // reset
				proposalDTO.setInsuredName(row.getCell(0).getStringCellValue());
				proposalDTO.setBank(row.getCell(1).getStringCellValue());
				proposalDTO.setAddress(row.getCell(2).getStringCellValue());
				proposalDTO.setPolicyNo(row.getCell(3).getStringCellValue());
				proposalDTO.setSumInsured((int) row.getCell(4).getNumericCellValue());
				proposalDTO.setRate((int) row.getCell(5).getNumericCellValue());
				proposalDTO.setPremium((int) row.getCell(6).getNumericCellValue());
				proposalDTO.setStartDate(row.getCell(7).getDateCellValue());
				proposalDTO.setRemark(row.getCell(8).getStringCellValue());
				System.out.println(proposalDTO.getStartDate());

				// Add additional fields as needed

				excelProposalDTOList.add(proposalDTO);
			}

			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		showExcel = true;
		System.out.println("========" + excelProposalDTOList.get(1));
	}

	public String formatDate(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			return dateFormat.format(date);
		} else {
			return ""; // or any other default value
		}
	}

	private Date newStartDate(Proposal proposal) {
		Date startDate = proposal.getStartDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.YEAR, 1);
		Date endDate = calendar.getTime();
		return endDate;
	}

	public void search() {
		System.out.println("========" + proposalSearchDTO);
		System.out.println("========" + proposalSearchDTO.getStartDate());
		proposal = proposalService.changeSearchDTOToProposal(proposalSearchDTO);
		System.out.println("========" + proposal.getStartDate());
		proposalSearchList = proposalService.search(proposal);
	}

	public void generateReport1() {

		InputStream templateStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("proposalReport4.jrxml");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("proposalSearchList", new JRBeanCollectionDataSource(proposalSearchList));
		JasperDesign jasperDesign;
		try {
			jasperDesign = JRXmlLoader.load(templateStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			String uniqueFileName = fileName + "_" + System.currentTimeMillis();
			fileName = uniqueFileName;
			FileUtils.forceMkdir(new File(dirPath));
			JasperExportManager.exportReportToPdfFile(jasperPrint, dirPath + uniqueFileName.concat(".pdf"));

		} catch (JRException | IOException e) {
			e.printStackTrace();
		}

	}

	public String getStream() {
		String fullFilePath = pdfDirPath + fileName.concat(".pdf");
		return fullFilePath;
	}

//	 public void exportExcel() {
//
//			ExternalContext ec = getFacesContext().getExternalContext();
//			ec.responseReset();
//			ec.setResponseContentType("application/vnd.ms-excel");
//			String fileName = "PROPOSAL_REPORT.xlsx";
//			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//			try (OutputStream op = ec.getResponseOutputStream();) {
//				ProposalReportExcel excel = new ProposalReportExcel();
//				excel.generate(op, proposalSearchList);
//				getFacesContext().responseComplete();				
//			} catch (IOException e) {
//				throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to export PROPOSAL_REPORT.xlsx", e);
//			}
//		}

	@SuppressWarnings("resource")
	public void exportToExcel() {
		System.out.println("why method not found");
		try {
			// Sample data
			List<Proposal> dataList = proposalSearchList;

			// Create Excel workbook
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("proposal");
			
			Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            // Create a cell style with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            
            BorderStyle borderStyle = BorderStyle.THIN;
            short borderColor = IndexedColors.BLACK.getIndex();

            // Apply border to header cell style
            headerCellStyle.setBorderTop(borderStyle);
            headerCellStyle.setBorderBottom(borderStyle);
            headerCellStyle.setBorderLeft(borderStyle);
            headerCellStyle.setBorderRight(borderStyle);
            headerCellStyle.setTopBorderColor(borderColor);
            headerCellStyle.setBottomBorderColor(borderColor);
            headerCellStyle.setLeftBorderColor(borderColor);
            headerCellStyle.setRightBorderColor(borderColor);

			// Create header row
			Row headerRow = sheet.createRow(0);
			String[] headers = { "GroupName", "InsuredName", "Bank", "Address", "PolicyNo", "SumInsured", "Rate",
					"Premium", "StartDate", "EndDate" }; // Adjust based on your data
	
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerCellStyle);
			}
			
			sheet.setColumnWidth(0, 20 * 256);
			sheet.setColumnWidth(1, 20 * 256);
			sheet.setColumnWidth(2, 15 * 256);
			sheet.setColumnWidth(3, 15 * 256);
			sheet.setColumnWidth(4, 15 * 256);
			sheet.setColumnWidth(5, 15 * 256);
			sheet.setColumnWidth(7, 15 * 256);
			sheet.setColumnWidth(8, 15 * 256);
			sheet.setColumnWidth(9, 15 * 256);

			// Populate data rows
			int rowNum = 1;
			for (Proposal p : dataList) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(p.getGroupName());
				row.createCell(1).setCellValue(p.getInsuredName());
				row.createCell(2).setCellValue(p.getBank());
				row.createCell(3).setCellValue(p.getAddress());
				row.createCell(4).setCellValue(p.getPolicyNo());
				row.createCell(5).setCellValue(p.getSumInsured());
				row.createCell(6).setCellValue(p.getRate());
				row.createCell(7).setCellValue(p.getPremium());
				row.createCell(8).setCellValue(formatDate(p.getStartDate()));
				row.createCell(9).setCellValue(formatDate(p.getEndDate()));
				// Add more cells as needed for additional fields
				
				/*
				 * SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy"); Cell
				 * startDateCell = row.createCell(8); String startDateString =
				 * p.getStartDate().toString();
				 * startDateCell.setCellValue(formatDate(p.getStartDate()));
				 * 
				 * Cell endDateCell = row.createCell(9); String endDateString =
				 * p.getEndDate().toString(); // Assuming getEndDate() returns a String
				 * endDateCell.setCellValue(format.format(endDateString));
				 */
			}

			// Write workbook to output stream
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String timestamp = dateFormat.format(new Date());
			String fileName = "proposal" + timestamp + ".xlsx";
		
			String downloadFilePath = "D:\\Downloads"; String filePath = downloadFilePath + "/" + fileName;

			  try (OutputStream fileOut = new FileOutputStream(filePath)) {
			  workbook.write(fileOut); fileOut.flush(); }
			 

			System.out.println("Excel file generated at: " + filePath);

			// Send the file to user for download
			externalContext.responseReset();
			externalContext.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			OutputStream outputStream = externalContext.getResponseOutputStream();
			workbook.write(outputStream);
			facesContext.responseComplete();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully download"));
			System.out.println("complete");
		} catch (IOException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred during export", null));
		}
	}
}
