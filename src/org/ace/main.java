package org.ace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.tlf.TLF;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");
		EntityManager em = emf.createEntityManager();
		List<Branch> branchList = null;
		Map<String, String> branchMap = new HashMap<>();
		
		List<Currency> currencyList = null;
		Map<String, String> currencyMap = new HashMap<>();
		
		List<TLF> tlfList = null;
		Map<String, String> tlfMap = new HashMap<>();
		
		
		List<test> testList = null;
		Map<String, String> testMap = new HashMap<>();
		try{
		em.getTransaction().begin();
		//Select all the record from student table
		Query queryBranch = em.createQuery("SELECT br FROM Branch br");
		branchList = queryBranch.getResultList();
		
		Query queryCurrency = em.createQuery("SELECT cur FROM Currency cur");
		currencyList = queryCurrency.getResultList();
		
		/*Query querytlf = em.createQuery("SELECT DISTINCT(tlf.ccoa.id) FROM TLF tlf");
		tlfList = querytlf.getResultList();*/
		
		/*Query queryCoaSetup = em.createQuery("SELECT cs FROM COASetup cs");
		coaSetupList = queryCoaSetup.getResultList();*/
		
		Query querytest = em.createQuery("SELECT test FROM test test");
		testList = querytest.getResultList();
		
		
		/*Iterator it = lst.iterator();
		while (it.hasNext()){
			Branch tlf = (Branch) it.next();
		    System.out.println("Id:"+tlf.getId()+""+" Name:"+tlf.getName());
		
	
		
		}*/
		
		
		for (Branch b : branchList) {
			branchMap.put(b.getId(), b.getBranchCode());
			//System.out.println("Branch      " + branchList.size());
		}
		
		for (Currency c : currencyList) {
			currencyMap.put(c.getId(), c.getCurrencyCode());
			//System.out.println("Currency    " + currencyList.size());
		}
		
		for (test t : testList) {
			testMap.put(t.getId(), t.getCcoaid());
			//System.out.println("test     " + t.getCcoaid());
		}
		int i=1;
		for(String t : testMap.keySet()) {
			String accode = testMap.get(t);
			for(String b : branchMap.keySet()) {
				String branchid = b;
				for(String c : currencyMap.keySet()){
					String currencyid = c;
					/*Query testquery =em.createQuery("SELECT c FROM CurrencyChartOfAccount c Where c.branch.id=:branchid AND c.currency.id=:currencyId");
					testquery.setParameter("branchid", branchid);
					testquery.setParameter("currencyId", currencyid);
					ccoaList = testquery.getResultList();*/
					Query coaquery = em.createQuery("select c.id from ChartOfAccount c Where c.acCode = :accode");
					coaquery.setParameter("accode", accode);
					List<String> acode =  coaquery.getResultList();
					Query ccoaquery = em.createQuery("SELECT cc.id from CurrencyChartOfAccount cc where cc.coa.id =:acode AND cc.branch.id = :branchid AND cc.currency.id=:currencyId");
					ccoaquery.setParameter("branchid", branchid);
					ccoaquery.setParameter("currencyId", currencyid);
					ccoaquery.setParameter("acode", acode.get(0));
					List<String> ccoaid=  ccoaquery.getResultList();
					//Query testupdateQuery = em.createQuery("UPDATE COASetup cs SET cs.ccoa.id  =  SELECT cc.id from CurrencyChartOfAccount cc where cc.coa.id =  (select c.id from ChartOfAccount c Where c.accode = :accode) AND cc.branch.id = :branchid AND cc.currency.id=:currencyId  Where cs.ccoa.id =:accode and cs.branch.id=:branchid AND cs.currency.id=:currencyId ");
					//Query testupdateQuery = em.createQuery("UPDATE COASetup cs SET cs.ccoa.id = " + ccoaid +"  Where cs.ccoa.id =:accode AND cs.branch.id=:branchid AND cs.currency.id=:currencyId ");
					//Query testupdateQuery = em.createQuery("UPDATE COASetup cs SET cs.ccoa.id =" + ccoaid +" "+"WHERE cs.ccoa.id =" +accode+" AND"+ "cs.branch.id"+ "="+branchid+"AND"+" cs.currency.id="+currencyid);
					//Query testupdateQuery = em.createQuery("SELECT p from COASetup p where p.branch.id = :branchid AND p.currency.id=:currencyId");
					Query testupdateQuery = em.createQuery("SELECT p.id from COASetup p");
					//testupdateQuery.setParameter("branchid", "ISSYS008HO000000000221062013        ");
					//testupdateQuery.setParameter("currencyId", "ISSYS0210001000000000129032013      ");
					//testupdateQuery.setParameter("accode", "IAL004");
					List<String> coasetuplist = testupdateQuery.getResultList();
					//System.out.println("Test List" + coasetuplist.size());
					/*testupdateQuery.setParameter("branchid", branchid);
					testupdateQuery.setParameter("currencyId", currencyid);
					testupdateQuery.setParameter("accode", accode);*/
					//testupdateQuery.setParameter("ccoaid", ccoaid);
					//testupdateQuery.executeUpdate();
					StringBuffer buffer = null;
					Query query = null;
					buffer = new StringBuffer();
					buffer.append(" SELECT DISTINCT c.acCode FROM COASetup cs JOIN cs.ccoa cc JOIN cc.coa c ");
					buffer.append(" WHERE cc.currency.id = :currenyId AND cc.branch.id = :branchid");
					query = em.createQuery(buffer.toString());
					query.setParameter("currenyId",currencyid );
					query.setParameter("branchid",branchid );
					List<String> acCodeList = query.getResultList();
					em.flush();
					System.out.println("test"+ acCodeList.size());
					
				}
			
			}
			i++;
		   
		}
		System.out.println(i);
		//System.out.println("test query"+ ccoaList.size());
		
		System.out.println("Branch      " + branchList.size());
		System.out.println("Currency    " + currencyList.size());
		System.out.println("test     " + testList.size());
		/*for(CurrencyChartOfAccount cs : ccoaList) {
			
			System.out.println("Coasetupid" + cs.getAcName());
		}*/
		em.getTransaction().commit();
		}
		catch(Exception e){
		System.out.println(e.getMessage());
		}
		finally{
		em.close();
		}
		}

	}


