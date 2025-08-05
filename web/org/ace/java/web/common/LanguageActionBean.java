package org.ace.java.web.common;

import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="LanguageActionBean")
@SessionScoped
public class LanguageActionBean {

	private String locale = "en";

	public String getLocale() {
		return locale;
	}

	public void setLocale(String localeCode) {
		this.locale = localeCode;
	}
	
	public void toEnglish() {
		Locale locale = Locale.ENGLISH;
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		this.locale = "en";
	}

	public void toJapanese() {
		Locale locale = Locale.JAPANESE;
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		this.locale = "ja";
	}
}
