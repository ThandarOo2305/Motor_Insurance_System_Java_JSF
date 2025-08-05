package org.ace.accounting.system.chartaccount.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.dto.GenLedgerSummaryDTO;
import org.ace.accounting.dto.LiabilitiesACDto;
import org.ace.accounting.dto.YPDto;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICoaDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("CoaDAO")
public class CoaDAO extends BasicDAO implements ICoaDAO {

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ChartOfAccount> findAll() throws DAOException {
		List<ChartOfAccount> result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of ChartOfAccount", pe);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findAllCOAByAccountCodeType() throws DAOException {
		List<ChartOfAccount> result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findAllCOAByAccountCodeType");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of ChartOfAccount", pe);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> findAllAcCode() throws DAOException {
		List<String> result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findAllAcCode");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all acCodes of ChartOfAccount", pe);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findByAcCode(String acCode) throws DAOException {
		ChartOfAccount result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findByAcCode");
			q.setParameter("acCode", acCode);
			result = (ChartOfAccount) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find ChartOfAccount with acCode : " + acCode, pe);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findByAcCode(String acCode, AccountType acType) throws DAOException {
		ChartOfAccount result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findByAcCodeAndType");
			q.setParameter("acCode", acCode);
			q.setParameter("acType", acType);
			result = (ChartOfAccount) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find ChartOfAccount with acCode : " + acCode, pe);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findCoaByIE() throws DAOException {
		List<ChartOfAccount> result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findCoaByIE");
			result = q.getResultList();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find ChartOfAccount by Income and Expense : ", pe);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findByIbsbACode(String ibsbACode) throws DAOException {
		ChartOfAccount result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findByIbsbACode");
			q.setParameter("ibsbACode", ibsbACode);
			result = (ChartOfAccount) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find ChartOfAccount with ibsbACode : " + ibsbACode, pe);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findAllHeadAccount() throws DAOException {
		List<ChartOfAccount> result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findAllHeadCode");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find All Head Account ", pe);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findAllGroupAccount() throws DAOException {
		List<ChartOfAccount> result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findAllGroupCode");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find All Group Account ", pe);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findCoaByHeadId(String headId) throws DAOException {
		ChartOfAccount result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findByHeadACId");
			q.setParameter("headId", headId);
			result = (ChartOfAccount) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find ChartOfAccount with Account Head Id : " + headId, pe);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findCoaByGroupId(String groupId) throws DAOException {
		ChartOfAccount result = null;
		try {
			Query q = em.createNamedQuery("ChartOfAccount.findByGroupACId");
			q.setParameter("groupId", groupId);
			result = (ChartOfAccount) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find ChartOfAccount with Account Group Id : " + groupId, pe);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateGroupAccount(ChartOfAccount chartOfAccount, String groupId) throws DAOException {
		try {
			StringBuffer sf = new StringBuffer("UPDATE ChartOfAccount c Set c.groupId=:groupId WHERE c.id=:id");
			Query q = em.createQuery(sf.toString());
			q.setParameter("groupId", groupId);
			q.setParameter("id", chartOfAccount.getId());
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of ChartOfAccount", pe);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<YPDto> findDtosForYearlyPosting() throws DAOException {
		List<YPDto> result = new ArrayList<>();
		try {
			StringBuffer sf = new StringBuffer("SELECT NEW org.ace.accounting.dto.YPDto(coa.id, coa.acCode, coa.acName, coa.groupId, coa.headId) " + " FROM ChartOfAccount coa "
					+ " WHERE coa.acType = org.ace.accounting.system.chartaccount.AccountType.L "

					+ " AND coa.acCodeType=org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL " + " ORDER BY coa.acCode");
			Query q = em.createQuery(sf.toString());
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of ypdtos for yearly posting", pe);
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findAllCoaByCriteria(CoaDialogCriteriaDto dto) throws DAOException {
		List<ChartOfAccount> result = new ArrayList<>();
		try {
			StringBuffer sf = new StringBuffer(" SELECT c FROM ChartOfAccount c WHERE c.id IS NOT NULL ");
			if (dto.getAccountCodeType() != null) {
				sf.append(" AND c.acCodeType = :acCodeType ");
			}
			if (dto.getAccountTypes().size() > 0) {
				sf.append(" AND c.acType IN :acTypeList ");
			}
			sf.append(" ORDER BY c.acType,c.acCodeType,c.acCode ASC ");
			Query q = em.createQuery(sf.toString());

			if (dto.getAccountCodeType() != null) {
				q.setParameter("acCodeType", dto.getAccountCodeType());
			}
			if (dto.getAccountTypes().size() > 0) {
				q.setParameter("acTypeList", dto.getAccountTypes());
			}
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to findAllCoaByCriteria", pe);
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiabilitiesACDto> findLiabilitiesACDtos() throws DAOException {
		List<LiabilitiesACDto> result = new ArrayList<>();
		try {
			StringBuffer sf = new StringBuffer(" SELECT ACCODE,cc.department.dCode FROM COA c ");
			sf.append(" LEFT JOIN CCOA cc ON cc.coaid=c.ID ");
			sf.append(" LEFT JOIN DEPTCODE d ON cc.DEPARTMENTID=d.ID ");
			sf.append(" WHERE c.ACTYPE='L'  ");
			sf.append(" GROUP BY ACCODE,DCODE ");
			sf.append(" ORDER BY ACCODE ");
			Query q = em.createNativeQuery(sf.toString());
			List<Object[]> objList = q.getResultList();
			for (Object[] obj : objList) {
				result.add(new LiabilitiesACDto(obj[0].toString(), obj[1] == null ? null : obj[1].toString()));
			}
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to findLiabilitiesACDtos", pe);
		}

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<GenLedgerSummaryDTO> findGenLedgerSummaryCOAList(Date startDate, Date endDate, String openingBalanceColumn, String amountColumn) throws DAOException {
		String previousMonth = "";
		/* FOR PREVIOUS OPEINING BALANCE */
		Date monthStartDate = new Date();
		/*
		 * buffer.append(" SELECT C.ACCODE,C.ACNAME,C.ACTYPE,CC.M2,"); buffer.
		 * append(" 'CREDIT'=  CASE WHEN ( T.TRANTYPEID = 1 OR T.TRANTYPEID = 4 ) THEN ISNULL(SUM(T.HOMEAMOUNT),0.0) ELSE   0.0  END,"
		 * ); buffer.
		 * append(" 'DEBIT'=  CASE WHEN ( T.TRANTYPEID = 2 OR T.TRANTYPEID = 3 ) THEN ISNULL(SUM(T.HOMEAMOUNT),0.0) ELSE   0.0  END"
		 * );
		 * buffer.append(" FROM TLF T RIGHT JOIN CCOA CC ON T.CCOAID = CC.ID");
		 * buffer.append(" JOIN COA C ON CC.COAID = C.ID");
		 * buffer.append(" WHERE CAST(T.SETTLEMENTDATE AS DATE) >=" + startDate
		 * + ""); buffer.append(" AND CAST(T.SETTLEMENTDATE AS DATE) <=" +
		 * endDate + ""); buffer.
		 * append(" GROUP BY C.ACCODE,C.ACNAME,CC.M2,T.TRANTYPEID,C.ACTYPE");
		 */

		/* FOR CURRENT */

		try {
			/*
			 * StringBuffer buffer = new StringBuffer();
			 * buffer.append(" SELECT C.ACCODE,C.ACNAME,C.ACTYPE,"); buffer.
			 * append(" 'CREDIT'=  CASE WHEN ( T.TRANTYPEID = 1 OR T.TRANTYPEID = 4 ) THEN ISNULL(SUM(T.HOMEAMOUNT),0.0) ELSE   0.0  END,"
			 * ); buffer.
			 * append(" 'DEBIT'=  CASE WHEN ( T.TRANTYPEID = 2 OR T.TRANTYPEID = 3 ) THEN ISNULL(SUM(T.HOMEAMOUNT),0.0) ELSE   0.0  END"
			 * ); buffer.
			 * append(" FROM TLF T RIGHT JOIN CCOA CC ON T.CCOAID = CC.ID");
			 * buffer.append(" JOIN COA C ON CC.COAID = C.ID");
			 * buffer.append(" WHERE CAST(T.SETTLEMENTDATE AS DATE) >=" +
			 * startDate + "");
			 * buffer.append(" AND CAST(T.SETTLEMENTDATE AS DATE) <=" + endDate
			 * + "");
			 * buffer.append(" GROUP BY C.ACCODE,C.ACNAME,T.TRANTYPEID,C.ACTYPE"
			 * );
			 */
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT NEW " + GenLedgerSummaryDTO.class.getName());
			sql.append(" (C.acCode,C.acName,T.narration,C.acType,");
			sql.append(" T.ccoa." + openingBalanceColumn + ",CASE WHEN (T.tranType.status ='CCV' OR T.tranType.status ='TCV' ) THEN T." + amountColumn + " ELSE " + BigDecimal.ZERO
					+ " END, ");
			sql.append(" CASE WHEN (T.tranType.status ='CDV' OR T.tranType.status ='TDV' ) THEN T." + amountColumn + " ELSE " + BigDecimal.ZERO + " END,T.settlementDate)");
			sql.append(" FROM TLF T ");
			sql.append(" JOIN ChartOfAccount C ON C.id = T.ccoa.coa.id ");
			sql.append(" WHERE T.settlementDate >=:monthStartDate");
			sql.append(" AND T.settlementDate <=:endDate");
			sql.append(" GROUP BY C.acCode,C.acName,T.narration,C.acType,T.ccoa." + openingBalanceColumn + ", T.settlementDate,T.tranType.status,T." + amountColumn);

			TypedQuery<GenLedgerSummaryDTO> query = em.createQuery(sql.toString(), GenLedgerSummaryDTO.class);

			query.setParameter("monthStartDate", startDate);
			query.setParameter("endDate", endDate);

			return query.getResultList();
		} catch (NoResultException ne) {
			return new ArrayList<>();
		} catch (PersistenceException pe) {
			throw translate("Failed to find coa for general ledger report", pe);
		}
	}

	public static void main(String args[]) {
		LocalDate localDate = LocalDate.now();
		localDate = localDate.withDayOfMonth(1);
		Date monthStartDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		LocalDate endLocalDate = localDate.of(2019, 6, 10);
		Date endDate = Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String openingBalanceColumn = "hOBal";
		String amountColumn = "homeAmount";
		CoaDAO coa = new CoaDAO();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");
		EntityManager em = emf.createEntityManager();
		// List<GenLedgerSummaryDTO> resultList =
		// coa.findGenLedgerSummaryCOAList(monthStartDate, endDate,
		// openingBalanceColumn, amountColumn, em);
		em.close();
		emf.close();

	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findForIbsCode(String ibsCode) throws DAOException {
		List<ChartOfAccount> result = new ArrayList<>();
		try {
			StringBuffer bf = new StringBuffer();
			bf.append("SELECT c FROM ChartOfAccount c WHERE c.acCode like('" + ibsCode + "%') AND c.acCodeType = org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL");
			Query q = em.createQuery(bf.toString());
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of COA for IBS Closing", pe);
		}

		return result;
	}
}
