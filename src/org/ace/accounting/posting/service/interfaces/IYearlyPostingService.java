package org.ace.accounting.posting.service.interfaces;

import java.util.Date;

import org.ace.accounting.dto.YPDto;
import org.ace.accounting.system.branch.Branch;

public interface IYearlyPostingService {
	public void handleYearlyPosting(Date postingDate, YPDto plDto, Branch branch);

	public void handleYearlyClosing(YPDto plDto, Branch branch);

}
