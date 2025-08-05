package org.ace.accounting.posting.service;

import java.util.Date;

import javax.annotation.Resource;

import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.posting.persistence.interfaces.IDataCloningDAO;
import org.ace.accounting.posting.service.interfaces.IDataCloningService;
import org.ace.accounting.system.chartaccount.CcoaHistory;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.systempost.SystemPost;
import org.ace.accounting.system.systempost.service.interfaces.ISystemPostService;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.system.tlfhist.TLFHIST;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "DataCloningService")
public class DataCloningService extends BaseService implements IDataCloningService {
	@Resource(name = "DataRepService")
	private IDataRepService<SystemPost> systemPostDataService;

	@Resource(name = "SystemPostService")
	private ISystemPostService systemPostService;

	@Resource(name = "TLFService")
	private ITLFService tlfService;

	@Resource(name = "DataRepService")
	private IDataRepService<TLF> tlfDataService;

	@Resource(name = "DataRepService")
	private IDataRepService<TLFHIST> tlfHistDataService;

	@Resource(name = "CcoaService")
	private ICcoaService ccoaService;

	@Resource(name = "DataRepService")
	private IDataRepService<CcoaHistory> ccoaHistDataService;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataService;

	@Resource(name = "CoaService")
	private ICoaService coaService;

	@Resource(name = "DataCloningDAO")
	private IDataCloningDAO dataCloningDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public void handleDataCloning(Date postingDate) {
		try {
			postingDate = DateUtils.resetEndDate(postingDate);
			dataCloningDAO.insertCCOAClone();
			dataCloningDAO.insertTLFCLONEByPostingDate(postingDate);

		} catch (DAOException de) {
			throw new SystemException(de.getErrorCode(), "Failed to handle yearly posting.", de);
		}
	}

}