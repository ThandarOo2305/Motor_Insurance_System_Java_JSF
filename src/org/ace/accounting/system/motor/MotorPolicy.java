package org.ace.accounting.system.motor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.motor.enumTypes.CurrencyType;
import org.ace.accounting.system.motor.enumTypes.CustomerType;
import org.ace.accounting.system.motor.enumTypes.PaymentType;
import org.ace.accounting.system.motor.enumTypes.SaleChannelType;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.MOTORPOLICY)
@TableGenerator(name = "MOTORPOLICY_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "MOTORPOLICY_GEN", allocationSize = 10)
@NamedQueries(value = {
		@NamedQuery(name = "MotorPolicy.findAll", query = "SELECT mp FROM MotorPolicy mp ORDER BY mp.customerName ASC"),
		@NamedQuery(name = "MotorPolicy.findByCustomerName", query = "SELECT mp FROM MotorPolicy mp WHERE mp.customerName = :customerName"),
		@NamedQuery(name = "MotorPolicy.findByPolicyNo", query = "SELECT mp FROM MotorPolicy mp WHERE mp.policyNo = :policyNo")})
@EntityListeners(IDInterceptor.class)
public class MotorPolicy implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "MOTORPOLICY_GEN")
	@Column(name = "policyId")
	private String id;

	@Enumerated(EnumType.STRING)
	private CustomerType customerType;

	private String customerName;

	@Enumerated(EnumType.STRING)
	private SaleChannelType saleChannel;

	private String policyNo;

	private String proposalNo;

	private Date submittedDate;

	private Date policyStartDate;

	private Date policyEndDate;

	private int period;

	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHID", referencedColumnName = "ID")
	private Branch branch;

	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "motorPolicy")
	private List<MotorPolicyVehicleLink> vehicleLinks = new ArrayList<>();

	public void addVehicleLink(MotorPolicyVehicleLink vehicleLink) {
		vehicleLinks.add(vehicleLink);
		vehicleLink.setMotorPolicy(this);
	}

	public void removeVehicleLink(MotorPolicyVehicleLink vehicleLink) {
		vehicleLinks.remove(vehicleLink);
		vehicleLink.setMotorPolicy(null);
	}

	public MotorPolicy() {

	}

	public List<MotorPolicyVehicleLink> getMotorPolicyVehicleLinks() {
		return vehicleLinks;
	}

	public void setMotorPolicyVehicleLinks(List<MotorPolicyVehicleLink> vehicleLinks) {
		this.vehicleLinks = vehicleLinks;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SaleChannelType getSaleChannel() {
		return saleChannel;
	}

	public void setSaleChannel(SaleChannelType saleChannel) {
		this.saleChannel = saleChannel;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public Date getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public List<MotorPolicyVehicleLink> getVehicleLinks() {
		return vehicleLinks;
	}

	public void setVehicleLinks(List<MotorPolicyVehicleLink> vehicleLinks) {
		this.vehicleLinks = vehicleLinks;
	}

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public BasicEntity getBasicEntity() {
		return basicEntity;
	}

	public void setBasicEntity(BasicEntity basicEntity) {
		this.basicEntity = basicEntity;
	}

	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}
}
