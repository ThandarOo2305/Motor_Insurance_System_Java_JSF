package org.ace.accounting.excelUpload;

import java.util.Date;
import java.util.List;

import org.ace.java.component.SystemException;

public interface IProposalService {

    public List<Proposal> findAll()throws SystemException;
	
	public Proposal addNewProposal(List<Proposal> proposal)throws SystemException;
	
	public Proposal updateProposal(Proposal proposal)throws SystemException;
	
	public void deleteProposal(Proposal proposal)throws SystemException;
	
	public Proposal changeDTOToProposal(ProposalDTO proposalDTO) ;
	
	public ProposalDTO changeProposalToDTO(Proposal proposal) ;
	
	public Proposal changeSearchDTOToProposal(ProposalSearchDTO proposalDTO) ;
	
	public ProposalSearchDTO changeProposalToSearchDTO(Proposal proposal) ;
	
	public List<Proposal> searchByGroupName(String groupName) throws SystemException;
	
	public List<Proposal> searchByBetweenDate(Date startDate,Date endDate) throws SystemException;
	
	public List<Proposal> search(Proposal proposal)throws SystemException;
	
}
