package org.ace.accounting.report;

import java.util.Date;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

/***************************************************************************************
 * @author PPA
 * @Date 2016-03-31
 * @Version 1.0
 * @Purpose This class serves as Criteria to find report data
 * 
 ***************************************************************************************/

public class ReportCriteria {

	// Branch Criteria.
	private Branch branch;
	// Currency Criteria.
	private Currency currency;
	// Created Date Criteria.
	private Date date;
	// By Home Currency or Not.
	private boolean isByHome;

	// Default Constructor.
	public ReportCriteria() {
	}

	// Constructor With parameters.
	public ReportCriteria(Branch branch, Currency currency, Date date, Boolean isByHome) {
		super();
		this.branch = branch;
		this.currency = currency;
		this.date = date;
		this.isByHome = isByHome;
	}

	// Getter.
	public Branch getBranch() {
		return branch;
	}

	// Setter.
	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	// Getter.
	public Currency getCurrency() {
		return currency;
	}

	// Setter.
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	// Getter.
	public Date getDate() {
		return date;
	}

	// Setter.
	public void setDate(Date date) {
		this.date = date;
	}

	// Getter.
	public boolean isByHome() {
		return isByHome;
	}

	// Setter.
	public void setByHome(boolean isByHome) {
		this.isByHome = isByHome;
	}

}
