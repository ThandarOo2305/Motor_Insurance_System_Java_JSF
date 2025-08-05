package org.ace.accounting.excelUpload;

import java.util.Date;
import java.util.List;
import org.ace.java.component.persistence.exception.DAOException;

public interface IProposalDAO {

	public List<Proposal> findAllProposal()throws DAOException;
	
	public String getLatestGroupName()throws DAOException;
	
	public List<Proposal> findByGroupName(String groupName)throws DAOException;
	
	public List<Proposal> searchByBetweenDate(Date startDate,Date endDate)throws DAOException;
	
	public List<Proposal> searchByBetweenDateAndGroupName(Date startDate,Date endDate,String groupName)throws DAOException;
	
	public List<Proposal> search(Proposal proposal)throws DAOException;
}
