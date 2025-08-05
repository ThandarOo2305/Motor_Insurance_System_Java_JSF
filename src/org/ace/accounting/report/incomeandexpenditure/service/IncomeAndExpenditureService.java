package org.ace.accounting.report.incomeandexpenditure.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.report.incomeandexpenditure.IncomeAndExpenditureDto;
import org.ace.accounting.report.incomeandexpenditure.IncomeExpenseCriteria;
import org.ace.accounting.report.incomeandexpenditure.presistence.interfaces.IIncomeAndExpenditureDAO;
import org.ace.accounting.report.incomeandexpenditure.service.interfaces.IIncomeAndExpenditureService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/***************************************************************************************
 * @Date 2016-04-25
 * @author PPA
 * @Version 1.0
 * @Purpose This class serves as Service Layer to show
 *          <code>IncomeAndExpenditure</code> Income and Expenditure Report
 *          process
 * 
 ***************************************************************************************/

@Service(value = "IncomeAndExpenditureService")
public class IncomeAndExpenditureService extends BaseService implements IIncomeAndExpenditureService {

	@Resource(name = "IncomeAndExpenditureDAO")
	private IIncomeAndExpenditureDAO incomeAndExpenditureDAO;

	/**
	 * find Income and Expenditure Report for the given criteria parameter.
	 * 
	 * @param IncomeExpenseCriteria[Criteria
	 *            of user selection].
	 * 
	 * @return List<IncomeAndExpenditure>.
	 * 
	 * @throws SystemException.
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IncomeAndExpenditureDto> findIncomeExpense(IncomeExpenseCriteria criteria) {
		List<IncomeAndExpenditureDto> result = null;
		try {
			result = incomeAndExpenditureDAO.find(criteria);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Faield to find CleanCashReport Service.", e);
		}
		return result;
	}
}
