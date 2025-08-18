package org.ace.accounting.system.motor;

public class MotorPolicyDTO {
	
	private String customerName;
    private String policyNo;
    private String proposalNo;
    private double sumInsured;
    private double basicTermPremium;
    private double AddOnTermPremium;
    private double totalPremium;
    
    
    
	public MotorPolicyDTO(String customerName, String policyNo, String proposalNo, double sumInsured,
			double basicTermPremium, double addOnTermPremium, double totalPremium) {
		super();
		this.customerName = customerName;
		this.policyNo = policyNo;
		this.proposalNo = proposalNo;
		this.sumInsured = sumInsured;
		this.basicTermPremium = basicTermPremium;
		AddOnTermPremium = addOnTermPremium;
		this.totalPremium = totalPremium;
	}
	public MotorPolicyDTO() {
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	public double getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(double sumInsured) {
		this.sumInsured = sumInsured;
	}
	public double getBasicTermPremium() {
		return basicTermPremium;
	}
	public void setBasicTermPremium(double basicTermPremium) {
		this.basicTermPremium = basicTermPremium;
	}
	public double getAddOnTermPremium() {
		return AddOnTermPremium;
	}
	public void setAddOnTermPremium(double addOnTermPremium) {
		AddOnTermPremium = addOnTermPremium;
	}
	public double getTotalPremium() {
		return totalPremium;
	}
	public void setTotalPremium(double totalPremium) {
		this.totalPremium = totalPremium;
	}
    
    
    

}
