package org.ace.accounting.report.cleanCashReport.service;

import java.util.Map;

import javax.annotation.Resource;

import org.ace.accounting.report.ReportCriteria;
import org.ace.accounting.report.cleanCashReport.CleanCashReport;
import org.ace.accounting.report.cleanCashReport.persistence.interfaces.ICleanCashDAO;
import org.ace.accounting.report.cleanCashReport.service.interfaces.ICleanCashService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/***************************************************************************************
 * @Date 2016-04-05
 * @author PPA
 * @Version 1.0
 * @Purpose This class serves as Service Layer to show
 *          <code>CleanCashReport</code> Clean Cash Report process
 * 
 ***************************************************************************************/

@Service(value = "CleanCashService")
public class CleanCashService extends BaseService implements ICleanCashService {

	@Resource(name = "CleanCashDAO")
	private ICleanCashDAO cleanCahDAO;

	/**
	 * find CleanCashReport for the given criteria parameter.
	 * 
	 * @param ReportCriteria[Criteria
	 *            of user selection].
	 * 
	 * @return Map<String, CleanCashReport>[Map of cleanCashReport with account
	 *         code key and CleanCashReport value].
	 * 
	 * @throws SystemException.
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, CleanCashReport> findCleanCashReport(ReportCriteria criteria) {
		Map<String, CleanCashReport> resultMap = null;
		try {
			resultMap = cleanCahDAO.find(criteria);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Faield to find CleanCashReport Service.", e);
		}
		return resultMap;
	}
}
