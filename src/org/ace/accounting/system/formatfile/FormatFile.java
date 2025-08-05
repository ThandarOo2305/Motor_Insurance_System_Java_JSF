package org.ace.accounting.system.formatfile;

import java.io.Serializable;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.department.Department;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.FORMATFILE)
@TableGenerator(name = "FORMATFILE_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "FORMATFILE_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "FormatFile.findAll", query = "SELECT x FROM FormatFile x ORDER BY x.formatType,x.lNo ASC"),
		@NamedQuery(name = "FormatFile.findByType&Name", query = " SELECT x FROM FormatFile x WHERE x.formatType=:formatType AND x.formatName=:formatName ORDER BY x.lNo ASC ") })
@EntityListeners(IDInterceptor.class)
public class FormatFile implements Serializable, Comparable<FormatFile> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2483691049433794718L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "FORMATFILE_GEN")
	private String id;

	private String formatType;

	private String formatName;

	private int lNo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COAID", referencedColumnName = "ID")
	private ChartOfAccount chartOfAccount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPTID", referencedColumnName = "ID")
	private Department department;

	private String desp;

	private String acRange1;

	private String acRange2;

	private String lRange1;

	private String lRange2;

	private String other;

	@Transient
	private boolean invalidFormula = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "FORMATSTATUS")
	private ReportType reportType;

	// status=true when formula is inserted and is correct
	private boolean status = false;

	private boolean showHide;

	private boolean amountTotal;

	private String normal;

	@Enumerated(EnumType.STRING)
	private ColType colType;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public FormatFile() {
		super();
		id = System.nanoTime() + "";
	}

	public FormatFile(String formatType, String formatName) {
		this.formatType = formatType;
		this.formatName = formatName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public int getlNo() {
		return lNo;
	}

	public void setlNo(int lNo) {
		this.lNo = lNo;
	}

	public ChartOfAccount getChartOfAccount() {
		return chartOfAccount;
	}

	public void setChartOfAccount(ChartOfAccount chartOfAccount) {
		this.chartOfAccount = chartOfAccount;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public String getAcRange1() {
		return acRange1;
	}

	public void setAcRange1(String acRange1) {
		this.acRange1 = acRange1;
	}

	public String getAcRange2() {
		return acRange2;
	}

	public void setAcRange2(String acRange2) {
		this.acRange2 = acRange2;
	}

	public String getlRange1() {
		return lRange1;
	}

	public void setlRange1(String lRange1) {
		this.lRange1 = lRange1;
	}

	public String getlRange2() {
		return lRange2;
	}

	public void setlRange2(String lRange2) {
		this.lRange2 = lRange2;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isShowHide() {
		return showHide;
	}

	public void setShowHide(boolean showHide) {
		this.showHide = showHide;
	}

	public boolean isAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(boolean amountTotal) {
		this.amountTotal = amountTotal;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}

	public ColType getColType() {
		return colType;
	}

	public void setColType(ColType colType) {
		this.colType = colType;
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

	public void setInvalidFormula(boolean invalidFormula) {
		this.invalidFormula = invalidFormula;
	}

	public boolean isInvalidFormula() {
		return invalidFormula;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acRange1 == null) ? 0 : acRange1.hashCode());
		result = prime * result + ((acRange2 == null) ? 0 : acRange2.hashCode());
		result = prime * result + (amountTotal ? 1231 : 1237);
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((chartOfAccount == null) ? 0 : chartOfAccount.hashCode());
		result = prime * result + ((colType == null) ? 0 : colType.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((desp == null) ? 0 : desp.hashCode());
		result = prime * result + ((formatName == null) ? 0 : formatName.hashCode());
		result = prime * result + ((formatType == null) ? 0 : formatType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (invalidFormula ? 1231 : 1237);
		result = prime * result + lNo;
		result = prime * result + ((lRange1 == null) ? 0 : lRange1.hashCode());
		result = prime * result + ((lRange2 == null) ? 0 : lRange2.hashCode());
		result = prime * result + ((normal == null) ? 0 : normal.hashCode());
		result = prime * result + ((other == null) ? 0 : other.hashCode());
		result = prime * result + ((reportType == null) ? 0 : reportType.hashCode());
		result = prime * result + (showHide ? 1231 : 1237);
		result = prime * result + (status ? 1231 : 1237);
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormatFile other = (FormatFile) obj;
		if (acRange1 == null) {
			if (other.acRange1 != null)
				return false;
		} else if (!acRange1.equals(other.acRange1))
			return false;
		if (acRange2 == null) {
			if (other.acRange2 != null)
				return false;
		} else if (!acRange2.equals(other.acRange2))
			return false;
		if (amountTotal != other.amountTotal)
			return false;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (chartOfAccount == null) {
			if (other.chartOfAccount != null)
				return false;
		} else if (!chartOfAccount.equals(other.chartOfAccount))
			return false;
		if (colType != other.colType)
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (desp == null) {
			if (other.desp != null)
				return false;
		} else if (!desp.equals(other.desp))
			return false;
		if (formatName == null) {
			if (other.formatName != null)
				return false;
		} else if (!formatName.equals(other.formatName))
			return false;
		if (formatType == null) {
			if (other.formatType != null)
				return false;
		} else if (!formatType.equals(other.formatType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (invalidFormula != other.invalidFormula)
			return false;
		if (lNo != other.lNo)
			return false;
		if (lRange1 == null) {
			if (other.lRange1 != null)
				return false;
		} else if (!lRange1.equals(other.lRange1))
			return false;
		if (lRange2 == null) {
			if (other.lRange2 != null)
				return false;
		} else if (!lRange2.equals(other.lRange2))
			return false;
		if (normal == null) {
			if (other.normal != null)
				return false;
		} else if (!normal.equals(other.normal))
			return false;
		if (this.other == null) {
			if (other.other != null)
				return false;
		} else if (!this.other.equals(other.other))
			return false;
		if (reportType != other.reportType)
			return false;
		if (showHide != other.showHide)
			return false;
		if (status != other.status)
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	public String toString() {
		return lNo + " , " + desp + " , " + other + " , " + formatType;
	}

	@Override
	public int compareTo(FormatFile o) {
		return ((Integer) this.getlNo()).compareTo((Integer) o.getlNo());
	}
}
