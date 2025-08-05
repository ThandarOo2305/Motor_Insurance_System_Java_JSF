package org.ace.accounting.system.chartaccount.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.CCOADialogDTO;
import org.ace.accounting.dto.CcoaDto;
import org.ace.accounting.dto.EnquiryLedgerDto;
import org.ace.accounting.dto.MonthlyBudgetDto;
import org.ace.accounting.dto.MonthlyBudgetHomeCurDto;
import org.ace.accounting.dto.ObalCriteriaDto;
import org.ace.accounting.dto.ObalDto;
import org.ace.accounting.dto.YearlyBudgetDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.AccountCodeType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICcoaDAO;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("CcoaDAO")
public class CcoaDAO extends BasicDAO implements ICcoaDAO {

	@Resource(name = "UserProcessService")
	private IUserProcessService userProcessService;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataRepService;

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findAll() throws DAOException {
		List<CurrencyChartOfAccount> list = null;
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.findAll");
			list = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CurrencyChartOfAccount", pe);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAByCOAid(String coaId) throws DAOException {
		List<CurrencyChartOfAccount> list = null;
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.findCCOAByCOAid");
			q.setParameter("id", coaId);
			list = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount by coaid", pe);
		}
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CurrencyChartOfAccount findSpecificCCOA(String coaId, String currencyId, String branchId, String optionalDepartmentId) throws DAOException {
		CurrencyChartOfAccount result = null;
		try {
			StringBuffer queryString = new StringBuffer(
					"SELECT c FROM CurrencyChartOfAccount c WHERE c.coa.id=:coaId AND c.currency.id=:currencyId " + " AND c.branch.id=:branchId ");
			if (optionalDepartmentId != null) {
				queryString.append(" AND c.department.id=:optionalDepartmentId ");
			}
			Query q = em.createQuery(queryString.toString());
			q.setParameter("coaId", coaId);
			q.setParameter("currencyId", currencyId);
			q.setParameter("branchId", branchId);
			if (optionalDepartmentId != null) {
				q.setParameter("optionalDepartmentId", optionalDepartmentId);
			}

			result = (CurrencyChartOfAccount) q.getSingleResult();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find specific CurrencyChartOfAccount", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCOAByBranchID(String branchID) throws DAOException {
		List<CurrencyChartOfAccount> list = null;
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.findCOAByBranchID");
			q.setParameter("branchId", branchID);
			list = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CurrencyChartOfAccount", pe);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> findAllBudgetYear() throws DAOException {
		List<String> list = null;
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.findAllBudget");
			list = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CurrencyChartOfAccount", pe);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<YearlyBudgetDto> findAllYearlyBudget(YearlyBudgetDto dto) throws DAOException {
		List<YearlyBudgetDto> result = null;
		try {
			StringBuffer sf = new StringBuffer(
					"SELECT new org.ace.accounting.dto.YearlyBudgetDto(c.id, c.coa.acCode, c.coa.acName, c.currency.currencyCode, d.dCode, c.branch.branchCode, c.bF)");
			sf.append(" FROM CurrencyChartOfAccount c LEFT OUTER JOIN c.department d WHERE c.coa.id!=null");
			if (!(dto.getCoaId() == null || dto.getCoaId().isEmpty())) {
				sf.append(" AND c.coa.id=:coaId");
			}
			if (!(dto.getBranchId() == null || dto.getBranchId().isEmpty())) {
				sf.append(" AND c.branch.id=:branchId ");
			}
			if (!(dto.getCurId() == null || dto.getCurId().isEmpty())) {
				sf.append(" AND c.currency.id=:currencyId");
			}
			if (!(dto.getDepartmentId() == null || dto.getDepartmentId().isEmpty())) {
				sf.append(" AND c.department.id=:departmentId");
			}
			sf.append(" ORDER BY  c.coa.acCode, c.currency.currencyCode, d.dCode, c.branch.branchCode");

			Query q = em.createQuery(sf.toString());

			if (!(dto.getCoaId() == null || dto.getCoaId().isEmpty())) {
				q.setParameter("coaId", dto.getCoaId());
			}
			if (!(dto.getBranchId() == null || dto.getBranchId().isEmpty())) {
				q.setParameter("branchId", dto.getBranchId());
			}
			if (!(dto.getCurId() == null || dto.getCurId().isEmpty())) {
				q.setParameter("currencyId", dto.getCurId());
			}
			if (!(dto.getDepartmentId() == null || dto.getDepartmentId().isEmpty())) {
				q.setParameter("departmentId", dto.getDepartmentId());
			}
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CurrencyChartOfAccount", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateYearlyBudget(YearlyBudgetDto dto) throws DAOException {
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.updateBudgetYearByID");
			q.setParameter("id", dto.getCcoaId());
			q.setParameter("bF", dto.getbF());
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CurrencyChartOfAccount", pe);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<CcoaDto> findAllCcoaDtos() throws DAOException {
		List<CcoaDto> result = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT NEW " + CcoaDto.class.getName() + "(c.id, c.coa.acCode, c.acName, c.coa.acType, d.dCode, c.currency.currencyCode, c.branch.branchCode) ");
			buffer.append("FROM CurrencyChartOfAccount c LEFT OUTER JOIN  c.department d ");
			User user = userProcessService.getLoginUser();
			if (!user.isAdmin())
				buffer.append(" WHERE c.branch.id = :branchId");

			buffer.append(" ORDER BY c.coa.acCode ASC ");
			Query query = em.createQuery(buffer.toString());
			if (!user.isAdmin())
				query.setParameter("branchId", user.getBranch().getId());

			result = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CcoaDtos", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ObalDto> findObal(ObalCriteriaDto dto) throws DAOException {
		List<ObalDto> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(
					"SELECT NEW org.ace.accounting.dto.ObalDto(c.coa.acCode,c.acName,c.oBal,c.hOBal,c.coa.acType,c.branch,c.department,c.currency,c.id,c.coa.headId,c.coa.groupId) "
							+ "FROM CurrencyChartOfAccount c WHERE c.coa.acType IN (org.ace.accounting.system.chartaccount.AccountType.A,org.ace.accounting.system.chartaccount.AccountType.L) "
							+ "AND c.coa.acCodeType = org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL ");

			if (dto.getBranchId() != null) {
				queryString.append(" AND c.branch.id=:branchId  ");
			}

			if (dto.getDepartmentId() != null) {
				queryString.append(" AND c.department.id=:departmentId  ");
			}

			if (dto.getCurId() != null && !dto.getCurId().isEmpty()) {
				queryString.append(" AND c.currency.id=:curId  ");
			}

			queryString.append(" ORDER BY c.coa.acType ");

			Query q = em.createQuery(queryString.toString());

			if (dto.getBranchId() != null) {
				q.setParameter("branchId", dto.getBranchId());
			}

			if (dto.getDepartmentId() != null) {
				q.setParameter("departmentId", dto.getDepartmentId());
			}

			if (dto.getCurId() != null && !dto.getCurId().isEmpty()) {
				q.setParameter("curId", dto.getCurId());
			}

			result = q.getResultList();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find Opening Balance ", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findBudgetFigure(String branchId) throws DAOException {
		List<CurrencyChartOfAccount> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(
					"SELECT c FROM CurrencyChartOfAccount c WHERE c.coa.acCodeType != org.ace.accounting.system.chartaccount.AccountCodeType.HEAD AND c.coa.acCodeType != org.ace.accounting.system.chartaccount.AccountCodeType.GROUP ");

			if (branchId != null) {
				queryString.append(" AND c.branch.id=:branchId  ");
			}

			queryString.append(" ORDER BY c.coa.acType ");

			Query q = em.createQuery(queryString.toString());

			if (branchId != null) {
				q.setParameter("branchId", branchId);
			}
			result = q.getResultList();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find Budget Figure with branchId : " + branchId, pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCcoaByObalDtos(List<ObalDto> dtoList) throws DAOException {
		try {
			for (ObalDto dto : dtoList) {
				if (dto.isUpdated()) {
					if (dto.getHeadId() != null) {
						CurrencyChartOfAccount headCcoa = findCCOAByIdAndBranch(dto.getHeadId(), dto.getBranch(), dto.getCurrency());
						headCcoa.setoBal(headCcoa.getoBal().add(dto.getoBal()));
						headCcoa.sethOBal(headCcoa.gethOBal().add(dto.getHoBal()));
						ccoaDataRepService.update(headCcoa);
					}

					if (dto.getGroupId() != null) {
						CurrencyChartOfAccount groupCcoa = findCCOAByIdAndBranch(dto.getGroupId(), dto.getBranch(), dto.getCurrency());
						groupCcoa.setoBal(groupCcoa.getoBal().add(dto.getoBal()));
						groupCcoa.sethOBal(groupCcoa.gethOBal().add(dto.getHoBal()));
						ccoaDataRepService.update(groupCcoa);
					}

					CurrencyChartOfAccount ccoa = ccoaDataRepService.findById(CurrencyChartOfAccount.class, dto.getCcoaId());
					ccoa.setoBal(dto.getoBal());
					ccoa.sethOBal(dto.getHoBal());
					ccoaDataRepService.update(ccoa);
				}
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to update opening balance of ccoa by dto", pe);
		}
	}

	/**
	 * Enquiry Account Ledger Information
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<EnquiryLedgerDto> findAllEnquiryLedger(EnquiryLedgerDto dto) throws DAOException {
		List<EnquiryLedgerDto> results = null;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int month = cal.get(Calendar.MONTH) + 2;
			int budgetMonth = month - BusinessUtil.getBudSmth();
			budgetMonth = budgetMonth < 0 ? budgetMonth + 12 : budgetMonth;
			String paramMonth = (BusinessUtil.getMonthStatus(budgetMonth)).toLowerCase();
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT NEW " + EnquiryLedgerDto.class.getName() + "(c.id, c.coa.acCode, c.acName, c.currency.currencyCode, c.monthlyRate." + paramMonth);
			buffer.append(", c.branch.branchCode) FROM CurrencyChartOfAccount c WHERE c.id  IS NOT NULL ");
			if (dto.getCurid() != null && !dto.getCurid().isEmpty())
				buffer.append(" AND c.currency.id = :currencyId");

			if (dto.getCoaid() != null)
				buffer.append(" AND c.coa.id = :coaId");

			if (dto.getBranchid() != null && !dto.getBranchid().isEmpty())
				buffer.append(" AND c.branch.id = :branchId  ");

			buffer.append(" ORDER BY c.coa.acCode, c.currency.currencyCode, c.branch.branchCode ");
			Query query = em.createQuery(buffer.toString());
			if (dto.getCurid() != null && !dto.getCurid().isEmpty())
				query.setParameter("currencyId", dto.getCurid());

			if (dto.getCoaid() != null)
				query.setParameter("coaId", dto.getCoaid());

			if (dto.getBranchid() != null && !dto.getBranchid().isEmpty())
				query.setParameter("branchId", dto.getBranchid());

			results = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount", pe);
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MonthlyBudgetDto> findAllMonthlyBudget(MonthlyBudgetDto dto) throws DAOException {
		List<MonthlyBudgetDto> list = null;
		try {
			StringBuffer queryString = new StringBuffer("SELECT NEW org.ace.accounting.dto.MonthlyBudgetDto(c.coa.acCode,d.dCode,"
					+ "c.currency.currencyCode,c.bfRate,c.id,c.branch.branchCode) FROM CurrencyChartOfAccount c LEFT OUTER JOIN c.department d "
					+ " WHERE c.coa.acCodeType=org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL");
			if (dto.getCurid() != null && !dto.getCurid().isEmpty()) {
				queryString.append(" AND c.currency.id=:currencyId");
			}
			if (dto.getCoaid() != null) {
				queryString.append(" AND c.coa.id=:coaId");
			}
			if (dto.getBranchid() != null) {
				queryString.append(" AND c.branch.id=:branchId  ");
			}

			if (dto.getDepartmentid() != null) {
				queryString.append(" AND d.id=:departmentId");
			}

			queryString.append(" ORDER BY c.coa.acType,c.coa.acCode,c.currency.currencyCode,c.branch.branchCode,d.dCode ");

			Query query = em.createQuery(queryString.toString());
			if (dto.getCurid() != null && !dto.getCurid().isEmpty()) {
				query.setParameter("currencyId", dto.getCurid());
			}
			if (dto.getCoaid() != null) {
				query.setParameter("coaId", dto.getCoaid());
			}
			if (dto.getBranchid() != null) {
				query.setParameter("branchId", dto.getBranchid());
			}

			if (dto.getDepartmentid() != null) {
				query.setParameter("departmentId", dto.getDepartmentid());
			}

			list = query.getResultList();

			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount", pe);
		}

		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMonthlyBudget(List<MonthlyBudgetDto> dtoList) throws DAOException {
		try {
			// TODO query here current dtoList size = 3820
			for (MonthlyBudgetDto dto : dtoList) {
				if (dto.isEdit()) {
					CurrencyChartOfAccount ccoa = ccoaDataRepService.findById(CurrencyChartOfAccount.class, dto.getCcoaid());
					ccoa.setBfRate(dto.getBfRate());
					ccoaDataRepService.update(ccoa);
				}
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to update CurrencyChartOfAccount", pe);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MonthlyBudgetHomeCurDto> findAllMonthlyBudgetHomeCur(MonthlyBudgetHomeCurDto dto) throws DAOException {
		List<MonthlyBudgetHomeCurDto> list = null;
		try {
			StringBuffer queryString = new StringBuffer("SELECT NEW org.ace.accounting.dto.MonthlyBudgetHomeCurDto(c.coa.acCode,d.dCode,"
					+ "c.currency.currencyCode,c.bfsrcRate,c.id,c.branch.branchCode) FROM CurrencyChartOfAccount c LEFT OUTER JOIN c.department d "
					+ " WHERE c.coa.acCodeType=org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL ");

			if (dto.getCurid() != null && !dto.getCurid().isEmpty()) {
				queryString.append(" AND c.currency.id=:currencyId");
			}
			if (dto.getCoaid() != null) {
				queryString.append(" AND c.coa.id=:coaId");
			}
			if (dto.getBranchid() != null) {
				queryString.append(" AND c.branch.id=:branchId  ");
			}

			if (dto.getDepartmentid() != null) {
				queryString.append(" AND d.id=:departmentId");
			}
			queryString.append(" ORDER BY c.coa.acType,c.coa.acCode,c.currency.currencyCode,c.branch.branchCode,d.dCode ");

			Query query = em.createQuery(queryString.toString());

			if (dto.getCurid() != null && !dto.getCurid().isEmpty()) {
				query.setParameter("currencyId", dto.getCurid());
			}
			if (dto.getCoaid() != null) {
				query.setParameter("coaId", dto.getCoaid());
			}
			if (dto.getBranchid() != null) {
				query.setParameter("branchId", dto.getBranchid());
			}

			if (dto.getDepartmentid() != null) {
				query.setParameter("departmentId", dto.getDepartmentid());
			}

			list = query.getResultList();

			em.flush();

		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount", pe);
		}
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMonthlyBudgetHomeCur(List<MonthlyBudgetHomeCurDto> dtoList) throws DAOException {
		try {
			for (MonthlyBudgetHomeCurDto dto : dtoList) {
				if (dto.isEdit()) {
					CurrencyChartOfAccount ccoa = ccoaDataRepService.findById(CurrencyChartOfAccount.class, dto.getCcoaid());
					ccoa.setBfsrcRate(dto.getBfsrcRate());
					ccoaDataRepService.update(ccoa);
				}
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to update Monthly Budget Home Currency.", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal finddblBalance(StringBuffer sf, ChartOfAccount coa, String budgetYear, Currency currency, Branch branch) throws DAOException {
		BigDecimal result = null;
		try {
			Query query = em.createQuery(sf.toString());
			query.setParameter("coa", coa);
			query.setParameter("budgetYear", budgetYear);
			if (currency != null) {
				query.setParameter("currency", currency);
			}
			if (branch != null) {
				query.setParameter("branch", branch);
			}
			result = (BigDecimal) query.getSingleResult();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount", pe);
		}
		return (result == null) ? BigDecimal.ZERO : result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CurrencyChartOfAccount findCCOAByIdAndBranch(String coaId, Branch branch, Currency currency) throws DAOException {
		CurrencyChartOfAccount result = null;
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.findCCOAByIdAndBranch");
			q.setParameter("coaId", coaId);
			q.setParameter("branch", branch);
			q.setParameter("currency", currency);
			result = (CurrencyChartOfAccount) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount with coaId " + coaId, pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CCOADialogDTO> findAllCCOADialogDTO(Currency currency, Branch branch) throws DAOException {
		List<CCOADialogDTO> resultList = null;
		try {
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append("SELECT New org.ace.accounting.dto.CCOADialogDTO(c.id, c.coa.acCode, c.coa.acName, ");
			queryBuffer.append("c.coa.acType, d.dCode, c.currency.currencyCode, c.branch.name ) ");
			queryBuffer.append("FROM CurrencyChartOfAccount c LEFT OUTER JOIN c.department d ");
			queryBuffer.append("WHERE c.coa.acCodeType NOT IN :codeTypeList ");
			if (branch != null) {
				queryBuffer.append("AND c.branch.id = :branchId ");
			}

			if (currency != null) {
				queryBuffer.append("AND c.currency.id = :currencyId ");
			}

			queryBuffer.append("ORDER BY c.coa.acCode ASC ");
			Query query = em.createQuery(queryBuffer.toString());
			if (branch != null) {
				query.setParameter("branchId", branch.getId());
			}

			if (currency != null) {
				query.setParameter("currencyId", currency.getId());
			}

			query.setParameter("codeTypeList", Arrays.asList(AccountCodeType.HEAD, AccountCodeType.GROUP));
			resultList = query.getResultList();

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By Branch ID.", e);
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCOAByCurrencyID(String currencyID) throws DAOException {
		List<CurrencyChartOfAccount> list = null;
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.findCCOAByCurrencyID");
			q.setParameter("curid", currencyID);
			list = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CurrencyChartOfAccount", pe);
		}
		return list;
	}

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

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAByBranchID(String branchID) throws DAOException {
		List<CurrencyChartOfAccount> list = null;
		try {
			Query q = em.createNamedQuery("CurrencyChartOfAccount.findCCOAByBranchID");
			q.setParameter("branchid", branchID);
			list = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of CurrencyChartOfAccount", pe);
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAforBranchClosing(String branchId) throws DAOException {
		try {
			// branchId = "BANCH00000000000000129032013";
			Date budgetStartDate = BusinessUtil.getBudgetStartDate();
			Date budgetEndDate = BusinessUtil.getBudgetEndDate();
			int month = (DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false)) + 1;
			StringBuffer bf = new StringBuffer();
			bf.append(
					// "SELECT c FROM CurrencyChartOfAccount c WHERE c.branch.id
					// =:branchId AND c.monthlyRate.m12 <> 0 AND c.coa.acType
					// IN(org.ace.accounting.system.chartaccount.AccountType.I,org.ace.accounting.system.chartaccount.AccountType.E)
					// AND c.coa.acCodeType =
					// org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL
					// ");
					"SELECT c FROM CurrencyChartOfAccount c WHERE c.branch.id =:branchId AND c.msrcRate.msrc" + month
							+ "<> 0 AND c.coa.acType IN(org.ace.accounting.system.chartaccount.AccountType.I,org.ace.accounting.system.chartaccount.AccountType.E) AND c.coa.acCodeType = org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL ");

			TypedQuery<CurrencyChartOfAccount> query = em.createQuery(bf.toString(), CurrencyChartOfAccount.class);
			query.setParameter("branchId", branchId);
			List<CurrencyChartOfAccount> resultList = query.getResultList();
			return resultList;
		} catch (NoResultException ne) {
			return new ArrayList<>();
		} catch (PersistenceException e) {
			throw translate("Fail to find CCOA ", e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAByCOAIDAndBranchId(String coaId, String branchId) throws DAOException {
		try {
			TypedQuery<CurrencyChartOfAccount> q = em.createQuery("SELECT c FROM CurrencyChartOfAccount c where c.coa.id =:coaId and c.branch.id=:branchId",
					CurrencyChartOfAccount.class);
			q.setParameter("coaId", coaId);
			q.setParameter("branchId", branchId);
			return q.getResultList();
		} catch (NoResultException pe) {
			return new ArrayList<>();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount with coaId " + coaId, pe);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateByBranch(String acName, String acCode, String branchCode, Department departmentId) throws DAOException {
		try {
			TypedQuery<CurrencyChartOfAccount> query = em.createNamedQuery("CurrencyChartOfAccount.updateByBranchId", CurrencyChartOfAccount.class);
			query.setParameter("acName", acName);
			query.setParameter("acCode", acCode);
			query.setParameter("branchCode", branchCode);
			query.setParameter("departmentId", departmentId);
			query.executeUpdate();
			em.flush();
		} catch (PersistenceException e) {
			throw translate("Fail to update CCOA By Branch!!", e);
		}
	}

	// Accumulated surplus or deficit closing for HO
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAforHOClosing(String branchId) throws DAOException {
		try {
			// branchId = "BANCH00000000000000129032013";
			Date budgetStartDate = BusinessUtil.getBudgetStartDate();
			Date budgetEndDate = BusinessUtil.getBudgetEndDate();
			int month = (DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false)) + 1;
			StringBuffer bf = new StringBuffer();
			bf.append("SELECT c FROM CurrencyChartOfAccount c WHERE c.branch.id =:branchId AND c.msrcRate.msrc" + month
					+ "<> 0 AND c.coa.acType IN(org.ace.accounting.system.chartaccount.AccountType.I,org.ace.accounting.system.chartaccount.AccountType.E) AND c.coa.acCodeType = org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL ");

			TypedQuery<CurrencyChartOfAccount> query = em.createQuery(bf.toString(), CurrencyChartOfAccount.class);
			query.setParameter("branchId", branchId);
			List<CurrencyChartOfAccount> resultList = query.getResultList();
			return resultList;
		} catch (NoResultException ne) {
			return new ArrayList<>();
		} catch (PersistenceException e) {
			throw translate("Fail to find CCOA ", e);
		}

	}

	// Accumulated surplus or deficit
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAByCOAIDAndBranchIdForHOClosing(String coaId, String branchId) throws DAOException {
		try {
			TypedQuery<CurrencyChartOfAccount> q = em.createQuery("SELECT c FROM CurrencyChartOfAccount c where c.coa.id =:coaId and c.branch.id=:branchId",
					CurrencyChartOfAccount.class);
			q.setParameter("coaId", coaId);
			q.setParameter("branchId", branchId);
			return q.getResultList();
		} catch (NoResultException pe) {
			return new ArrayList<>();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount with coaId " + coaId, pe);
		}
	}

}
