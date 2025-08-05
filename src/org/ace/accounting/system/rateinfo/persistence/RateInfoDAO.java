package org.ace.accounting.system.rateinfo.persistence;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.rateinfo.RateInfo;
import org.ace.accounting.system.rateinfo.RateType;
import org.ace.accounting.system.rateinfo.persistence.interfaces.IRateInfoDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("RateInfoDAO")
public class RateInfoDAO extends BasicDAO implements IRateInfoDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RateInfo> findAll() throws DAOException {
		List<RateInfo> result = null;
		try {
			Query q = em.createNamedQuery("RateInfo.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of RateInfo", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public double findExchangeRateBy(Currency reqCur, RateType reqRateType, Date rDate) throws DAOException {
		double exchangeRate = 0.0;
		try {
			StringBuffer querry = new StringBuffer();
			querry.append("SELECT CASE WHEN");
			querry.append("(0=(SELECT COUNT(r1.id) FROM RateInfo r1 where r1.lastModify=1 and r1.rDate=r.rDate and r1.rateType=r.rateType and r1.currency.id=r.currency.id))");
			querry.append(" then (select r2.exchangeRate from RateInfo r2 where r2.lastModify=0 and r2.id=r.id) else");
			querry.append("(select r3.exchangeRate from RateInfo r3 where r3.lastModify=1 and r3.rDate=r.rDate and r3.rateType=r.rateType and r3.currency.id=r.currency.id)");
			querry.append(" end from RateInfo r where r.rDate=:rDate and r.rateType=:reqRateType");
			querry.append(" and r.currency.id=:currency  and r.basicEntity.createdDate=");
			querry.append("(select max(r4.basicEntity.createdDate) from RateInfo r4 where r4.rDate=:rDate and r4.rateType=:reqRateType and r4.currency.id=:currency)");
			Query q = em.createQuery(querry.toString());
			q.setParameter("currency", reqCur.getId());
			q.setParameter("reqRateType", reqRateType);
			q.setParameter("rDate", DateUtils.resetStartDate(rDate));
			BigDecimal rate = (BigDecimal) q.getSingleResult();
			exchangeRate = rate.doubleValue();
			em.flush();
		} catch (NoResultException re) {
			return 0.0;
		} catch (PersistenceException pe) {
			throw translate("Failed to findRateInfoByCurRTypeRDate", pe);
		}

		return exchangeRate;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateLastModifyBy(Currency cur, RateType rateType) throws DAOException {
		try {
			Query q = em.createNamedQuery("RateInfo.updateLastModifyBy");
			q.setParameter("currency", cur);
			q.setParameter("rateType", rateType);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Faile to update LastModify 0 By Daily Currency Rate", pe);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RateInfo> findRateInfoByCurrencyID(String currencyID) throws DAOException {
		List<RateInfo> list = null;
		try {
			Query q = em.createNamedQuery("RateInfo.findRateInfoByCurrencyID");
			q.setParameter("currency", currencyID);
			list = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of RateInfo", pe);
		}
		return list;
	}
}
