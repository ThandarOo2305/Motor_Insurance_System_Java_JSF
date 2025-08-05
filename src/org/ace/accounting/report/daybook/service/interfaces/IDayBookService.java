/**
 * 
 */
package org.ace.accounting.report.daybook.service.interfaces;

import java.util.List;

import org.ace.accounting.dto.DayBookDto;
import org.ace.accounting.dto.DayBookReportDto;
import org.ace.accounting.dto.DayBookReportDto1;

/**
 * @author Aung
 *
 */
public interface IDayBookService {
	public List<DayBookReportDto> findDayBookList(DayBookDto dto);

	public List<DayBookReportDto1> findDayBookListWithGrandTotal(DayBookDto dto);

	public DayBookReportDto getGrandTotal(List<DayBookReportDto> detailList);
}
