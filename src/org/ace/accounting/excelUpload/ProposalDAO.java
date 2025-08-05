package org.ace.accounting.excelUpload;

import java.io.BufferedReader;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;

@Repository(value = "ProposalDAO")
public class ProposalDAO extends BasicDAO implements IProposalDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal> findAllProposal() throws DAOException {
		List<Proposal> proposalList = null;
		try {
			 Query query = em.createNamedQuery("Proposal.findAll");
			 proposalList = query.getResultList();
			 em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of Proposal", pe);
		}
		return proposalList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getLatestGroupName() throws DAOException {
		String groupName = null;
		try {
			Query query = em.createNamedQuery("Proposal.getLatestGroupName");
			query.setMaxResults(1);
			List<String> groupNames = query.getResultList();
			if(!groupNames.isEmpty()) {
				groupName = groupNames.get(0);
			}
			em.flush();
		}catch(NoResultException nre) {
			throw translate("no result", nre);
		}
		catch (PersistenceException pe) {
			throw translate("Failed to find all of Proposal", pe);
		}
		return groupName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal> findByGroupName(String groupName) throws DAOException {
		List<Proposal> proposal  = null;
		try {
			Query query = em.createNamedQuery("Proposal.findByGroupName");
			query.setParameter("groupName", groupName);
			proposal = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find Proposal by group name", pe);
		}
		return proposal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal> searchByBetweenDate(Date startDate,Date endDate) throws DAOException {
		List<Proposal> proposal = null;
		try {
			Query query = em.createNamedQuery("Proposal.findByBetweenDate");
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			proposal = query.getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to find Proposal by BetweenDate", pe);
		}
		return proposal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal> searchByBetweenDateAndGroupName(Date startDate, Date endDate, String groupName)throws DAOException {
		List<Proposal> proposal = null;
		try {
			Query query = em.createNamedQuery("findByBetweenDateAndGroupName");
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			proposal = query.getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to find Proposal by GroupName", pe);
		}
		return proposal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal> search(Proposal proposal) throws DAOException {
		List<Proposal> proposalList = null;
		try {
			StringBuffer stringBuffer = new StringBuffer("SELECT p FROM Proposal p WHERE p.startDate BETWEEN :startDate AND :endDate AND p.endDate BETWEEN :startDate AND :endDate ");
			if(!proposal.getGroupName().isEmpty()) {
				stringBuffer.append("AND p.groupName = :groupName ");
			}
			if(!proposal.getPolicyNo().isEmpty()) {
				stringBuffer.append("AND p.policyNo = :policyNo ");
			}
			if(!proposal.getGroupName().isEmpty() && !proposal.getPolicyNo().isEmpty()) {
				stringBuffer.append("AND p.policyNo = :policyNo AND p.groupName = :groupName");
			}
			Query query = em.createQuery(stringBuffer.toString());
			query.setParameter("startDate", proposal.getStartDate());
			query.setParameter("endDate", proposal.getEndDate());
			if(!proposal.getGroupName().isEmpty()) {
				query.setParameter("groupName", proposal.getGroupName());
			}
			if(!proposal.getPolicyNo().isEmpty()) {
				query.setParameter("policyNo", proposal.getPolicyNo());
			}
			if(!proposal.getGroupName().isEmpty() && !proposal.getPolicyNo().isEmpty()) {
				query.setParameter("groupName", proposal.getGroupName());
				query.setParameter("policyNo", proposal.getPolicyNo());
			}
			
			proposalList = query.getResultList();
			
		} catch (PersistenceException pe) {
			throw translate("Failed to find Proposal ", pe);
		}
		return proposalList;
	}
	
	

}
