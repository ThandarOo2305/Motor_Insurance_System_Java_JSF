package org.ace.accounting.system.formatfile.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.dto.ReportFormatDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.system.formatfile.FormatFile;
import org.ace.accounting.system.formatfile.persistence.interfaces.IFormatFileDAO;
import org.ace.accounting.system.formatfile.service.interfaces.IFormatFileService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "FormatFileService")
public class FormatFileService extends BaseService implements IFormatFileService {

	@Resource(name = "FormatFileDAO")
	private IFormatFileDAO formatFileDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<FormatFile> dataRepService;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<FormatFile> findAllFormatFile() {
		List<FormatFile> result = null;
		try {
			result = formatFileDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of FormatFile)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ReportFormatDto> findByReportType(ReportType reportType) {
		List<ReportFormatDto> result = null;
		try {
			result = formatFileDAO.findByReportType(reportType);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of FormatFile)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateFormatFile(FormatFile newFF, FormatFile oldFF, List<FormatFile> ffList) {
		try {
			for (FormatFile ff : ffList) {
				ff.setFormatType(newFF.getFormatType());
				ff.setFormatName(newFF.getFormatName());
				dataRepService.update(ff);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update formatfile)", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(FormatFile ff, List<FormatFile> ffList) {
		try {
			for (FormatFile ff1 : ffList) {
				ff1.setFormatType(ff.getFormatType());
				ff1.setFormatName(ff.getFormatName());
				dataRepService.insert(ff1);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to insert formatfile)", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<FormatFile> findByTypeAndName(String formatType, String formatName) {
		List<FormatFile> result = null;
		try {
			result = formatFileDAO.findByTypeAndName(formatType, formatName);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find FormatFile by type and name)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(ReportFormatDto dto) {
		try {
			List<FormatFile> list = formatFileDAO.findByTypeAndName(dto.getFormatType(), dto.getFormatName());
			for (FormatFile ff : list) {
				dataRepService.delete(ff);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete FormatFile)", e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRange(List<FormatFile> ffList) {
		try {
			for (FormatFile ff : ffList) {
				dataRepService.delete(ff);
			}

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete FormatFile)", e);
		}
	}


}