package org.ace.accounting.system.trantype.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;
import org.ace.accounting.system.trantype.persistence.interfaces.ITranTypeDAO;
import org.ace.accounting.system.trantype.service.interfaces.ITranTypeService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "TranTypeService")
public class TranTypeService extends BaseService implements ITranTypeService {

	@Resource(name = "TranTypeDAO")
	private ITranTypeDAO tranTypeDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<TranType> dataRepService;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<TranType> findAllTranType() {
		List<TranType> result = null;
		try {
			result = tranTypeDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of TranType)", e);
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TranType findByTransCode(TranCode tranCode) {
		TranType result = null;
		try {
			result = tranTypeDAO.findByTransCode(tranCode);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find TranType By tranCode)" + tranCode, e);
		}
		return result;
	}
	
}
