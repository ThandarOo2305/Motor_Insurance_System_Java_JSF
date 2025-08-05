/**
 * 
 */
package org.ace.accounting.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aung
 *
 */
public class DayBookReportDto1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DayBookReportDto grandTotal;

	private List<DayBookReportDto> detailList;

	private boolean homeCurrencyConverted;
	
	public DayBookReportDto1(DayBookReportDto grandTotal, List<DayBookReportDto> detailList) {
		super();
		this.grandTotal = grandTotal;
		this.detailList = detailList;

	}

	public DayBookReportDto getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(DayBookReportDto grandTotal) {
		this.grandTotal = grandTotal;
	}

	public List<DayBookReportDto> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<DayBookReportDto> detailList) {
		this.detailList = detailList;
	}

	public boolean isHomeCurrencyConverted() {
		return homeCurrencyConverted;
	}

	public void setHomeCurrencyConverted(boolean homeCurrencyConverted) {
		this.homeCurrencyConverted = homeCurrencyConverted;
	}
	
	

}
