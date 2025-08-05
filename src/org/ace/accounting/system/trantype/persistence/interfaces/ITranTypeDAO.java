package org.ace.accounting.system.trantype.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;
import org.ace.java.component.persistence.exception.DAOException;

public interface ITranTypeDAO {
	
	public List<TranType> findAll() throws DAOException;
	
	public TranType findByTransCode(TranCode tranCode) throws DAOException;
}
