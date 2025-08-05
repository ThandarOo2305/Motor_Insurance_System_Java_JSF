package org.ace.accounting.occupation;

import java.util.List;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.java.web.common.BaseBean;
import org.primefaces.PrimeFaces;

@ManagedBean(name="OccupationDialogActionBean")
@ViewScoped
public class OccupationDialogActionBean extends BaseBean{
	
	@ManagedProperty(value ="#{OccupationService}")
	private IOccupationService occService;

	
	
	public IOccupationService getOccService() {
		return occService;
	}

	public void setOccService(IOccupationService occService) {
		this.occService = occService;
	}

	private List<Occupation> occupations;

	@PostConstruct
	public void init() {
		occupations = occService.findAll();
	}
	
	public List<Occupation> getOccupations() {
		return occupations;
	}

	public void selectOccupation(Occupation occupation) {
		PrimeFaces.current().dialog().closeDynamic(occupation);
	}
	

}
