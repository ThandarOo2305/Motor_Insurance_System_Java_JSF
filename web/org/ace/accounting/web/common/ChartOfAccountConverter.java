package org.ace.accounting.web.common;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.ace.accounting.system.chartaccount.ChartOfAccount;

@FacesConverter("ChartOfAccountConverter")
public class ChartOfAccountConverter implements Converter {

	public static List<ChartOfAccount> coaList;

	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (submittedValue.trim().equals("")) {
			return null;
		} else {
			try {
				for (ChartOfAccount coa : coaList) {
					if (coa.getAcCode() == submittedValue) {
						return coa;
					}
				}
			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Account not found!"));
			}
		}

		return null;
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			return String.valueOf(((ChartOfAccount) value).getAcCode());
		}
	}
}