package org.ace.accounting.system.formatfile.persistence.interfaces;

import java.util.List;

import org.ace.accounting.dto.ReportFormatDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.system.formatfile.ColType;
import org.ace.accounting.system.formatfile.FormatFile;
import org.ace.java.component.persistence.exception.DAOException;

public interface IFormatFileDAO {
	public List<FormatFile> findAll() throws DAOException;

	public List<ReportFormatDto> findByReportType(ReportType reportType) throws DAOException;

	public List<FormatFile> findByTypeAndName(String formatType, String formatName) throws DAOException;

	public List<FormatFile> findLR(String formatType, ReportType reportType) throws DAOException;

	public List<FormatFile> findOther(ColType colType,String formatType, ReportType reportType) throws DAOException;
	
}
