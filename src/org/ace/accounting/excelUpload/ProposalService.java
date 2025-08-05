package org.ace.accounting.excelUpload;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "ProposalService")
public class ProposalService implements IProposalService {
	
	@Resource(name = "DataRepService")
	private IDataRepService<Proposal> dataRepService;
	
	@Resource(name = "ProposalDAO")
	private IProposalDAO proposalDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Proposal> findAll() throws SystemException {
		List<Proposal> proposalList = null;
		try {
			proposalList = proposalDAO.findAllProposal();
			
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to get customer list", e);
		}
		return proposalList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Proposal addNewProposal(List<Proposal> proposalList) throws SystemException {
		  
		try {
			 String latestGroupId = proposalDAO.getLatestGroupName();
			
			 String newGroupId = incrementGroupId(latestGroupId);
			
			for(Proposal proposal : proposalList) {
				proposal.setGroupName(newGroupId);
				proposal.setEndDate(endDate(proposal));
				dataRepService.insert(proposal);
			}
			
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to create a Customer", e);
		}

		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Proposal updateProposal(Proposal proposal) throws SystemException {
		try {
			dataRepService.update(proposal);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to create a Customer", e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteProposal(Proposal proposal) throws SystemException {
		try {
			dataRepService.delete(proposal);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to create a Customer", e);
		}
		
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Proposal> searchByGroupName(String groupName) throws SystemException {
		List<Proposal> proposalList = null;
		try {
			proposalList = proposalDAO.findByGroupName(groupName);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to search by group name", e);
		}
		return proposalList;
	}
	
	

	@Override
	public List<Proposal> searchByBetweenDate(Date startDate,Date endDate) throws SystemException {
		List<Proposal> proposalList = null;
		try {
			proposalList = proposalDAO.searchByBetweenDate(startDate, endDate);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to search by start date", e);
		}
		return proposalList;
	}
	
	@Override
	public List<Proposal> search(Proposal proposal) throws SystemException {
		List<Proposal> proposalList = null;
		try {
			proposalList = proposalDAO.search(proposal);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to search by start date", e);
		}
		return proposalList;
	
	}

	@Override
	public Proposal changeDTOToProposal(ProposalDTO proposalDTO) {
		Proposal proposal = new Proposal();
		proposal.setId(proposalDTO.getId());
		proposal.setGroupName(proposalDTO.getGroupName());
		proposal.setBank(proposalDTO.getBank());
		proposal.setInsuredName(proposalDTO.getInsuredName());
		proposal.setAddress(proposalDTO.getAddress());
		proposal.setPolicyNo(proposalDTO.getPolicyNo());
		proposal.setSumInsured(proposalDTO.getSumInsured());
		proposal.setRate(proposalDTO.getRate());
		proposal.setPremium(proposalDTO.getPremium());
		proposal.setStartDate(proposalDTO.getStartDate());
		proposal.setEndDate(proposalDTO.getEndDate());
		proposal.setRemark(proposalDTO.getRemark());
		proposal.setVersion(proposalDTO.getVersion());
		proposal.setBasicEntity(proposalDTO.getBasicEntity());
		
		return proposal;
	}
	

	@Override
	public ProposalDTO changeProposalToDTO(Proposal proposal) {
		ProposalDTO proposalDTO = new ProposalDTO();
		proposalDTO.setId(proposal.getId());
		proposalDTO.setGroupName(proposal.getGroupName());
		proposalDTO.setBank(proposal.getBank());
		proposalDTO.setInsuredName(proposal.getInsuredName());
		proposalDTO.setAddress(proposal.getAddress());
		proposalDTO.setPolicyNo(proposal.getPolicyNo());
		proposalDTO.setSumInsured(proposal.getSumInsured());
		proposalDTO.setRate(proposal.getRate());
		proposalDTO.setPremium(proposal.getPremium());
		proposalDTO.setRemark(proposal.getRemark());
		proposalDTO.setStartDate(proposal.getStartDate());
		proposalDTO.setEndDate(proposal.getEndDate());
		proposalDTO.setVersion(proposal.getVersion());
		proposalDTO.setBasicEntity(proposal.getBasicEntity());
		return proposalDTO;
	}
	
	
	
	@Override
	public Proposal changeSearchDTOToProposal(ProposalSearchDTO proposalDTO) {
		Proposal proposal = new Proposal();
		proposal.setStartDate(proposalDTO.getStartDate());
		proposal.setEndDate(proposalDTO.getEndDate());
		proposal.setGroupName(proposalDTO.getGroupName());
		proposal.setPolicyNo(proposalDTO.getPolicyNo());
		return proposal;
	}

	@Override
	public ProposalSearchDTO changeProposalToSearchDTO(Proposal proposal) {
		ProposalSearchDTO proposalSearchDTO = new ProposalSearchDTO();
		proposalSearchDTO.setStartDate(proposal.getStartDate());
		proposalSearchDTO.setEndDate(proposal.getEndDate());
		proposalSearchDTO.setGroupName(proposal.getGroupName());
		proposalSearchDTO.setPolicyNo(proposal.getPolicyNo());
		return proposalSearchDTO;
	}

	private String incrementGroupId(String latestGroupId) {
        String newGroupId;
        if (latestGroupId != null) {
            // Extract the numeric part of the latest group ID and increment it
            int numericPart = Integer.parseInt(latestGroupId.substring(4)) + 1;
            // Format the new group ID
            newGroupId = String.format("GID-%03d", numericPart);
        } else {
            // If there are no existing group IDs, start from 001
            newGroupId = "GID-001";
        }
        return newGroupId;
    }
	
	private Date endDate(Proposal proposal){
        Date startDate = proposal.getStartDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();
        return endDate;
    }

	
	
	

}
