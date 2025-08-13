package org.ace.accounting.system.motor;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.MOTORPOLICYVEHICLELINK)
@TableGenerator(name = "MOTORPOLICYVEHICLELINK_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "MOTORPOLICYVEHICLELINK_GEN", allocationSize = 10)
@NamedQueries(value = {
		@NamedQuery(name = "MotorPolicyVehicleLink.findAll", query = "SELECT mv FROM MotorPolicyVehicleLink mv ORDER BY mv.registrationNo ASC"),
		@NamedQuery(name = "MotorPolicyVehicleLink.findByRegistrationNo", query = "SELECT mv FROM MotorPolicyVehicleLink mv WHERE mv.registrationNo = :registrationNo") })
@EntityListeners(IDInterceptor.class)
public class MotorPolicyVehicleLink implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "MOTORPOLICYVEHICLELINK_GEN")
	@Column(name =  "vehicleId")
	private String id;
	
	private String registrationNo;
	
	private String model;
	
	private String engineNo;
	
	private String chassisNo;
	
	private int yearOfManufacture;
	
	private String typeOfBody;
	
	private String manufacture;
	
	private String productType;
	
	private int cubicCapacity;
	
	private int seating;
	
	private double sumInsured;
	
	private double estimatePresentSumInsurance;
	
	private int claimCount;
	
	private int ncbCount;
	
	private double ncbAmount;
	
	private String hirePurchaseCompany;
	
	// Additional Covers
    private boolean actsOfGod;
    private boolean nilExcess;
    private boolean srcc;
    private boolean theft;
    private boolean warRisk;
    private boolean betterment;
    private boolean paAndMt;
    private boolean sunRoof;
    private boolean thirdParty;
    
    @Version
	private int version;
	
	@Embedded
	private BasicEntity basicEntity;
	
//	@ManyToOne(cascade = CascadeType.ALL, optional = true)
//	private MotorPolicy motorPolicy;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "policyId") // foreign key in MotorPolicyVehicleLink
	private MotorPolicy motorPolicy;
    
    public MotorPolicy getMotorPolicy() {
		return motorPolicy;
	}

	public void setMotorPolicy(MotorPolicy motorPolicy) {
		this.motorPolicy = motorPolicy;
	}

	public boolean isActsOfGod() {
		return actsOfGod;
	}

	public void setActsOfGod(boolean actsOfGod) {
		this.actsOfGod = actsOfGod;
	}

	public boolean isNilExcess() {
		return nilExcess;
	}

	public void setNilExcess(boolean nilExcess) {
		this.nilExcess = nilExcess;
	}

	public boolean isSrcc() {
		return srcc;
	}

	public void setSrcc(boolean srcc) {
		this.srcc = srcc;
	}

	public boolean isTheft() {
		return theft;
	}

	public void setTheft(boolean theft) {
		this.theft = theft;
	}

	public boolean isWarRisk() {
		return warRisk;
	}

	public void setWarRisk(boolean warRisk) {
		this.warRisk = warRisk;
	}

	public boolean isBetterment() {
		return betterment;
	}

	public void setBetterment(boolean betterment) {
		this.betterment = betterment;
	}

	public boolean isPaAndMt() {
		return paAndMt;
	}

	public void setPaAndMt(boolean paAndMt) {
		this.paAndMt = paAndMt;
	}

	public boolean isSunRoof() {
		return sunRoof;
	}

	public void setSunRoof(boolean sunRoof) {
		this.sunRoof = sunRoof;
	}

	public boolean isThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(boolean thirdParty) {
		this.thirdParty = thirdParty;
	}

	public boolean isWindScreen() {
		return windScreen;
	}

	public void setWindScreen(boolean windScreen) {
		this.windScreen = windScreen;
	}

	private boolean windScreen;
	
	public MotorPolicyVehicleLink() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public int getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(int yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public String getTypeOfBody() {
		return typeOfBody;
	}

	public void setTypeOfBody(String typeOfBody) {
		this.typeOfBody = typeOfBody;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getCubicCapacity() {
		return cubicCapacity;
	}

	public void setCubicCapacity(int cubicCapacity) {
		this.cubicCapacity = cubicCapacity;
	}

	public int getSeating() {
		return seating;
	}

	public void setSeating(int seating) {
		this.seating = seating;
	}

	public double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public double getEstimatePresentSumInsurance() {
		return estimatePresentSumInsurance;
	}

	public void setEstimatePresentSumInsurance(double estimatePresentSumInsurance) {
		this.estimatePresentSumInsurance = estimatePresentSumInsurance;
	}

	public int getClaimCount() {
		return claimCount;
	}

	public void setClaimCount(int claimCount) {
		this.claimCount = claimCount;
	}

	public int getNcbCount() {
		return ncbCount;
	}

	public void setNcbCount(int ncbCount) {
		this.ncbCount = ncbCount;
	}

	public double getNcbAmount() {
		return ncbAmount;
	}

	public void setNcbAmount(double ncbAmount) {
		this.ncbAmount = ncbAmount;
	}

	public String getHirePurchaseCompany() {
		return hirePurchaseCompany;
	}

	public void setHirePurchaseCompany(String hirePurchaseCompany) {
		this.hirePurchaseCompany = hirePurchaseCompany;
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

}
