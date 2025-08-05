package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GenLedgerDetailsDTO {
	private String accountCode;
	private String accountName;
	private BigDecimal openingBalance;
	private List<GenLedgerSummaryDTO> detailsAccountList;
	private JRBeanCollectionDataSource detailsAccountDataSource;

	public GenLedgerDetailsDTO(String accountCode,String accountName, BigDecimal openingBalance, List<GenLedgerSummaryDTO> detailsAccountList) {
		super();
		this.accountCode = accountCode;
		this.accountName = accountName;
		this.openingBalance = openingBalance;
		this.detailsAccountList = detailsAccountList;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}

	public List<GenLedgerSummaryDTO> getDetailsAccountList() {
		if (detailsAccountList == null) {
			return new ArrayList<>();
		} else {
			return detailsAccountList;
		}

	}

	public void setDetailsAccountList(List<GenLedgerSummaryDTO> detailsAccountList) {
		this.detailsAccountList = detailsAccountList;
	}

	public JRBeanCollectionDataSource getDetailsAccountDataSource() {
		return new JRBeanCollectionDataSource(detailsAccountList, false);
	}
	
	

}
