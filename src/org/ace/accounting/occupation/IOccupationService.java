package org.ace.accounting.occupation;

import java.util.List;


import org.ace.java.component.SystemException;

public interface IOccupationService {
	
	public Occupation addNewOccupation(Occupation occupation) throws SystemException;
	
	public List<Occupation> findAll()throws SystemException;

	public Occupation updateOccupation(Occupation occupation)throws SystemException;
	
	public void deleteOccupation(Occupation occupation) throws SystemException;

}
