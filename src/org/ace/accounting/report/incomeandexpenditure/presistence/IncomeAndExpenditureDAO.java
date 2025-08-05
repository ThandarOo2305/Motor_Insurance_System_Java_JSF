package org.ace.accounting.report.incomeandexpenditure.presistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.report.incomeandexpenditure.IncomeAndExpenditureDto;
import org.ace.accounting.report.incomeandexpenditure.IncomeExpenseCriteria;
import org.ace.accounting.report.incomeandexpenditure.presistence.interfaces.IIncomeAndExpenditureDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/***************************************************************************************
 * @Date 2016-04-25
 * @author PPA
 * @Version 1.0
 * @Purpose This class serves as Data Manipulation to retrieve
 *          <code>IncomeAndExpenditure</code> Income and Expenditure Report
 *          process
 * 
 ***************************************************************************************/

@Repository("IncomeAndExpenditureDAO")
public class IncomeAndExpenditureDAO extends BasicDAO implements IIncomeAndExpenditureDAO {

	// To generate log
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Operation has not been written yet.
	 * 
	 * @param IncomeExpenseCriteria.
	 * 
	 * @return List<IncomeAndExpenditure>.
	 * @throws DAOException.
	 * 
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IncomeAndExpenditureDto> find(IncomeExpenseCriteria criteria) throws DAOException {
		List<IncomeAndExpenditureDto> result = null;
		try {
			logger.debug("find() method for IncomeAndExpenditure Report has been started.");
			String view;
			if (criteria.isGroup()) {
				view = "VW_INCOME_EXPENSE_GROUP";
			} else {
				if (criteria.getBranch() == null) {
					view = "VW_INCOME_EXPENSE_DETAIL_AllBranch";
				} else {
					view = "VW_INCOME_EXPENSE_DETAIL";
				}
			}

			String sumFields = " ACODE ";
			String fields = " ACODE ";
			if (criteria.isGroup()) {
				sumFields += ",ACNAME ";
				fields += ",ACNAME ";
			} else {
				if (criteria.getBranch() == null) {
					sumFields += " ,GROUPACNAME ";
					fields += " ,GROUPACNAME ";
				} else {
					sumFields += ",SUBACNAME ";
					fields += ",SUBACNAME ";
				}
			}

			sumFields += ",ACTYPE ";
			String groupByFields = sumFields;
			fields += ",ACTYPE ";

			String mPrefix;
			String ytdPrefix;
			if (criteria.isHomeCur() || criteria.isHomeConverted()) {
				mPrefix = "MSRC";
				ytdPrefix = "YTDMSRC";
			} else {
				mPrefix = "M";
				ytdPrefix = "YTDM";
			}

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);// set to first day of the month ,
												// doesn't really matter
			cal.set(Calendar.YEAR, criteria.getYear());
			cal.set(Calendar.MONTH, criteria.getMonth());
			Date reportDate = cal.getTime();
			String budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);

			if (!criteria.isQuarterly()) {
				String budgetMonth = BusinessUtil.getBudgetInfo(reportDate, 3);
				sumFields += ", SUM(" + ytdPrefix + budgetMonth + ") , SUM(" + mPrefix + budgetMonth + ") ";
				fields += ", " + ytdPrefix + budgetMonth + " ," + mPrefix + budgetMonth + " ";
			} else {
				String ytdCol = "";
				String mCols = "";
				String sumMCols = "";
				if (criteria.getQuarter() >= 1) {
					ytdCol += ", " + ytdPrefix + "1 + " + ytdPrefix + "2 + " + ytdPrefix + "3 ";
					mCols += mPrefix + "1 , " + mPrefix + "2 , " + mPrefix + "3 ";
					sumMCols += ", SUM(" + mPrefix + "1) , " + "SUM(" + mPrefix + "2) , " + "SUM(" + mPrefix + "3) ";
				}
				if (criteria.getQuarter() >= 2) {
					ytdCol += " + " + ytdPrefix + "4 + " + ytdPrefix + "5 + " + ytdPrefix + "6 ";
					mCols += " , " + mPrefix + "4 , " + mPrefix + "5 , " + mPrefix + "6 ";
					sumMCols += " , SUM(" + mPrefix + "4) , " + "SUM(" + mPrefix + "5) , " + "SUM(" + mPrefix + "6) ";
				}
				if (criteria.getQuarter() >= 3) {
					ytdCol += " + " + ytdPrefix + "7 + " + ytdPrefix + "8 + " + ytdPrefix + "9 ";
					mCols += " , " + mPrefix + "7 , " + mPrefix + "8 , " + mPrefix + "9 ";
					sumMCols += " , SUM(" + mPrefix + "7) , " + "SUM(" + mPrefix + "8) , " + "SUM(" + mPrefix + "9) ";
				}
				if (criteria.getQuarter() >= 4) {
					ytdCol += " + " + ytdPrefix + "10 + " + ytdPrefix + "11 + " + ytdPrefix + "12 ";
					mCols += " , " + mPrefix + "10 , " + mPrefix + "11 , " + mPrefix + "12 ";
					sumMCols += " , SUM(" + mPrefix + "10) , " + "SUM(" + mPrefix + "11) , " + "SUM(" + mPrefix + "12) ";
				}
				ytdCol += " AS YTDTOTAL ";
				sumFields += ", SUM(YTDTOTAL) " + sumMCols;
				fields += ytdCol + " , " + mCols;
			}

			StringBuffer query = new StringBuffer();
			query.append("SELECT " + sumFields + "FROM ( SELECT " + fields + " FROM " + view + " V WHERE BUDGET=?1 ");
			int index = 1;
			if (!criteria.isHomeCur() && criteria.getCurrency() != null) {
				index++;
				query.append(" AND CURID =?" + String.valueOf(index));
			}
			if (criteria.getBranch() != null) {
				index++;
				query.append(" AND BRANCHID =?" + String.valueOf(index));
			}
			query.append(" ) V ");
			query.append("GROUP BY " + groupByFields);
			query.append(" ORDER BY ACTYPE DESC,ACODE ASC");

			Query q = em.createNativeQuery(query.toString());
			q.setParameter(1, budgetYear);
			index = 1;
			if (!criteria.isHomeCur() && criteria.getCurrency() != null) {
				index++;
				q.setParameter(index, criteria.getCurrency().getId());
			}
			if (criteria.getBranch() != null) {
				index++;
				q.setParameter(index, criteria.getBranch().getId());
			}

			List<Object[]> objList = q.getResultList();
			if (objList.size() == 0) {
				throw new DAOException("101010101", "No data to report.", new Throwable());
			} else {
				result = new ArrayList<>();
				for (Object[] obj : objList) {
					IncomeAndExpenditureDto dto = new IncomeAndExpenditureDto();
					dto.setAcCode(obj[0].toString());
					dto.setAcName(obj[1].toString());
					dto.setAcType(obj[2].toString());
					if (!criteria.isQuarterly()) {
						dto.setYearToDate(new BigDecimal(obj[3].toString()));
						dto.setThisMonth(new BigDecimal(obj[4].toString()));
					} else {
						dto.setYearToDate(new BigDecimal(obj[3].toString()));
						if (criteria.getQuarter() >= 1) {
							dto.setM1(new BigDecimal(obj[4].toString()));
							dto.setM2(new BigDecimal(obj[5].toString()));
							dto.setM3(new BigDecimal(obj[6].toString()));
						}
						if (criteria.getQuarter() >= 2) {
							dto.setM4(new BigDecimal(obj[7].toString()));
							dto.setM5(new BigDecimal(obj[8].toString()));
							dto.setM6(new BigDecimal(obj[9].toString()));
						}
						if (criteria.getQuarter() >= 3) {
							dto.setM7(new BigDecimal(obj[10].toString()));
							dto.setM8(new BigDecimal(obj[11].toString()));
							dto.setM9(new BigDecimal(obj[12].toString()));
						}
						if (criteria.getQuarter() >= 4) {
							dto.setM10(new BigDecimal(obj[13].toString()));
							dto.setM11(new BigDecimal(obj[14].toString()));
							dto.setM12(new BigDecimal(obj[15].toString()));
						}
					}
					result.add(dto);
				}
			}

			em.flush();
			logger.debug("find() method for IncomeAndExpenditure Report has been successfully finished.");
		} catch (PersistenceException pe) {
			throw translate("Failed to find IncomeAndExpenditure Report Data by criteria.", pe);
		}
		return result;
	}
}
