package org.ace.accounting.posting.persistence;

import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.posting.persistence.interfaces.IDataCloningDAO;
import org.ace.accounting.system.setup.persistence.interfaces.ISetupDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("DataCloningDAO")
public class DataCloningDAO extends BasicDAO implements IDataCloningDAO {

	@Resource(name = "SetupDAO")
	private ISetupDAO setupDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertCCOAClone() throws DAOException {
		StringBuffer sf = new StringBuffer("INSERT INTO CLONE_CCOA(ID, ACNAME, ACCRUED, BF, BUDGET, CBAL, HOBAL, OBAL, SCCRBAL, VERSION, CREATEDDATE,");
		sf.append(" CREATEDUSERID,  UPDATEDDATE, UPDATEDUSERID, BF1, BF10, BF11, BF12, BF13, BF2, BF3 ,BF4 ,BF5 ,BF6 ,BF7 ,BF8 ,BF9 , ");
		sf.append(" BFSRC1 ,BFSRC10 , BFSRC11 , BFSRC12 , BFSRC13 , BFSRC2 , BFSRC3 , BFSRC4 , BFSRC5 , BFSRC6 , BFSRC7 ,  BFSRC8 , BFSRC9 , ");
		sf.append("LYMSRC1 , LYMSRC10 , LYMSRC11 , LYMSRC12 , LYMSRC13 , LYMSRC2 , LYMSRC3 , LYMSRC4 ,  LYMSRC5 , LYMSRC6 , LYMSRC7 , LYMSRC8 ,");
		sf.append(" LYMSRC9 , M1 , M10 , M11 , M12 , M13 , M2 , M3 , M4 , M5 , M6 , M7 ,  M8 , M9 , MREV1 , MREV10 , MREV11 , MREV12 , MREV13 , MREV2");
		sf.append(" , MREV3 , MREV4 , MREV5 , MREV6 , MREV7 , MREV8 , MREV9 ,  MSRC1 , MSRC10 , MSRC11 , MSRC12 , MSRC13 , MSRC2 , MSRC3 , MSRC4 , ");
		sf.append("MSRC5 , MSRC6 , MSRC7 , MSRC8 , MSRC9 , COAID , BRANCHID ,  CURRENCYID , DEPARTMENTID)   (SELECT  ID, ACNAME, ACCRUED, BF, BUDGET, ");
		sf.append("CBAL, HOBAL, OBAL, SCCRBAL, VERSION, CREATEDDATE, CREATEDUSERID,  UPDATEDDATE, UPDATEDUSERID, BF1, BF10, BF11, BF12, BF13, BF2, BF3 ,");
		sf.append("BF4 ,BF5 ,BF6 ,BF7 ,BF8 ,BF9 ,  BFSRC1 ,BFSRC10 , BFSRC11 , BFSRC12 , BFSRC13 , BFSRC2 , BFSRC3 , BFSRC4 , BFSRC5 , BFSRC6 , BFSRC7 ,");
		sf.append("  BFSRC8 , BFSRC9 , LYMSRC1 , LYMSRC10 , LYMSRC11 , LYMSRC12 , LYMSRC13 , LYMSRC2 , LYMSRC3 , LYMSRC4 ,  LYMSRC5 , LYMSRC6 , LYMSRC7 , ");
		sf.append("LYMSRC8 , LYMSRC9 , M1 , M10 , M11 , M12 , 0 , M2 , M3 , M4 , M5 , M6 , M7 ,  M8 , M9 , MREV1 , MREV10 , MREV11 , MREV12 , MREV13 , MREV2 , ");
		sf.append("MREV3 , MREV4 , MREV5 , MREV6 , MREV7 , MREV8 , MREV9 ,  MSRC1 , MSRC10 , MSRC11 , MSRC12 , 0 , MSRC2 , MSRC3 , MSRC4 , MSRC5 , MSRC6 , MSRC7 , ");
		sf.append("MSRC8 , MSRC9 , COAID , BRANCHID ,  CURRENCYID , DEPARTMENTID FROM CCOA  )");
		try {
			Query q = em.createNativeQuery(sf.toString());
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insertCCOAClone by Data Cloning", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTLFCLONEByPostingDate(Date postingDate) throws DAOException {
		StringBuffer sf = new StringBuffer("INSERT INTO TLF_CLONE(ID, CUSTOMERID, ccoaid, HOMEAMOUNT, LOCALAMOUNT, CURRENCYID, ");
		sf.append(" CHEQUENO, BANKID, PURCHASEORDERID, LOANID, NARRATION, TRANTYPEID, DEPARTMENTID, ORGNTLFID, BRANCHID, RATE,");
		sf.append(" SETTLEMENTDATE, REVERSE, REFERENCENO, REFERENCETYPE, ENO, PAID, TLFNO, CLEARING, ISRENEWAL, PAYABLETRAN, ");
		sf.append(" VERSION,CREATEDDATE,CREATEDUSERID,UPDATEDDATE,UPDATEDUSERID)  (SELECT ID, CUSTOMERID, CCOAID, HOMEAMOUNT, LOCALAMOUNT,");
		sf.append(" CURRENCYID, CHEQUENO, BANKID, PURCHASEORDERID, LOANID, NARRATION, TRANTYPEID, DEPARTMENTID, ORGNTLFID, BRANCHID, RATE,");
		sf.append(" SETTLEMENTDATE, REVERSE, REFERENCENO, REFERENCETYPE, ENO, PAID, TLFNO, CLEARING, ISRENEWAL, PAYABLETRAN, ");
		sf.append(" VERSION,CREATEDDATE,CREATEDUSERID,UPDATEDDATE,UPDATEDUSERID FROM TLF t WHERE t.SETTLEMENTDATE <= ?1 )");
		try {
			Query q = em.createNativeQuery(sf.toString());
			q.setParameter(1, postingDate);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insertTLFHIST By PostingDate", pe);
		}
	}
}
