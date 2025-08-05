package org.ace.accounting.web.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.dto.CoaDialogDto;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "CoaDialogActionBean")
@ViewScoped
public class CoaDialogActionBean extends BaseBean {

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
	}

	private CoaDialogDto dto = new CoaDialogDto();

	@PostConstruct
	public void init() {
		Comparator<ChartOfAccount> c = Comparator.comparing(ChartOfAccount::getAcType).thenComparing(ChartOfAccount::getAcCodeType).thenComparing(ChartOfAccount::getAcCode);
		// There must be criteria to get coaList
		CoaDialogCriteriaDto criteria = (CoaDialogCriteriaDto) getParam(ParamId.COA_DIALOG_CRITERIA_DATA);
		List<ChartOfAccount> list = new ArrayList<ChartOfAccount>();
		if (getParam(ParamId.COA_DATA) == null) {
			list = coaService.findAllCoaByCriteria(criteria);
			list.sort(c);
			dto.setCoaList(list);
			dto.setCriteriaDto(criteria);
			putParam(ParamId.COA_DATA, dto);
		} else {
			dto = (CoaDialogDto) getParam(ParamId.COA_DATA);
			if (!dto.getCriteriaDto().equals(criteria)) {
				list = coaService.findAllCoaByCriteria(criteria);
				list.sort(c);
				dto.setCoaList(list);
				dto.setCriteriaDto(criteria);
				putParam(ParamId.COA_DATA, dto);
			}
		}
		removeParam(ParamId.COA_DIALOG_CRITERIA_DATA);
	}

	public List<ChartOfAccount> getCoaList() {
		return dto.getCoaList();
	}

	public void selectCoa(ChartOfAccount coa) {
		PrimeFaces.current().dialog().closeDynamic(coa);
		/* RequestContext.getCurrentInstance().closeDialog(coa); */
	}
}
