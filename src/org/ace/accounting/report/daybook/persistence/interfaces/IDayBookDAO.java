/**
 * 
 */
package org.ace.accounting.report.daybook.persistence.interfaces;

import java.util.List;

import org.ace.accounting.dto.DayBookDto;
import org.ace.accounting.dto.DayBookReportDto;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.java.component.persistence.exception.DAOException;


public interface IDayBookDAO {

	public List<DayBookReportDto> findDayBookList(StringBuffer sf, DayBookDto dto,ChartOfAccount chartOfAccount) throws DAOException;

}
