package org.ace.accounting.system.tlfhist.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.tlfhist.TLFHIST;
import org.ace.accounting.system.tlfhist.persistence.interfaces.ITLFHISTDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("TLFHISTDAO")
public class TLFHISTDAO extends BasicDAO implements ITLFHISTDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TLFHIST> findAll() throws DAOException {
		List<TLFHIST> result = null;
		try {
			Query q = em.createNamedQuery("TLFHIST.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of TLFHIST", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TLF> findVoucherListByReverseZero(String voucherNo) throws DAOException {
		List<TLF> result = null;
		try {
			Query q = em.createNamedQuery("TLFHIST.findVoucherListByVoucherNo");
			q.setParameter("voucherNo", voucherNo);
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find VoucherListByReverseZero", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountLedgerDto> findDebitCreditBy(StringBuffer sf, ChartOfAccount coa, Date sDate) throws DAOException {
		List<AccountLedgerDto> result = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(sDate);

		int month = (cal.get(Calendar.MONTH)) == 0 ? 1 : (cal.get(Calendar.MONTH));
		month = month + 1;
		Date startDate = DateUtils.formatStringToDate("1" + "-" + month + "-" + (cal.get(Calendar.YEAR)));
		Date endDate = DateUtils.minusDays(sDate, 1);
		try {
			Query query = em.createQuery(sf.toString());
			query.setParameter("coa", coa);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			result = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find DebitCredit By ChartOfAccount", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountLedgerDto> findDebitCreditBy(StringBuffer sf, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException {
		List<AccountLedgerDto> result = null;
		try {
			Query query = em.createQuery(sf.toString());
			query.setParameter("coa", coa);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			result = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find DebitCredit By ChartOfAccount", pe);
		}
		return result;
	}

}
