package org.ace.accounting.occupation;

import java.util.List;


import org.ace.java.component.persistence.exception.DAOException;

public interface IOccupationDAO {
	
	public List<Occupation> findAll()throws DAOException;

}
