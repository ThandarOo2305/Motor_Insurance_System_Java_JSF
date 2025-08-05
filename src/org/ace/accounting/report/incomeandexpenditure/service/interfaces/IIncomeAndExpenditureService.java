package org.ace.accounting.report.incomeandexpenditure.service.interfaces;

import java.util.List;

import org.ace.accounting.report.incomeandexpenditure.IncomeAndExpenditureDto;
import org.ace.accounting.report.incomeandexpenditure.IncomeExpenseCriteria;

public interface IIncomeAndExpenditureService {

	/**
	 * Interface to find IncomeAndExpenditure Report for the given criteria
	 * parameter.
	 * 
	 * @param IncomeExpenseCriteria[Criteria
	 *            of user selection].
	 * 
	 * @return List<IncomeAndExpenditure>.
	 * 
	 * @throws SystemException.
	 * 
	 */
	public List<IncomeAndExpenditureDto> findIncomeExpense(IncomeExpenseCriteria criteria);
}
