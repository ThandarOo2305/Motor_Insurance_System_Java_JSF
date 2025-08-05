package org.ace.accounting.report.costAllocation.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.dto.AllocateByDeptDto;
import org.ace.accounting.dto.AllocateProcessByDeptDto;
import org.ace.accounting.dto.AllocateProcessDto;
import org.ace.accounting.dto.CostAllocationReportDto;
import org.ace.accounting.report.costAllocation.persistence.interfaces.ICostAllocationDAO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.department.service.interfaces.IDepartmentService;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/********
 * 
 * @author aceplu-019
 *
 */
@Repository("CostAllocationDAO")
public class CostAllocationDAO extends BasicDAO implements ICostAllocationDAO {
	// To generate log
	private Logger logger = Logger.getLogger(getClass());

	@Resource(name = "DepartmentService")
	private IDepartmentService deptService;

	public void setDeptService(IDepartmentService deptService) {
		this.deptService = deptService;
	}

	@Resource(name = "DataRepService")
	private IDataRepService<ChartOfAccount> coaService;

	@Resource(name = "DataRepService")
	private IDataRepService<Currency> currencyService;
	@Resource(name = "DataRepService")
	private IDataRepService<Branch> branchService;

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CostAllocationReportDto> findCostAllocationReport(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth,
			boolean currencyConverted) {
		List<CostAllocationReportDto> result = new ArrayList<CostAllocationReportDto>();
		List<Object[]> tempResults = null;

		Calendar cal = Calendar.getInstance();
		int year = Integer.parseInt(allocateYear);
		int m = Integer.parseInt(allocateMonth);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, m);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date reportDate = cal.getTime();

		String mm = BusinessUtil.getBudgetInfo(reportDate, 3);

		String budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);

		String queryMonth = "";
		// String localMonth = BusinessUtil.getMonthStatus(m);

		String localMonth = "M" + mm;

		if (currencyType.equals(CurrencyType.HOMECURRENCY)) {
			// queryMonth = BusinessUtil.getMonthSRCStatus(m);
			queryMonth = "MSRC" + mm;
		} else {
			if (!currencyConverted) {
				// queryMonth = BusinessUtil.getMonthStatus(m);
				queryMonth = "M" + mm;
			} else {
				// queryMonth = BusinessUtil.getMonthSRCStatus(m);
				queryMonth = "MSRC" + mm;
			}

		}

		String dept = "";
		String strDept = "";
		List<Department> departments = deptService.findAllDepartment();
		for (Department d : departments) {
			strDept = strDept + "[" + d.getdCode() + "] AS " + d.getdCode() + ",";
			dept = dept + "[" + d.getdCode() + "],";
		}
		dept = dept.substring(0, dept.length() - 1);
		strDept = strDept.substring(0, strDept.length() - 1);
		StringBuffer queryString = null;
		try {
			logger.debug("findCostAllocationReport()method has been started");

			if (!currencyType.equals(CurrencyType.HOMECURRENCY)) {
				queryString = new StringBuffer("SELECT (SELECT ACCODE FROM COA WHERE ID = ACODEID) AS ACCODE,");
				queryString.append("(SELECT ACNAME FROM COA WHERE ID=ACODEID)AS ACNAME,");
				queryString.append("(SELECT ACTYPE FROM COA WHERE ID=ACODEID)AS ACTYPE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACCODE FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACCODE FROM COA WHERE ID=ALCODEHEADID) END ) AS HACODE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACNAME FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACNAME FROM COA WHERE ID=ALCODEHEADID) END ) AS HACNAME,");
				queryString.append("(SELECT CUR FROM CUR where ID=CURRENCYID),");
				queryString.append("(SELECT BRANCHCODE FROM BRANCH WHERE ID=BRANCHID)AS BRANCH,RATE, TOTAL, " + strDept + " FROM ");
				queryString.append("(SELECT (SUM(v." + queryMonth + ")* a.AMTPERCENT)/a.BASEDON AS BALANCE,");
				queryString.append("d.DCODE,a.COAID as ACODEID,SUM(v." + queryMonth + " ) AS TOTAL ,");
				queryString.append("cc.GROUPID AS ALCODEHEADID,");
				queryString.append("a.BUDGET , v.CURRENCYID , v.BRANCHID , ");
				queryString.append("CASE WHEN SUM(v." + localMonth + ")= 0 THEN Round((SUM(v. " + queryMonth + " )/1),2) ");
				queryString.append(" ELSE Round((SUM(v." + queryMonth + " )/SUM(v." + localMonth + " )),4) END as Rate  ");
				queryString.append("FROM VW_CCOA v JOIN ALLOCATE_CODE a   ON v.COAID=a.COAID AND v.BUDGET=a.BUDGET ");
				queryString.append("JOIN COA cc   ON cc.ID=a.COAID   JOIN DEPTCODE d ON d.ID=a.DEPARTMENTID ");
				queryString.append("GROUP BY v.COAID,a.COAID,a.AMTPERCENT,a.BASEDON,d.DCODE,a.BUDGET,cc.GROUPID,cc.HEADID,v.CURRENCYID,v.BRANCHID )  ");
				queryString.append("P PIVOT  (SUM(BALANCE) FOR DCODE IN (" + dept + ")) AS PVT   where PVT.BUDGET=?1 ");
			} else {
				queryString = new StringBuffer("SELECT (SELECT ACCODE FROM COA WHERE ID = ACODEID) AS ACCODE,");
				queryString.append("(SELECT ACNAME FROM COA WHERE ID=ACODEID)AS ACNAME,");
				queryString.append("(SELECT ACTYPE FROM COA WHERE ID=ACODEID)AS ACTYPE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACCODE FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACCODE FROM COA WHERE ID=ALCODEHEADID) END ) AS HACODE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACNAME FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACNAME FROM COA WHERE ID=ALCODEHEADID) END ) AS HACNAME,");
				queryString.append("(SELECT CUR FROM CUR where CUR='MMK'),");
				queryString.append("(SELECT BRANCHCODE FROM BRANCH WHERE ID=BRANCHID)AS BRANCH,1 as RATE, TOTAL, " + strDept + " FROM ");
				queryString.append("(SELECT (SUM(v." + queryMonth + ")* a.AMTPERCENT)/a.BASEDON AS BALANCE,");
				queryString.append("d.DCODE,a.COAID as ACODEID,SUM(v." + queryMonth + " ) AS TOTAL ,");
				queryString.append("cc.GROUPID AS ALCODEHEADID,");
				queryString.append("a.BUDGET , v.BRANCHID  ");
				// queryString.append("a.BUDGET , v.CURRENCYID , v.BRANCHID ,
				// ");
				// queryString.append("CASE WHEN SUM(v." + localMonth + ")= 0
				// THEN Round((SUM(v. " + queryMonth + " )/1),2) ");
				// queryString.append(" ELSE Round((SUM(v." + queryMonth + "
				// )/SUM(v." + localMonth + " )),4) END as Rate ");
				queryString.append("FROM VW_CCOA v JOIN ALLOCATE_CODE a   ON v.COAID=a.COAID AND v.BUDGET=a.BUDGET ");
				queryString.append("JOIN COA cc   ON cc.ID=a.COAID   JOIN DEPTCODE d ON d.ID=a.DEPARTMENTID ");
				queryString.append("GROUP BY v.COAID,a.COAID,a.AMTPERCENT,a.BASEDON,d.DCODE,a.BUDGET,cc.GROUPID,cc.HEADID,v.BRANCHID )  ");
				queryString.append("P PIVOT  (SUM(BALANCE) FOR DCODE IN (" + dept + ")) AS PVT   where PVT.BUDGET=?1 ");
			}

			int index = 1;
			if (branch != null) {
				index++;
				queryString.append(" AND PVT.BRANCHID=?" + String.valueOf(index));
			}
			if (currency != null) {
				index++;
				queryString.append(" AND PVT.CURRENCYID=?" + String.valueOf(index));
			}
			queryString.append(" order by ACCODE ");
			Query q = em.createNativeQuery(queryString.toString());
			q.setParameter("1", budgetYear);
			index = 1;
			if (branch != null) {
				index++;
				q.setParameter(String.valueOf(index), branch.getId());
			}
			if (currency != null) {
				index++;
				q.setParameter(String.valueOf(index), currency.getId());
			}
			tempResults = q.getResultList();
			em.flush();
			logger.debug("findCostAllocationReport() method has been successfully finished");
			for (Object obj[] : tempResults) {

				String acCode = obj[0].toString();
				String acName = obj[1].toString();
				String acType = obj[2].toString();
				String hacCode = obj[3].toString();
				String hacName = obj[4].toString();
				String currencyCode = obj[5].toString();
				String branchCode = obj[6].toString();
				BigDecimal rate = new BigDecimal(obj[7].toString());
				BigDecimal total = new BigDecimal(obj[8].toString());
				CostAllocationReportDto costAllocationReportDto = new CostAllocationReportDto(acCode, acName, acType, hacCode, hacName, currencyCode, branchCode, rate, total);

				int deptIndex = 0;
				for (int i = 9; i < obj.length; i++) {
					String dCode = departments.get(deptIndex).getdCode();
					BigDecimal amount = (BigDecimal) obj[i];
					costAllocationReportDto.putToMap(dCode, amount);
					deptIndex++;
				}

				result.add(costAllocationReportDto);
			}

		} catch (PersistenceException pe) {
			logger.error("findCostAllocationReport() method has been failed", pe);
			throw translate("Failed to find Cost Allocation", pe);
		}
		return result;

	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateProcessDto> findAllocateProcess(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth,
			boolean currencyConverted) {
		List<AllocateProcessDto> result = new ArrayList<AllocateProcessDto>();
		List<Object[]> tempResults = null;

		int m = Integer.parseInt(allocateMonth);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(allocateYear));
		cal.set(Calendar.MONTH, m);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date reportDate = cal.getTime();
		String budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
		String mm = BusinessUtil.getBudgetInfo(reportDate, 3);
		/*
		 * String queryMonth = ""; String localMonth =
		 * BusinessUtil.getMonthStatus(m); if
		 * (currencyType.equals(CurrencyType.HOMECURRENCY)) { queryMonth =
		 * BusinessUtil.getMonthSRCStatus(m); } else { if (!currencyConverted) {
		 * queryMonth = BusinessUtil.getMonthStatus(m); } else { queryMonth =
		 * BusinessUtil.getMonthSRCStatus(m); }
		 * 
		 * }
		 */
		String queryMonth = "";
		// String localMonth = BusinessUtil.getMonthStatus(m);

		String localMonth = "M" + mm;

		if (currencyType.equals(CurrencyType.HOMECURRENCY)) {
			// queryMonth = BusinessUtil.getMonthSRCStatus(m);
			queryMonth = "MSRC" + mm;
		} else {
			if (!currencyConverted) {
				// queryMonth = BusinessUtil.getMonthStatus(m);
				queryMonth = "M" + mm;
			} else {
				// queryMonth = BusinessUtil.getMonthSRCStatus(m);
				queryMonth = "MSRC" + mm;
			}

		}

		try {
			logger.debug("findAllocateProcess()method has been started");
			// StringBuffer queryString = new StringBuffer();
			// queryString.append("SELECT (SELECT ACCODE FROM COA WHERE ID =
			// ACODEID) AS ACCODE,");
			// queryString.append("(SELECT ACTYPE FROM COA WHERE ID=ACODEID)AS
			// ACTYPE,");
			// queryString.append("(SELECT CUR FROM CUR where ID=CURRENCYID)AS
			// CURRENCY,");
			// queryString.append("(SELECT BRANCHCODE FROM BRANCH WHERE
			// ID=BRANCHID)AS BRANCH,RATE, TOTAL, " + strDept + " FROM ");
			// queryString.append("(SELECT (SUM(v." + queryMonth + ")*
			// a.AMTPERCENT)/a.BASEDON AS BALANCE,");
			// queryString.append("d.DCODE,a.COAID as ACODEID,SUM(v." +
			// queryMonth + " ) AS TOTAL ,");
			// queryString.append("a.BUDGET , v.CURRENCYID , v.BRANCHID , ");
			// queryString.append("CASE WHEN SUM(v." + localMonth + ")= 0 THEN
			// Round((SUM(v. " + queryMonth + " )/1),2) ");
			// queryString.append(" ELSE Round((SUM(v." + queryMonth + "
			// )/SUM(v." + localMonth + " )),4) END as Rate ");
			// queryString.append("FROM VW_CCOA v JOIN ALLOCATE_CODE a ON
			// v.COAID=a.COAID AND v.BUDGET=a.BUDGET ");
			// queryString.append("JOIN COA cc ON cc.ID=a.COAID JOIN DEPTCODE d
			// ON d.ID=a.DEPARTMENTID ");
			// queryString.append("GROUP BY
			// v.COAID,a.COAID,a.AMTPERCENT,a.BASEDON,d.DCODE,a.BUDGET,v.CURRENCYID,v.BRANCHID
			// ) ");
			// queryString.append("P PIVOT (SUM(BALANCE) FOR DCODE IN (" + dept
			// + ")) AS PVT where PVT.BUDGET=?1 ");
			// queryString.append("AND PVT.TOTAL > 0");

			// ACode,Balance,ACType,cur,Rate
			StringBuffer queryString = new StringBuffer();
			if (!currencyType.equals(CurrencyType.HOMECURRENCY)) {
				queryString.append("SELECT (SELECT ACCODE FROM COA WHERE ID = ACODEID) AS ACCODE,BALANCE,");
				queryString.append("(SELECT ACTYPE FROM COA WHERE ID=ACODEID)AS ACTYPE,");
				queryString.append("(SELECT CUR FROM CUR where ID=CURRENCYID)AS CURRENCY,");
				queryString.append(" RATE FROM ");
				queryString.append("(SELECT (SUM(v." + queryMonth + ")* a.AMTPERCENT)/a.BASEDON AS BALANCE,");
				queryString.append("d.DCODE,a.COAID as ACODEID,SUM(v." + queryMonth + " ) AS TOTAL ,");
				queryString.append("a.BUDGET , v.CURRENCYID , v.BRANCHID , ");
				queryString.append("CASE WHEN SUM(v." + localMonth + ")= 0 THEN Round((SUM(v. " + queryMonth + " )/1),2) ");
				queryString.append(" ELSE Round((SUM(v." + queryMonth + " )/SUM(v." + localMonth + " )),4) END as Rate  ");
				queryString.append("FROM VW_CCOA v JOIN ALLOCATE_CODE a   ON v.COAID=a.COAID AND v.BUDGET=a.BUDGET ");
				queryString.append("JOIN COA cc   ON cc.ID=a.COAID   JOIN DEPTCODE d ON d.ID=a.DEPARTMENTID ");
				queryString.append("GROUP BY v.COAID,a.COAID,a.AMTPERCENT,a.BASEDON,d.DCODE,a.BUDGET,v.CURRENCYID,v.BRANCHID )  ");
				queryString.append("P where P.BUDGET=?1 ");
				queryString.append("AND P.BALANCE <> 0");
			} else {
				queryString.append("SELECT (SELECT ACCODE FROM COA WHERE ID = ACODEID) AS ACCODE,BALANCE,");
				queryString.append("(SELECT ACTYPE FROM COA WHERE ID=ACODEID)AS ACTYPE,");
				queryString.append("(SELECT CUR FROM CUR where  CUR='MMK')AS CURRENCY,");
				queryString.append(" RATE FROM ");
				queryString.append("(SELECT (SUM(v." + queryMonth + ")* a.AMTPERCENT)/a.BASEDON AS BALANCE,");
				queryString.append("d.DCODE,a.COAID as ACODEID,SUM(v." + queryMonth + " ) AS TOTAL ,");
				queryString.append("a.BUDGET  , v.BRANCHID , ");
				// queryString.append("CASE WHEN SUM(v." + localMonth + ")= 0
				// THEN Round((SUM(v. " + queryMonth + " )/1),2) ");
				// queryString.append(" ELSE Round((SUM(v." + queryMonth + "
				// )/SUM(v." + localMonth + " )),4) END as Rate ");
				queryString.append("1 as Rate ");
				queryString.append("FROM VW_CCOA v JOIN ALLOCATE_CODE a   ON v.COAID=a.COAID AND v.BUDGET=a.BUDGET ");
				queryString.append("JOIN COA cc   ON cc.ID=a.COAID   JOIN DEPTCODE d ON d.ID=a.DEPARTMENTID ");
				queryString.append("GROUP BY v.COAID,a.COAID,a.AMTPERCENT,a.BASEDON,d.DCODE,a.BUDGET,v.BRANCHID )  ");
				queryString.append("P where P.BUDGET=?1 ");
				queryString.append("AND P.BALANCE <> 0");
			}
			int index = 1;
			if (branch != null) {
				index++;
				queryString.append(" AND P.BRANCHID=?" + String.valueOf(index));
			}
			if (currency != null) {
				index++;
				queryString.append(" AND P.CURRENCYID=?" + String.valueOf(index));
			}
			Query q = em.createNativeQuery(queryString.toString());
			q.setParameter("1", budgetYear);
			index = 1;
			if (branch != null) {
				index++;
				q.setParameter(String.valueOf(index), branch.getId());
			}
			if (currency != null) {
				index++;
				q.setParameter(String.valueOf(index), currency.getId());
			}
			tempResults = q.getResultList();
			em.flush();
			logger.debug("findAllocateProcess()method has been successfully finished");
			for (Object obj[] : tempResults) {
				// ACode,Balance,ACType,cur,Rate
				String acCode = obj[0].toString();
				BigDecimal balance = new BigDecimal(obj[1].toString());
				String acType = obj[2].toString();
				String currencyCode = obj[3].toString();
				BigDecimal rate = new BigDecimal(obj[4].toString());
				AllocateProcessDto allocateProcessDto = new AllocateProcessDto(acCode, balance, acType, currencyCode, rate);
				result.add(allocateProcessDto);
			}

		} catch (PersistenceException pe) {
			logger.error("findAllocateProcess()method has been failed", pe);
			throw translate("Failed to find Allocate Process", pe);
		}
		return result;

	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateProcessByDeptDto> findAllocateProcessByDept(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth,
			boolean currencyConverted) {
		List<AllocateProcessByDeptDto> result = new ArrayList<AllocateProcessByDeptDto>();
		List<Object[]> tempResults = null;

		int m = Integer.parseInt(allocateMonth);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(allocateYear));
		cal.set(Calendar.MONTH, m);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date reportDate = cal.getTime();
		String budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
		String mm = BusinessUtil.getBudgetInfo(reportDate, 3);
		String queryMonth = "";
		/*
		 * String localMonth = BusinessUtil.getMonthStatus(m); if
		 * (currencyType.equals(CurrencyType.HOMECURRENCY)) { queryMonth =
		 * BusinessUtil.getMonthSRCStatus(m); } else { if (!currencyConverted) {
		 * queryMonth = BusinessUtil.getMonthStatus(m); } else { queryMonth =
		 * BusinessUtil.getMonthSRCStatus(m); }
		 * 
		 * }
		 */
		String localMonth = "M" + mm;

		if (currencyType.equals(CurrencyType.HOMECURRENCY)) {
			// queryMonth = BusinessUtil.getMonthSRCStatus(m);
			queryMonth = "MSRC" + mm;
		} else {
			if (!currencyConverted) {
				// queryMonth = BusinessUtil.getMonthStatus(m);
				queryMonth = "M" + mm;
			} else {
				// queryMonth = BusinessUtil.getMonthSRCStatus(m);
				queryMonth = "MSRC" + mm;
			}

		}

		String dept = "";
		String strDept = "";
		String groupDept = "";
		List<Department> departments = deptService.findAllDepartment();
		for (Department d : departments) {
			groupDept = groupDept + "SUM(" + d.getdCode() + ") AS " + d.getdCode() + ",";
			strDept = strDept + "[" + d.getdCode() + "] AS " + d.getdCode() + ",";
			dept = dept + "[" + d.getdCode() + "],";
		}
		groupDept = groupDept.substring(0, groupDept.length() - 1);
		dept = dept.substring(0, dept.length() - 1);
		strDept = strDept.substring(0, strDept.length() - 1);

		try {
			logger.debug("findAllocateProcessByDept()method has been started");
			StringBuffer queryString = new StringBuffer();
			if (!currencyType.equals(CurrencyType.HOMECURRENCY)) {
				queryString.append("Select HACODE,Cur, Rate," + groupDept + " FROM(");
				queryString.append("SELECT (SELECT ACCODE FROM COA WHERE ID = ACODEID)AS ACCODE,");
				queryString.append("(SELECT ACNAME FROM COA WHERE ID=ACODEID)AS ACNAME,");
				queryString.append("(SELECT ACTYPE FROM COA WHERE ID=ACODEID)AS ACTYPE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACCODE FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACCODE FROM COA WHERE ID=ALCODEHEADID) END ) AS HACODE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACNAME FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACNAME FROM COA WHERE ID=ALCODEHEADID) END ) AS HACNAME,");
				queryString.append("(SELECT CUR FROM CUR where ID=CURRENCYID) AS Cur,");
				queryString.append("RATE, TOTAL, " + strDept + " FROM ");
				queryString.append("(SELECT (SUM(v." + queryMonth + ")* a.AMTPERCENT)/a.BASEDON AS BALANCE,");
				queryString.append("d.DCODE,a.COAID as ACODEID,SUM(v." + queryMonth + " ) AS TOTAL ,");
				queryString.append("cc.GROUPID AS ALCODEHEADID,");
				queryString.append("a.BUDGET , v.CURRENCYID , v.BRANCHID , ");
				queryString.append("CASE WHEN SUM(v." + localMonth + ")= 0 THEN Round((SUM(v. " + queryMonth + " )/1),2) ");
				queryString.append(" ELSE Round((SUM(v." + queryMonth + " )/SUM(v." + localMonth + " )),4) END as Rate  ");
				queryString.append("FROM VW_CCOA v JOIN ALLOCATE_CODE a   ON v.COAID=a.COAID AND v.BUDGET=a.BUDGET ");
				queryString.append("JOIN COA cc   ON cc.ID=a.COAID   JOIN DEPTCODE d ON d.ID=a.DEPARTMENTID ");
				queryString.append("GROUP BY v.COAID,a.COAID,a.AMTPERCENT,a.BASEDON,d.DCODE,a.BUDGET,cc.GROUPID,cc.HEADID,v.CURRENCYID,v.BRANCHID )  ");
				queryString.append("P PIVOT  (SUM(BALANCE) FOR DCODE IN (" + dept + ")) AS PVT   where PVT.BUDGET=?1 ");
			} else {
				queryString.append("Select HACODE,Cur, Rate," + groupDept + " FROM(");
				queryString.append("SELECT (SELECT ACCODE FROM COA WHERE ID = ACODEID)AS ACCODE,");
				queryString.append("(SELECT ACNAME FROM COA WHERE ID=ACODEID)AS ACNAME,");
				queryString.append("(SELECT ACTYPE FROM COA WHERE ID=ACODEID)AS ACTYPE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACCODE FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACCODE FROM COA WHERE ID=ALCODEHEADID) END ) AS HACODE,");
				queryString.append("(CASE WHEN ALCODEHEADID IS NULL THEN (SELECT ACNAME FROM COA WHERE ID=ACODEID)");
				queryString.append("ELSE (SELECT ACNAME FROM COA WHERE ID=ALCODEHEADID) END ) AS HACNAME,");
				queryString.append("(SELECT CUR FROM CUR where cur = 'MMK') AS Cur,");
				queryString.append("RATE, TOTAL, " + strDept + " FROM ");
				queryString.append("(SELECT (SUM(v." + queryMonth + ")* a.AMTPERCENT)/a.BASEDON AS BALANCE,");
				queryString.append("d.DCODE,a.COAID as ACODEID,SUM(v." + queryMonth + " ) AS TOTAL ,");
				queryString.append("cc.GROUPID AS ALCODEHEADID,");
				queryString.append("a.BUDGET , v.BRANCHID , ");
				queryString.append("1 as Rate ");

				// queryString.append("CASE WHEN SUM(v." + localMonth + ")= 0
				// THEN Round((SUM(v. " + queryMonth + " )/1),2) ");
				// queryString.append(" ELSE Round((SUM(v." + queryMonth + "
				// )/SUM(v." + localMonth + " )),4) END as Rate ");
				queryString.append("FROM VW_CCOA v JOIN ALLOCATE_CODE a   ON v.COAID=a.COAID AND v.BUDGET=a.BUDGET ");
				queryString.append("JOIN COA cc   ON cc.ID=a.COAID   JOIN DEPTCODE d ON d.ID=a.DEPARTMENTID ");
				queryString.append("GROUP BY v.COAID,a.COAID,a.AMTPERCENT,a.BASEDON,d.DCODE,a.BUDGET,cc.GROUPID,cc.HEADID,v.BRANCHID )  ");
				queryString.append("P PIVOT  (SUM(BALANCE) FOR DCODE IN (" + dept + ")) AS PVT   where PVT.BUDGET=?1 ");
			}
			int index = 1;
			if (branch != null) {
				index++;
				queryString.append(" AND PVT.BRANCHID=?" + String.valueOf(index));
			}
			if (currency != null) {
				index++;
				queryString.append(" AND PVT.CURRENCYID=?" + String.valueOf(index));
			}
			queryString.append(" ) T Group By HACODE,Rate,Cur");
			Query q = em.createNativeQuery(queryString.toString());
			q.setParameter("1", budgetYear);
			index = 1;
			if (branch != null) {
				index++;
				q.setParameter(String.valueOf(index), branch.getId());
			}
			if (currency != null) {
				index++;
				q.setParameter(String.valueOf(index), currency.getId());
			}
			tempResults = q.getResultList();
			em.flush();
			logger.debug("findAllocateProcessByDept()method has been successfully finished");
			for (Object obj[] : tempResults) {

				String hacCode = obj[0].toString();
				String currencyCode = obj[1].toString();
				// String branchCode = obj[2].toString();
				BigDecimal rate = new BigDecimal(obj[2].toString());
				AllocateProcessByDeptDto allocateProcessByDeptDto = new AllocateProcessByDeptDto(hacCode, currencyCode, null, rate);

				int deptIndex = 0;
				for (int i = 3; i < obj.length; i++) {
					String dCode = departments.get(deptIndex).getdCode();
					BigDecimal amount = (BigDecimal) obj[i];
					allocateProcessByDeptDto.putToMap(dCode, amount);
					deptIndex++;
				}
				result.add(allocateProcessByDeptDto);
			}

		} catch (PersistenceException pe) {
			logger.error("findAllocateProcessByDept()method has been failed", pe);
			throw translate("Failed to find Allocate Process", pe);
		}
		return result;

	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateByDeptDto> getAcCodeByDept(String hacCode) {
		List<AllocateByDeptDto> result = new ArrayList<AllocateByDeptDto>();
		List<Object[]> tempResults = null;
		try {
			logger.debug("getAcCodeByDept()method has been started");
			StringBuffer query = new StringBuffer("SELECT c.ACCODE,d.DCODE,c.ACTYPE");
			query.append(" FROM CCOA cc INNER JOIN COA c ON c.id=cc.COAID");
			query.append(" LEFT JOIN DEPTCODE d ON d.ID=cc.DEPARTMENTID");
			query.append(" WHERE GROUPID=(SELECT ID FROM COA WHERE ACCODE='" + hacCode + "')");
			query.append(" AND ISNULL(d.DCODE,'') <> ''");
			query.append(" GROUP BY ACCODE,DCODE,ACTYPE");
			Query q = em.createNativeQuery(query.toString());
			tempResults = q.getResultList();
			em.flush();
			logger.debug("getAcCodeByDept()method has been successfully finished");
			for (Object obj[] : tempResults) {
				String acCode = obj[0].toString();
				String dCode = obj[1].toString();
				String acType = obj[2].toString();
				AllocateByDeptDto allocateDeptDto = new AllocateByDeptDto(acCode, dCode, acType);
				result.add(allocateDeptDto);
			}
		} catch (PersistenceException pe) {
			logger.error("getAcCodeByDept()method has been failed", pe);
			throw translate("Failed to find HeadCode", pe);
		}
		return result;

	}

}
