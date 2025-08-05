package org.ace.accounting.report.incomeandexpenditure.presistence.interfaces;

import java.util.List;

import org.ace.accounting.report.incomeandexpenditure.IncomeAndExpenditureDto;
import org.ace.accounting.report.incomeandexpenditure.IncomeExpenseCriteria;
import org.ace.java.component.persistence.exception.DAOException;

public interface IIncomeAndExpenditureDAO {

	public List<IncomeAndExpenditureDto> find(IncomeExpenseCriteria criteria) throws DAOException;
}
