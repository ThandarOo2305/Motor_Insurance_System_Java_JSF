package org.ace.accounting.common.service;

import javax.annotation.Resource;

import org.ace.accounting.common.interfaces.ITranValiDAO;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.persistence.interfaces.ICurrencyDAO;
import org.springframework.stereotype.Service;

@Service(value = "CurrencyCodeValidator")
public class CurrencyCodeValidator implements IDataValidator<Currency> {

	@Resource(name = "TranValiDAO")
	private ITranValiDAO tranValiDAO;

	@Resource(name = "CurrencyDAO")
	private ICurrencyDAO curDAO;

	@Override
	public ValidationResult validate(Currency currency, boolean transaction) {
		ValidationResult result = new ValidationResult();
		String formId = "currencyEntryForm";
		if (!transaction) {
			if (currency.getIsHomeCur()) {
				Currency homeCurrency = curDAO.findHomeCurrency();
				if (homeCurrency != null && !homeCurrency.getId().equals(currency.getId())) {
					result.addErrorMessage(formId + ":isHomeCur", MessageId.HOME_CUR_USED);
				}
			}
		} else {
			if (tranValiDAO.isCurUsed(currency)) {
				result.addErrorMessage(formId + ":panelCur", MessageId.CUR_USED_TRANS);
			}
		}

		return result;
	}

}
