package org.ace.accounting.report.cleanCashReport.persistence.interfaces;

import java.util.Map;

import org.ace.accounting.report.ReportCriteria;
import org.ace.accounting.report.cleanCashReport.CleanCashReport;
import org.ace.java.component.persistence.exception.DAOException;

public interface ICleanCashDAO {

	/**
	 * Interface to retrieve CleanCashReport from database for the given
	 * criteria parameter.
	 * 
	 * @param ReportCriteria[Criteria
	 *            of user selection].
	 * 
	 * @return Map<String, CleanCashReport>[Map of cleanCashReport with acode
	 *         key and CleanCashReport value].
	 * 
	 * @throws DAOException.
	 * 
	 */
	public Map<String, CleanCashReport> find(ReportCriteria criteria) throws DAOException;
}
