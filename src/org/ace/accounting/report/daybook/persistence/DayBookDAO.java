/**
 * 
 */
package org.ace.accounting.report.daybook.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.DayBookDto;
import org.ace.accounting.dto.DayBookReportDto;
import org.ace.accounting.report.daybook.persistence.interfaces.IDayBookDAO;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aung
 *
 */
@Repository("DayBookDAO")
public class DayBookDAO extends BasicDAO implements IDayBookDAO {

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<DayBookReportDto> findDayBookList(StringBuffer sf, DayBookDto dto,ChartOfAccount ccoa) throws DAOException {
		List<DayBookReportDto> result = null;
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("cashAccount", ccoa);
			q.setParameter("requiredDate",DateUtils.resetStartDate( dto.getRequiredDate()));
			if (dto.getCurrency() != null) {
				q.setParameter("currency", dto.getCurrency());
			}
			if (dto.getBranch() != null) {
				q.setParameter("branch", dto.getBranch());
			}
			result = q.getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to findDayBookList", pe);
		}
		return result;
	}


}
