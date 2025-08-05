package org.ace.accounting.system.trantype.service.interfaces;

import java.util.List;

import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;

public interface ITranTypeService {
	public List<TranType> findAllTranType();
	public TranType findByTransCode(TranCode tranCode);
}
