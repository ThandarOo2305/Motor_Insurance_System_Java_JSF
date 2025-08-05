package org.ace.accounting.occupation;

import java.util.List;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.system.branch.Branch;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "ManageOccupationActionBean")
@ViewScoped
public class ManageOccupationActionBean extends BaseBean {
	
	@ManagedProperty(value ="#{OccupationService}")
	private IOccupationService occService;
	
	public IOccupationService getOccService() {
		return occService;
	}

	public void setOccService(IOccupationService occService) {
		this.occService = occService;
	}

	private Occupation occupation;
	private List<Occupation> occList;
	private boolean createNew;
	
	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public List<Occupation> getOccList() {
		return occList;
	}

	public void setOccList(List<Occupation> occList) {
		this.occList = occList;
	}

	
	public boolean isCreateNew() {
		return createNew;
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}

	@PostConstruct 
	  public void init() { 
		  createNewOccupation();
		  rebindData(); 
  }
	
	private void rebindData() {
		occList = occService.findAll();
	}
	
	public void createNewOccupation() {
		createNew = true;
		occupation = new Occupation();
	}
	
	
	public void addNewOccupation() {
		System.out.println(occupation.getName());
		occService.addNewOccupation(occupation);
		createNewOccupation();
		rebindData(); 
	}
	
	public void prepareUpdateOccupation(Occupation occupation) {
		createNew = false;
		this.occupation = occupation;
	}
	
	public void updateOccupation() {
		try {
			occService.updateOccupation(occupation);
			createNewOccupation();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}
	
	public String deleteOccupation(Occupation occupation) {
		try {
			occService.deleteOccupation(occupation);;
			createNewOccupation();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
		return null;
	}

}
