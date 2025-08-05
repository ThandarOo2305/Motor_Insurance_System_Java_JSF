package org.ace.accounting.report.reportStatement.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.dto.LiabilitiesACDto;
import org.ace.accounting.dto.ReportStatementDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.report.reportStatement.persistence.interfaces.IReportStatementDAO;
import org.ace.accounting.report.reportStatement.service.interfaces.IReportStatementService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.formatfile.ColType;
import org.ace.accounting.system.formatfile.FormatFile;
import org.ace.accounting.system.formatfile.persistence.interfaces.IFormatFileDAO;
import org.ace.accounting.web.common.FIType;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "ReportStatementService")
public class ReportStatementService extends BaseService implements IReportStatementService {
	// TODO -- for monthly formula process SP_MonthlyReport cannot be found
	// anywhere

	@Resource(name = "ReportStatementDAO")
	private IReportStatementDAO reportStatementDAO;

	@Resource(name = "FormatFileDAO")
	private IFormatFileDAO formatFileDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ReportStatementDto> previewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String formatType) throws Exception {
		List<ReportStatementDto> result = null;
		try {
			result = reportStatementDAO.previewProcedure(isObal, reportType, currencyType, currency, branch, reportDate, formatType);
			result = formulaProcess(result, reportType, formatType);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Preview procedure failed", e);
		}
		return result;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ReportStatementDto> previewProcedureForCloneData(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch,
			Date reportDate, String budgetYear, String formatType) throws Exception {
		List<ReportStatementDto> result = null;
		try {
			result = reportStatementDAO.previewProcedureForCloneData(isObal, reportType, currencyType, currency, branch, reportDate, budgetYear, formatType);
			result = formulaProcess(result, reportType, formatType);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Preview procedure failed", e);
		}
		return result;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ReportStatementDto> prevPreviewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String budgetYear, String formatType) throws Exception {
		List<ReportStatementDto> result = null;
		try {
			result = reportStatementDAO.prevPreviewProcedure(isObal, reportType, currencyType, currency, branch, reportDate, budgetYear, formatType);
			result = formulaProcess(result, reportType, formatType);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Preview procedure failed", e);
		}
		return result;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ReportStatementDto> liabilitiesAcodeCheck(LiabilitiesACDto plDto, LiabilitiesACDto taxDto, List<ReportStatementDto> dtoList) {
		try {

			// TODO
			// -- consult if this is needed

			// TODO
			// -- no department in coa
			boolean departmentStatus = BusinessUtil.isDepartment();

			// TODO -- no break
			// update "ALL" match ACODE by setting their CBAL to saved CBAL
			for (ReportStatementDto dto : dtoList) {
				if (departmentStatus) {
					if (plDto.getAcCode().equals(dto.getAcCode())) {
						if ((dto.getdCode() == null && plDto.getdCode() == null) || (dto.getdCode() != null && dto.getdCode().equals(plDto.getdCode()))) {
							dto.setcBal(plDto.getcBal());
						}
					}
					if (taxDto.getAcCode().equals(dto.getAcCode())) {
						if ((dto.getdCode() == null && taxDto.getdCode() == null) || (dto.getdCode() != null && dto.getdCode().equals(taxDto.getdCode()))) {
							dto.setcBal(taxDto.getcBal());
						}
					}
				} else {
					if (plDto.getAcCode().equals(dto.getAcCode())) {
						dto.setcBal(plDto.getcBal());
					}
					if (taxDto.getAcCode().equals(dto.getAcCode())) {
						dto.setcBal(taxDto.getcBal());
					}
				}
			}
			// } else {
			//
			// }

		} catch (

		DAOException e)

		{
			throw new SystemException(e.getErrorCode(), "Liabilities Acode Check failed", e);
		}
		return dtoList;

	}

	private List<ReportStatementDto> formulaProcess(List<ReportStatementDto> dtoList, ReportType reportType, String formatType) throws Exception {
		try {
			List<FormatFile> ffList;
			// For LR ( will not use )
			// TODO

			// ----- CHECK findLR method for more info
			// ffList = formatFileDAO.findLR(formatType,
			// reportType);
			// if (ffList != null) {
			// for (FormatFile ff : ffList) {
			// BigDecimal cbalTotal = BigDecimal.ZERO;
			// BigDecimal amtTotal = BigDecimal.ZERO;
			// BigDecimal obalTotal = BigDecimal.ZERO;
			// BigDecimal ramtTotal = BigDecimal.ZERO;
			// for (ReportStatementDto dto : dtoList) {
			// if (Integer.parseInt(ff.getlRange1()) <= dto.getLno() &&
			// dto.getLno() <= Integer.parseInt(ff.getlRange2())) {
			// cbalTotal = cbalTotal.add(dto.getcBal() != null ? dto.getcBal() :
			// BigDecimal.ZERO);
			// amtTotal = amtTotal.add(dto.getAmt() != null ? dto.getAmt() :
			// BigDecimal.ZERO);
			// obalTotal = obalTotal.add(dto.getoBal() != null ? dto.getoBal() :
			// BigDecimal.ZERO);
			// ramtTotal = ramtTotal.add(dto.getrAmt() != null ? dto.getrAmt() :
			// BigDecimal.ZERO);
			// }
			// }
			// for (ReportStatementDto dto : dtoList) {
			// if (dto.getLno() == ff.getlNo()) {
			// dto.setcBal(cbalTotal);
			// dto.setAmt(amtTotal);
			// dto.setoBal(obalTotal);
			// dto.setrAmt(ramtTotal);
			// }
			// }
			// }
			// }
			// -----

			// For Other
			// TODO consult about BFAmount
			// TODO consult why other is only column type L
			ffList = formatFileDAO.findOther(ColType.L, formatType, reportType);
			for (FormatFile ff : ffList) {
				for (ReportStatementDto dto : dtoList) {
					if (dto.getLno() == ff.getlNo()) {
						dto = calculateFormula(ColType.L, dtoList, dto, ff.getOther());
					}
				}
			}
			ffList = formatFileDAO.findOther(ColType.R, formatType, reportType);
			for (FormatFile ff : ffList) {
				for (ReportStatementDto dto : dtoList) {
					if (dto.getRlno() == ff.getlNo()) {
						dto = calculateFormula(ColType.R, dtoList, dto, ff.getOther());
						dto.setrDesp(ff.getDesp());
					}
				}
			}
			ffList = formatFileDAO.findOther(ColType.B, formatType, reportType);
			for (FormatFile ff : ffList) {
				for (ReportStatementDto dto : dtoList) {
					if (dto.getLno() == ff.getlNo()) {
						dto = calculateFormula(ColType.B, dtoList, dto, ff.getOther());
						dto.setDesp(ff.getDesp());
						dto.setrDesp(ff.getDesp());
					}
				}
			}

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Formula process failed.", e);
		}
		return dtoList;
	}

	// {1-2}
	private ReportStatementDto calculateFormula(ColType coltype, List<ReportStatementDto> dtoList, ReportStatementDto targetDto, String formula) throws Exception {
		// targetDto is the total line which it's formula is being calculated
		// operand is for none line numbers

		int length = formula.length();
		String prevOperator = null;
		boolean ocbExist = false;
		boolean commaExist = false;
		String numString = "";
		int startLine = 0;
		int endLine = 0;
		for (int currentIndex = 0; currentIndex < length; currentIndex++) {
			String currentChar = String.valueOf(formula.charAt(currentIndex));
			FIType currentType = FIType.getType(currentChar);
			if (currentType.equals(FIType.DIGIT)) {
				numString += currentChar;
				FIType nextType = null;
				if (currentIndex + 1 < length) {
					String nextChar = String.valueOf(formula.charAt(currentIndex + 1));
					nextType = FIType.getType(nextChar);
				} else {
					nextType = FIType.BRACE;
					ocbExist = false;
				}

				// if next character is not digit anymore , handle the number
				if (!nextType.equals(FIType.DIGIT)) {
					if (ocbExist) {
						// If current type is digit and there was opening brace
						// ,
						// it is a line number
						if (nextType.equals(FIType.COMMADOT)) {
							// if next type is comma , then save the current
							// line no for later use
							startLine = Integer.parseInt(numString);
							numString = "";
						} else if (nextType.equals(FIType.BRACE)) {
							if (startLine > 0 && commaExist) {
								endLine = Integer.parseInt(numString);
								if (coltype.equals(ColType.L)) {
									BigDecimal rangeCbalTotal = BigDecimal.ZERO;
									BigDecimal rangeObalTotal = BigDecimal.ZERO;
									BigDecimal rangeAmtTotal = BigDecimal.ZERO;
									BigDecimal rangeBfamountTotal = BigDecimal.ZERO;
									BigDecimal rangeTotalTotal = BigDecimal.ZERO;
									for (int i = startLine; i <= endLine; i++) {
										ReportStatementDto rangeDto = getAmountOfLine(dtoList, i);
										// TODO , rangeDto won't be null if
										// preview procedure query is handled
										if (rangeDto != null) {
											rangeCbalTotal = rangeCbalTotal.add(rangeDto.getcBal());
											rangeObalTotal = rangeObalTotal.add(rangeDto.getoBal());
											rangeAmtTotal = rangeAmtTotal.add(rangeDto.getAmt());
											rangeBfamountTotal = rangeBfamountTotal.add(rangeDto.getBfAmount());
											rangeTotalTotal = rangeTotalTotal.add(rangeDto.getTotal());
										}
									}
									targetDto = handleOperator(targetDto, rangeCbalTotal, rangeObalTotal, rangeAmtTotal, rangeBfamountTotal, rangeTotalTotal, null, prevOperator);
								} else if (coltype.equals(ColType.R)) {
									BigDecimal rangeRamtTotal = BigDecimal.ZERO;
									BigDecimal rangeCbalTotal = BigDecimal.ZERO;
									BigDecimal rangeObalTotal = BigDecimal.ZERO;
									BigDecimal rangeAmtTotal = BigDecimal.ZERO;
									BigDecimal rangeBfamountTotal = BigDecimal.ZERO;
									BigDecimal rangeTotalTotal = BigDecimal.ZERO;
									for (int i = startLine; i <= endLine; i++) {
										ReportStatementDto rangeDto = getAmountOfLine(dtoList, i);
										if (rangeDto != null) {
											rangeCbalTotal = rangeCbalTotal.add(rangeDto.getcBal());
											rangeObalTotal = rangeObalTotal.add(rangeDto.getoBal());
											rangeAmtTotal = rangeAmtTotal.add(rangeDto.getAmt());
											rangeBfamountTotal = rangeBfamountTotal.add(rangeDto.getBfAmount());
											rangeTotalTotal = rangeTotalTotal.add(rangeDto.getTotal());
										}
										rangeRamtTotal = rangeRamtTotal.add(rangeDto.getrAmt());
									}
									targetDto = handleOperator(targetDto, rangeCbalTotal, rangeObalTotal, rangeAmtTotal, rangeBfamountTotal, rangeTotalTotal, null, prevOperator);
								} else if (coltype.equals(ColType.B)) {
									BigDecimal rangeCbalTotal = BigDecimal.ZERO;
									BigDecimal rangeRamtTotal = BigDecimal.ZERO;
									for (int i = startLine; i <= endLine; i++) {
										ReportStatementDto rangeDto = getAmountOfLine(dtoList, i);
										rangeCbalTotal = rangeCbalTotal.add(rangeDto.getcBal());
										rangeRamtTotal = rangeRamtTotal.add(rangeDto.getrAmt());
									}
									BigDecimal result = BigDecimal.ZERO;
									if (rangeCbalTotal.abs().doubleValue() > rangeRamtTotal.abs().doubleValue()) {
										result = rangeCbalTotal;
									} else {
										result = rangeRamtTotal;
									}
									targetDto = handleOperator(targetDto, result, null, null, null, null, result, prevOperator);
								}
								numString = "";
								prevOperator = null;
								startLine = 0;
								endLine = 0;
								commaExist = false;
							} else {
								// there is opening brace but no start line
								// value and comma , then it is single line
								// int latestLineNo =
								// Integer.parseInt(String.valueOf(numString.charAt(numString.length()
								// - 1)));
								// ReportStatementDto prevLineDto =
								// getAmountOfLine(dtoList, startLine);
								ReportStatementDto lineDto = getAmountOfLine(dtoList, Integer.parseInt(numString));
								if (coltype.equals(ColType.L)) {
									// if (null != prevLineDto) {
									// targetDto = handleOperator(targetDto,
									// prevLineDto.getcBal(),
									// prevLineDto.getoBal(),
									// prevLineDto.getAmt(),
									// prevLineDto.getBfAmount(),
									// prevLineDto.getTotal(), null, "+");
									// }
									//
									// targetDto = handleOperator(targetDto,
									// lineDto.getcBal(), lineDto.getoBal(),
									// lineDto.getAmt(), lineDto.getBfAmount(),
									// lineDto.getTotal(), null,
									// prevOperator);
									// } else if (coltype.equals(ColType.R)) {
									// if (null != prevLineDto) {
									// targetDto = handleOperator(targetDto,
									// null, null, null, null, null,
									// prevLineDto.getrAmt(), "+");
									// }
									//
									// targetDto = handleOperator(targetDto,
									// null, null, null, null, null,
									// lineDto.getrAmt(), prevOperator);
									// } else if (coltype.equals(ColType.B)) {
									// if (null != prevLineDto) {
									// targetDto = handleOperator(targetDto,
									// prevLineDto.getcBal(), null, null, null,
									// null, prevLineDto.getrAmt(), "+");
									// }
									//
									// targetDto = handleOperator(targetDto,
									// lineDto.getcBal(), null, null, null,
									// null,
									// lineDto.getrAmt(), prevOperator);
									// }

									targetDto = handleOperator(targetDto, lineDto.getcBal(), lineDto.getoBal(), lineDto.getAmt(), lineDto.getBfAmount(), lineDto.getTotal(), null,
											prevOperator);
								} else if (coltype.equals(ColType.R)) {
									targetDto = handleOperator(targetDto, lineDto.getcBal(), lineDto.getoBal(), lineDto.getAmt(), lineDto.getBfAmount(), lineDto.getTotal(),
											lineDto.getrAmt(), prevOperator);
								} else if (coltype.equals(ColType.B)) {
									targetDto = handleOperator(targetDto, lineDto.getcBal(), null, null, null, null, lineDto.getrAmt(), prevOperator);
								}
								prevOperator = null;
								numString = "";
							}
						}
					} else {
						// If current type is digit and there was NO opening
						// brace ,
						// it is a raw number
						if (prevOperator != null) {
							if (coltype.equals(ColType.L)) {
								targetDto = handleOperator(targetDto, new BigDecimal(numString), new BigDecimal(numString), new BigDecimal(numString), new BigDecimal(numString),
										new BigDecimal(numString), null, prevOperator);
							} else if (coltype.equals(ColType.R)) {
								targetDto = handleOperator(targetDto, new BigDecimal(numString), new BigDecimal(numString), new BigDecimal(numString), new BigDecimal(numString),
										new BigDecimal(numString), new BigDecimal(numString), prevOperator);
							} else if (coltype.equals(ColType.B)) {
								targetDto = handleOperator(targetDto, new BigDecimal(numString), null, null, null, null, new BigDecimal(numString), prevOperator);

							}
							prevOperator = null;
							numString = "";
						} else if (prevOperator == null && targetDto.isCbalObalAmtZero()) {
							// if there was no previous and the result is zero ,
							// it is probably the first number
							if (coltype.equals(ColType.L)) {
								targetDto.setcBal(new BigDecimal(Integer.parseInt(numString)));
								targetDto.setoBal(new BigDecimal(Integer.parseInt(numString)));
								targetDto.setAmt(new BigDecimal(Integer.parseInt(numString)));
							} else if (coltype.equals(ColType.R)) {
								targetDto.setrAmt(new BigDecimal(Integer.parseInt(numString)));
								targetDto.setcBal(new BigDecimal(Integer.parseInt(numString)));
								targetDto.setoBal(new BigDecimal(Integer.parseInt(numString)));
							} else if (coltype.equals(ColType.B)) {
								targetDto.setcBal(new BigDecimal(Integer.parseInt(numString)));
								targetDto.setrAmt(new BigDecimal(Integer.parseInt(numString)));
							}
							numString = "";
						}
					}
					continue;
				} else {
					// if next character is a digit , continue to get the full
					// number
					continue;
				}
			} else if (currentType.equals(FIType.BRACE)) {
				if (currentChar.equals("{")) {
					ocbExist = true;
					continue;
				} else if (currentChar.equals("}")) {
					ocbExist = false;
					continue;
				}
				continue;
			} else if (currentType.equals(FIType.OPERATOR)) {
				prevOperator = currentChar;
				// startLine = Integer.parseInt(numString);
				numString = "";
				continue;
			} else if (currentType.equals(FIType.COMMADOT)) {
				commaExist = true;
				continue;
			} else {
				continue;
			}
		}
		return targetDto;
	}

	private ReportStatementDto getAmountOfLine(List<ReportStatementDto> dtoList, int lNo) {
		for (ReportStatementDto dto : dtoList) {
			if (dto.getLno() == lNo) {
				return dto;
			}
		}
		return null;
	}

	private ReportStatementDto handleOperator(ReportStatementDto resultDto, BigDecimal cbal, BigDecimal obal, BigDecimal amt, BigDecimal bfamt, BigDecimal total, BigDecimal ramt,
			String prevOperator) throws Exception {
		if (prevOperator != null) {
			if (prevOperator.equals("+")) {
				if (cbal != null)
					resultDto.setcBal(resultDto.getcBal().add(cbal));
				if (obal != null)
					resultDto.setoBal(resultDto.getoBal().add(obal));
				if (amt != null)
					resultDto.setAmt(resultDto.getAmt().add(amt));
				if (bfamt != null)
					resultDto.setBfAmount(resultDto.getBfAmount().add(bfamt));
				if (total != null)
					resultDto.setTotal(resultDto.getTotal().add(total));
				if (ramt != null)
					resultDto.setrAmt(resultDto.getrAmt().add(ramt));
			} else if (prevOperator.equals("-")) {
				if (cbal != null)
					resultDto.setcBal(resultDto.getcBal().subtract(cbal));
				if (obal != null)
					resultDto.setoBal(resultDto.getoBal().subtract(obal));
				if (amt != null)
					resultDto.setAmt(resultDto.getAmt().subtract(amt));
				if (bfamt != null)
					resultDto.setBfAmount(resultDto.getBfAmount().subtract(bfamt));
				if (total != null)
					resultDto.setTotal(resultDto.getTotal().subtract(total));
				if (ramt != null)
					resultDto.setrAmt(resultDto.getrAmt().subtract(ramt));
			} else if (prevOperator.equals("*")) {
				if (cbal != null)
					resultDto.setcBal(resultDto.getcBal().multiply(cbal));
				if (obal != null)
					resultDto.setoBal(resultDto.getoBal().multiply(obal));
				if (amt != null)
					resultDto.setAmt(resultDto.getAmt().multiply(amt));
				if (bfamt != null)
					resultDto.setBfAmount(resultDto.getBfAmount().multiply(bfamt));
				if (total != null)
					resultDto.setTotal(resultDto.getTotal().multiply(total));
				if (ramt != null)
					resultDto.setrAmt(resultDto.getrAmt().multiply(ramt));
			} else if (prevOperator.equals("/")) {
				if (cbal != null)
					resultDto.setcBal(resultDto.getcBal().divide(cbal));
				if (obal != null)
					resultDto.setoBal(resultDto.getoBal().divide(obal));
				if (amt != null)
					resultDto.setAmt(resultDto.getAmt().divide(amt));
				if (bfamt != null)
					resultDto.setBfAmount(resultDto.getBfAmount().divide(bfamt));
				if (total != null)
					resultDto.setTotal(resultDto.getTotal().divide(total));
				if (ramt != null)
					resultDto.setrAmt(resultDto.getrAmt().divide(ramt));
			}
		} else {
			if (cbal != null)
				resultDto.setcBal(cbal);
			if (obal != null)
				resultDto.setoBal(obal);
			if (amt != null)
				resultDto.setAmt(amt);
			if (bfamt != null)
				resultDto.setBfAmount(bfamt);
			if (total != null)
				resultDto.setTotal(total);
			if (ramt != null)
				resultDto.setrAmt(ramt);
		}

		return resultDto;
	}
}