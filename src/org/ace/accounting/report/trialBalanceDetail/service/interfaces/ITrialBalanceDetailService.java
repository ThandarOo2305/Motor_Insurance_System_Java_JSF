package org.ace.accounting.report.trialBalanceDetail.service.interfaces;

import java.util.List;

import org.ace.accounting.dto.TrialBalanceCriteriaDto;
import org.ace.accounting.dto.TrialBalanceReportDto;

public interface ITrialBalanceDetailService {

	public List<TrialBalanceReportDto> findTrialBalanceDetailList(TrialBalanceCriteriaDto dto);

	public List<TrialBalanceReportDto> findTrialBalanceDetailListByClone(TrialBalanceCriteriaDto dto);
}
