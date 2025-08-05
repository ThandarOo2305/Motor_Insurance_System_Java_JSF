package org.ace.accounting.posting.service.interfaces;

import java.util.Date;

import org.ace.accounting.system.branch.Branch;
import org.ace.java.component.SystemException;

public interface IDailyPostingService {
	public void processDailyPosting(Branch branch, Date postingDate) throws SystemException;
}
