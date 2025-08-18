package org.ace.accounting.system.motor;

import java.util.Date;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.motor.enumTypes.SaleChannelType;

public class MotorEnquiryDTO {
	private String policyNo;
	private String proposalNo;
	private String registrationNo;
	private SaleChannelType saleChannel;
	private String customerName;
	private Branch branch;
	private int claimCount;
	private double totalSumInsured;
	private double basicPremium;
	private Date submittedDate;

	public MotorEnquiryDTO(String policyNo, String proposalNo, String registrationNo, SaleChannelType saleChannel, String customerName,
			Branch branch, int claimCount, double totalSumInsured, double basicPremium, Date submittedDate) {
		this.policyNo = policyNo;
		this.proposalNo = proposalNo;
		this.registrationNo = registrationNo;
		this.saleChannel = saleChannel;
		this.customerName = customerName;
		this.branch = branch;
		this.claimCount = claimCount;
		this.totalSumInsured = totalSumInsured;
		this.basicPremium = basicPremium;
		this.submittedDate = submittedDate;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public SaleChannelType getSaleChannel() {
		return saleChannel;
	}

	public void setSaleChannel(SaleChannelType saleChannel) {
		this.saleChannel = saleChannel;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public int getClaimCount() {
		return claimCount;
	}

	public void setClaimCount(int claimCount) {
		this.claimCount = claimCount;
	}

	public double getTotalSumInsured() {
		return totalSumInsured;
	}

	public void setTotalSumInsured(double totalSumInsured) {
		this.totalSumInsured = totalSumInsured;
	}

	public double getBasicPremium() {
		return basicPremium;
	}

	public void setBasicPremium(double basicPremium) {
		this.basicPremium = basicPremium;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

//	public String getCreatedUser() {
//		return createdUserId;
//	}
//
//	public void setCreatedUser(String createdUser) {
//		this.createdUserId = createdUser;
//	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
