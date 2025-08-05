package org.ace.accounting.web.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.ReportFormatDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.formatfile.ColType;
import org.ace.accounting.system.formatfile.FormatFile;
import org.ace.accounting.system.formatfile.service.interfaces.IFormatFileService;
import org.ace.accounting.web.common.FIType;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.ace.java.web.model.SelectableIDDataModel;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "ReportFormatActionBean")
@ViewScoped
public class ReportFormatActionBean extends BaseBean {

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<FormatFile> dataRepService;

	public void setDataRepService(IDataRepService<FormatFile> dataRepService) {
		this.dataRepService = dataRepService;
	}

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
	}

	@ManagedProperty(value = "#{FormatFileService}")
	private IFormatFileService formatFileService;

	public void setFormatFileService(IFormatFileService formatFileService) {
		this.formatFileService = formatFileService;
	}

	private ReportType reportType;
	private FormatFile formatFile;
	private FormatFile selectedFF;
	private boolean selection;
	private boolean createNew;
	private List<ReportFormatDto> dtoList;
	private List<FormatFile> ffList;
	private List<ChartOfAccount> coaList;
	private SelectableIDDataModel<FormatFile> selectableFFList;
	private ReportFormatDto dto;
	private FormatFile tempFormatFile;
	private ChartOfAccount rangeStart;
	private ChartOfAccount rangeEnd;
	private int ffListVar;
	private int lineStart;
	private int lineEnd;
	
	private List<FormatFile> oldffList;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		reportType = ReportType.PL;
		ffList = new ArrayList<FormatFile>();
		if (getParam(ParamId.COAENTRY_COALIST) == null) {
			coaList = coaService.findAllCoa();
			putParam(ParamId.COAENTRY_COALIST, coaList);
		} else {
			coaList = (List<ChartOfAccount>) getParam(ParamId.COAENTRY_COALIST);
		}
		selection = true;
	}

	public void selectReportType() {
		if (reportType != null) {
			selection = false;
			createNewFormatFile();
			rebindData();
		} else {
			addInfoMessage(null, MessageId.REQUIRE_REPORT_TYPE);
		}
	}

	public void resetFormatFile(FormatFile ff) {
		ff.setChartOfAccount(null);
		ff.setDesp(null);
		ff.setDepartment(null);
	}

	public void back() {
		reportType = ReportType.PL;
		selection = true;
		createNew = true;
	}

	public void rebindData() {
		dtoList = formatFileService.findByReportType(reportType);
	}

	public void createNewFormatFile() {
		ffList = new ArrayList<FormatFile>();
		createNew = true;
		tempFormatFile = new FormatFile();
		formatFile = new FormatFile();
		formatFile.setlNo(1);
		formatFile.setShowHide(true);
		formatFile.setColType(ColType.R);
		formatFile.setAmountTotal(true);
		formatFile.setReportType(reportType);
		ffList.add(formatFile);
	}

	public void addNewFormatFile() {
		try {
			if (validate()) {
				List<FormatFile> temp = formatFileService.findByTypeAndName(formatFile.getFormatType(), formatFile.getFormatName());
				if (temp.size() == 0) {
					formatFileService.insert(formatFile, ffList);
					addInfoMessage(null, MessageId.INSERT_SUCCESS, formatFile.getFormatType());
					createNewFormatFile();
					rebindData();
				} else {
					addErrorMessage(null, MessageId.DUPE_FORMATFILE);
				}
			} else {
				addErrorMessage(null, MessageId.ERROR_FORMULA_FOUND);
			}
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public void prepareUpdateDto(ReportFormatDto dto) {
		createNew = false;
		oldffList = new ArrayList<>();
		tempFormatFile = new FormatFile(dto.getFormatType(), dto.getFormatName());
		formatFile = new FormatFile(dto.getFormatType(), dto.getFormatName());
		ffList = formatFileService.findByTypeAndName(dto.getFormatType(), dto.getFormatName());
		oldffList.addAll(ffList);
		
	}

	public void updateFormatFile() {
		try {
			if (validate()) {
				// formatFileService.updateFormatFile(formatFile,
				// tempFormatFile, ffList);
				formatFileService.deleteRange(oldffList);
				formatFileService.insert(formatFile, ffList);
				addInfoMessage(null, MessageId.UPDATE_SUCCESS, formatFile.getFormatType());
				rebindData();
				ffList = new ArrayList<FormatFile>();
				createNewFormatFile();
			} else {
				addErrorMessage(null, MessageId.ERROR_FORMULA_FOUND);
			}
		} catch (SystemException e) {
			handleSysException(e);
		}
	}


	private boolean validate() {
		for (FormatFile ff : ffList) {
			if (ff.isInvalidFormula())
				return false;
		}
		return true;
	}

	public void deleteDto(ReportFormatDto dto) {
		try {
			formatFileService.delete(dto);
			addInfoMessage(null, MessageId.DELETE_SUCCESS, dto.getFormatType());
			createNewFormatFile();
			rebindData();
			this.dto = new ReportFormatDto();
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public void addNewLine() {
		FormatFile ff = new FormatFile();
		if (selectedFF != null) {
			ff.setlNo(selectedFF.getlNo() + 1);
			for (FormatFile ff2 : ffList) {
				if (ff2.getlNo() > selectedFF.getlNo()) {
					ff2.setlNo(ff2.getlNo() + 1);
				}
			}
		} else {
			if (ffList.size() > 0)
				ff.setlNo(ffList.get(ffList.size() - 1).getlNo() + 1);
			else
				ff.setlNo(1);
		}

		ff.setShowHide(true);
		ff.setColType(ColType.R);
		ff.setAmountTotal(true);
		ff.setReportType(reportType);
		ffList.add(ff);
	}

	public void selectRange() {
		int start = coaList.indexOf(rangeStart);
		int end = coaList.indexOf(rangeEnd);

		if (start > end) {
			addErrorMessage("rangeSelectionForm:rangeStart", MessageId.RANGE_ERROR);
			addErrorMessage(null, MessageId.RANGE_ERROR);
		} else {

			int size = end - start + 1;
			if (selectedFF != null) {
				for (FormatFile ff2 : ffList) {
					if (ff2.getlNo() > selectedFF.getlNo()) {
						ff2.setlNo(ff2.getlNo() + size);
					}
				}
			}

			int increment = 0;
			for (; start <= end; start++) {
				FormatFile ff = new FormatFile();
				if (selectedFF != null) {
					ff.setlNo(selectedFF.getlNo() + 1 + increment);
				} else {
					ff.setlNo(ffList.size() + 1);
				}
				ff.setShowHide(true);
				ff.setColType(ColType.R);
				ff.setAmountTotal(true);
				ff.setReportType(reportType);
				ff.setChartOfAccount(coaList.get(start));
				ff.setDesp(coaList.get(start).getAcName());
				ffList.add(ff);
				increment++;
			}

			PrimeFaces.current().executeScript("PF('lineRangeDialog').hide()");
			rangeStart = null;
			rangeEnd = null;
		}
	}

	public void deleteLine() {
		if (selectedFF != null) {
			ffList.remove(selectedFF);
			for (FormatFile ff : ffList) {
				if (ff.getlNo() > selectedFF.getlNo()) {
					ff.setlNo(ff.getlNo() - 1);
				}
			}
			selectedFF = null;
		} else {
			ffList.remove(ffList.size() - 1);
		}
	}

	public void deleteRange() {
		int maxLine = ffList.get(ffList.size() - 1).getlNo();
		if (lineStart > maxLine || lineEnd > maxLine) {
			addErrorMessage("deleteRangeSelectionForm:lineStart", MessageId.RANGE_EXCEEDED, maxLine);
		} else if (lineStart > lineEnd) {
			addErrorMessage("deleteRangeSelectionForm:lineStart", MessageId.RANGE_ERROR);
		} else {

			lineStart--;
			lineEnd--;
			for (FormatFile ff : ffList) {
				if (ff.getlNo() > lineEnd) {
					ff.setlNo(ff.getlNo() - (lineEnd - lineStart + 1));
				}
			}

			for (; lineEnd >= lineStart; lineEnd--) {
				ffList.remove(lineEnd);
			}

			lineStart = 0;
			lineEnd = 0;
			PrimeFaces.current().executeScript("PF('deleteRangeDialog').hide()");
		}

	}

	public void checkFormula(FormatFile ff) {
		String formula = ff.getOther().trim();
		int length = formula.length();

		if (formula == null || formula.isEmpty()) {
			return;
		}

		if (!formula.matches("^[\\d\\{\\}\\+\\-\\*\\/\\.\\,]+$")) {
			ff.setInvalidFormula(true);
			addErrorMessage(null, MessageId.INVALID_FORMAT);
			return;
		}
		int maxLine = ffList.get(ffList.size() - 1).getlNo();

		// should have use regex
		List<FIType> expected = new ArrayList<FIType>();
		String prevOperator = null;
		boolean ocbExist = false;
		int ocbPos = 0;
		String numString = "";
		expected = FIType.set(expected, FIType.DIGIT, FIType.BRACE);

		for (int currentIndex = 0; currentIndex < length; currentIndex++) {
			String currentChar = String.valueOf(formula.charAt(currentIndex));
			FIType currentType = FIType.getType(currentChar);
			if (expected.contains(currentType)) {
				if (currentType.equals(FIType.BRACE)) {
					if (currentChar.equals("{")) {
						if (ocbExist) {
							ff.setInvalidFormula(true);
							addErrorMessage(null, MessageId.DUPE_CHAR, ocbPos, BusinessUtil.getPosition(currentIndex));
							return;
						} else {
							ocbExist = true;
							ocbPos = currentIndex + 1;
							prevOperator = currentChar;
							expected = FIType.set(expected, FIType.DIGIT);
							continue;
						}
					} else {
						if (ocbExist) {
							ocbExist = false;
							expected = FIType.set(expected, FIType.OPERATOR);
							continue;
						} else {
							addErrorMessage(null, MessageId.REQUIRED_OCB);
							return;
						}
					}
				} else if (currentType.equals(FIType.DIGIT)) {
					if (prevOperator != null) {
						if (FIType.getType(prevOperator).equals(FIType.COMMADOT)) {
							expected = FIType.set(expected, FIType.DIGIT, FIType.BRACE);
							numString = numString + currentChar;
							int latestLineNo = Integer.parseInt(String.valueOf(numString.charAt(numString.length() - 1)));
							if (!Character.isDigit(formula.charAt(currentIndex + 1)) && latestLineNo > maxLine) {
								ff.setInvalidFormula(true);
								addErrorMessage(null, MessageId.LINE_EXCEEDED, numString, maxLine, BusinessUtil.getPosition(currentIndex - (numString.length() - 1)));
								return;
							}
						} else if (FIType.getType(prevOperator).equals(FIType.BRACE)) {
							if (prevOperator.equals("{")) {
								// handle closing curly brace here
								expected = FIType.set(expected, FIType.DIGIT, FIType.OPERATOR, FIType.COMMADOT, FIType.BRACE);
								numString = numString + currentChar;
								int latestLineNo = Integer.parseInt(String.valueOf(numString.charAt(numString.length() - 1)));
								if (!Character.isDigit(formula.charAt(currentIndex + 1)) && latestLineNo > maxLine) {
									ff.setInvalidFormula(true);
									addErrorMessage(null, MessageId.LINE_EXCEEDED, numString, maxLine, BusinessUtil.getPosition(currentIndex - (numString.length() - 1)));
									return;
								}
							}
						} else if (FIType.getType(prevOperator).equals(FIType.OPERATOR)) {
							if (ocbExist) {
								expected = FIType.set(expected, FIType.DIGIT, FIType.OPERATOR, FIType.BRACE);
								numString = numString + currentChar;
								int latestLineNo = Integer.parseInt(String.valueOf(numString.charAt(numString.length() - 1)));
								if (!Character.isDigit(formula.charAt(currentIndex + 1)) && latestLineNo > maxLine) {
									ff.setInvalidFormula(true);
									addErrorMessage(null, MessageId.LINE_EXCEEDED, numString, maxLine, BusinessUtil.getPosition(currentIndex - (numString.length() - 1)));
									return;
								}
							} else {
								expected = FIType.set(expected, FIType.DIGIT, FIType.OPERATOR);
							}
						}
					} else {
						expected = FIType.set(expected, FIType.DIGIT, FIType.OPERATOR);
					}
					continue;
				} else if (currentType.equals(FIType.OPERATOR)) {
					prevOperator = currentChar;
					if (ocbExist) {
						expected = FIType.set(expected, FIType.DIGIT);
						numString = "";
					} else {
						expected = FIType.set(expected, FIType.DIGIT, FIType.BRACE);
					}
					continue;
				} else if (currentType.equals(FIType.COMMADOT)) {
					numString = "";
					prevOperator = currentChar;
					expected = FIType.set(expected, FIType.DIGIT);

					continue;
				}
			} else {
				if (currentIndex == 0) {
					ff.setInvalidFormula(true);
					addErrorMessage(null, MessageId.INVALID_START);
					return;
				}
				if (prevOperator != null) {
					ff.setInvalidFormula(true);
					addErrorMessage(null, MessageId.DUPE_CHAR, BusinessUtil.getPosition(currentIndex - 1), BusinessUtil.getPosition(currentIndex));
					return;
				}
				if (currentChar.equals("{")) {
					if (prevOperator == null) {
						// when { is detected without +-*/ operator
						ff.setInvalidFormula(true);
						addErrorMessage(null, MessageId.INVALID_OCB, BusinessUtil.getPosition(currentIndex));
						return;
					}
				}
				if (currentChar.equals("}")) {
					if (prevOperator == null) {
						// when } is detected without +-*/ operator
						ff.setInvalidFormula(true);
						addErrorMessage(null, MessageId.INVALID_CCB, BusinessUtil.getPosition(currentIndex));
						return;
					}
				}
			}
		}
		if (!ocbExist) {
			ff.setStatus(true);
			ff.setInvalidFormula(false);
		} else {
			addErrorMessage(null, MessageId.REQUIRED_CCB);
			return;
		}
	}

	public void returnCoa(SelectEvent event) {
		ChartOfAccount coa = (ChartOfAccount) event.getObject();
		ffList.get(ffListVar).setChartOfAccount(coa);
		ffList.get(ffListVar).setDesp(coa.getAcName());
	}

	public void returnRangeStart(SelectEvent event) {
		ChartOfAccount coa = (ChartOfAccount) event.getObject();
		setRangeStart(coa);
	}

	public void returnRangeEnd(SelectEvent event) {
		ChartOfAccount coa = (ChartOfAccount) event.getObject();
		setRangeEnd(coa);
	}

	public boolean isSelection() {
		return selection;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public List<ReportType> getReportTypes() {
		return Arrays.asList(ReportType.values());
	}

	public void setFormatFile(FormatFile formatFile) {
		this.formatFile = formatFile;
	}

	public FormatFile getFormatFile() {
		return formatFile;
	}

	public List<ReportFormatDto> getDtoList() {
		return dtoList;
	}

	public void setDto(ReportFormatDto dto) {
		this.dto = dto;
	}

	public ReportFormatDto getDto() {
		return dto;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public List<FormatFile> getFfList() {
		return ffList;
	}

	private List<ReportFormatDto> filteredList;

	public List<ReportFormatDto> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<ReportFormatDto> filteredList) {
		this.filteredList = filteredList;
	}

	private List<FormatFile> ffFilteredList;

	public List<FormatFile> getFfFilteredList() {
		return ffFilteredList;
	}

	public void setFfFilteredList(List<FormatFile> ffFilteredList) {
		this.ffFilteredList = ffFilteredList;
	}

	public FormatFile getSelectedFF() {
		return selectedFF;
	}

	public void setSelectedFF(FormatFile selectedFF) {
		this.selectedFF = selectedFF;
	}

	public void setRangeStart(ChartOfAccount rangeStart) {
		this.rangeStart = rangeStart;
	}

	public void setRangeEnd(ChartOfAccount rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	public ChartOfAccount getRangeEnd() {
		return rangeEnd;
	}

	public ChartOfAccount getRangeStart() {
		return rangeStart;
	}

	public List<ColType> getColTypes() {
		return Arrays.asList(ColType.values());
	}

	public SelectableIDDataModel<FormatFile> getSelectableFFList() {
		Collections.sort(ffList);
		selectableFFList = new SelectableIDDataModel<FormatFile>(ffList);
		return selectableFFList;
	}

	public void setSelectableFFList(SelectableIDDataModel<FormatFile> selectableFFList) {
		this.selectableFFList = selectableFFList;
	}

	public void setFfList(List<FormatFile> ffList) {
		this.ffList = ffList;
	}

	public void setFfListVar(int ffListVar) {
		this.ffListVar = ffListVar;
	}

	public int getFfListVar() {
		return ffListVar;
	}

	public void onRowSelect(SelectEvent event) {
		setSelectedFF((FormatFile) event.getObject());
	}

	public void setLineStart(int lineStart) {
		this.lineStart = lineStart;
	}

	public void setLineEnd(int lineEnd) {
		this.lineEnd = lineEnd;
	}

	public int getLineStart() {
		return lineStart;
	}

	public int getLineEnd() {
		return lineEnd;
	}
}
