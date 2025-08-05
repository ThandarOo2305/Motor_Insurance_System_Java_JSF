package org.ace.accounting.common.service;

import javax.annotation.Resource;

import org.ace.accounting.common.interfaces.ITranValiDAO;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.springframework.stereotype.Service;

@Service(value = "AccountCodeValidator")
public class AccountCodeValidator implements IDataValidator<ChartOfAccount> {

	@Resource(name = "CoaService")
	private ICoaService coaService;

	@Resource(name = "TranValiDAO")
	private ITranValiDAO tranValiDAO;

	@Override
	public ValidationResult validate(ChartOfAccount coa, boolean transaction) {

		ValidationResult result = new ValidationResult();

		String formId = "coaEntryForm";

		if (transaction) {
			if (tranValiDAO.isCoaUsed(coa.getId())) {
				result.addErrorMessage(null, MessageId.TRANS_USED, coa.getAcCode());
			}

		} else {
			ChartOfAccount temp = coaService.findCoaByAcCode(coa.getAcCode());
			if (temp != null && !temp.getId().equals(coa.getId())) {
				result.addErrorMessage(formId + ":acCode", MessageId.ACCODE_EXISTED, temp.getAcCode(), temp.getAcType());
			}

			if (coa.getIbsbACode() != null && !coa.getIbsbACode().isEmpty()) {
				temp = coaService.findCoaByibsbAcCode(coa.getIbsbACode());
				if (temp != null && !temp.getId().equals(coa.getId())) {
					result.addErrorMessage(formId + ":ibsbACode", MessageId.IBSBCODE_EXISTED, temp.getIbsbACode(), temp.getAcCode(), temp.getAcType());
				}
			}

		}

		return result;
	}

}
