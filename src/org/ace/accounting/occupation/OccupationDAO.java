package org.ace.accounting.occupation;

import java.util.List;



import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Repository(value = "OccupationDAO")
public class OccupationDAO extends BasicDAO implements IOccupationDAO {
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Occupation> findAll()throws DAOException{
		List<Occupation> occList = null;
		try {
			Query q = em.createNamedQuery("Occupation.findAll");
			occList = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of Student", pe);
		}
		return occList;
	}

}
