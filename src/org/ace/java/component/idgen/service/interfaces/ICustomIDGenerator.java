package org.ace.java.component.idgen.service.interfaces;

import java.util.Date;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.component.idgen.IDGen;
import org.ace.java.component.idgen.exception.CustomIDGeneratorException;
import org.ace.java.component.persistence.exception.DAOException;

@SuppressWarnings("rawtypes")
public interface ICustomIDGenerator {
	public String getNextId(String key, String productCode) throws CustomIDGeneratorException;

	public String getNextId(String key, String productCode, Branch branch) throws CustomIDGeneratorException;

	public String getPrefix(Class cla) throws CustomIDGeneratorException;

	public String getPrefix(Class cla, User user) throws CustomIDGeneratorException;

	public String getNextId(String key, String productCode, String vocherCodePrefix) throws CustomIDGeneratorException;

	public String getNextId(String key, String branchid, int month, int year, Date settlementDate) throws CustomIDGeneratorException;

	public IDGen findCustomIDGenByBranchCodeMonthandYear(String key, int month, int year, String branchId) throws SystemException;

	public IDGen insert(IDGen idgen) throws DAOException;

}
