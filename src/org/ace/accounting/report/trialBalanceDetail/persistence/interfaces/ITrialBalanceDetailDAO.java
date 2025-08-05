package org.ace.accounting.report.trialBalanceDetail.persistence.interfaces;

import java.util.List;

import org.ace.accounting.dto.TrialBalanceCriteriaDto;
import org.ace.accounting.dto.TrialBalanceReportDto;
import org.ace.java.component.persistence.exception.DAOException;

public interface ITrialBalanceDetailDAO {

	public List<TrialBalanceReportDto> findTrialBalanceDetailList(TrialBalanceCriteriaDto dto) throws DAOException;

	public List<TrialBalanceReportDto> findTrialBalanceDetailListByClone(TrialBalanceCriteriaDto dto) throws DAOException;
}
