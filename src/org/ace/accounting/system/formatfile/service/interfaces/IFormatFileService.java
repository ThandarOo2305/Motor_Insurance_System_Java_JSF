package org.ace.accounting.system.formatfile.service.interfaces;

import java.util.List;

import org.ace.accounting.dto.ReportFormatDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.system.formatfile.FormatFile;

public interface IFormatFileService {
	public List<FormatFile> findAllFormatFile();

	public List<ReportFormatDto> findByReportType(ReportType reportType);

	public List<FormatFile> findByTypeAndName(String formatType, String formatName);

	public void updateFormatFile(FormatFile newFF, FormatFile oldFF, List<FormatFile> ffList);

	public void insert(FormatFile ff, List<FormatFile> ffList);

	public void delete(ReportFormatDto dto);
	
	public void deleteRange(List<FormatFile> ffList);
}