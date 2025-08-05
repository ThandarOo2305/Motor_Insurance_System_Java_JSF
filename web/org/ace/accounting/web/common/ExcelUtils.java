package org.ace.accounting.web.common;

import java.io.InputStream;

import org.ace.java.web.common.ApplicationSetting;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	private final static String CURRENCY_FORMAT = "#,###.00";
	private final static String DATE_FORMAT = "dd-MM-yyyy";

	public static void fillCompanyLogo(Workbook wb, Sheet sheet, int addressColumn) {
		try {
			InputStream logoStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationSetting.getCompanyLogoFilePath());
			// InputStream addressStream =
			// Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationSetting.getCompanyAddressAdFilePath());
			byte[] logoBytes = IOUtils.toByteArray(logoStream);
			// byte[] addressBytes = IOUtils.toByteArray(addressStream);
			int logoPictureIdx = wb.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);
			// int addressPictureIdx = wb.addPicture(addressBytes,
			// Workbook.PICTURE_TYPE_PNG);
			logoStream.close();
			// addressStream.close();
			CreationHelper helper = wb.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor logoAnchor = helper.createClientAnchor();
			logoAnchor.setCol1(1);
			logoAnchor.setRow1(0);
			Picture logoPict = drawing.createPicture(logoAnchor, logoPictureIdx);
			logoPict.resize(6.5);
			ClientAnchor addressAnchor = helper.createClientAnchor();
			addressAnchor.setCol1(addressColumn);
			addressAnchor.setRow1(0);
			// Picture addressPict = drawing.createPicture(addressAnchor,
			// addressPictureIdx);
			// addressPict.resize(0.7);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static XSSFCellStyle getDefaultCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (12 * 20));
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		return cellStyle;
	}

	public static XSSFCellStyle getWrapCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		Font font = wb.createFont();
		font.setFontName("Myanmar3");
		font.setFontHeight((short) (12 * 20));
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		return cellStyle;
	}

	public static XSSFCellStyle getTextAlignRightStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		return cellStyle;
	}

	public static XSSFCellStyle getAlignCenterStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font = wb.createFont();
		font.setFontName("Myanmar3");
		font.setFontHeight((short) (12 * 20));
		font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static XSSFCellStyle getFontBoldAlignCenterStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font = wb.createFont();
		font.setFontName("Myanmar3");
		font.setFontHeight((short) (16 * 20));
		font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static XSSFCellStyle getTextCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		return cellStyle;
	}

	public static XSSFCellStyle getCurrencyCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setDataFormat(wb.createDataFormat().getFormat(CURRENCY_FORMAT));
		return cellStyle;
	}

	public static XSSFCellStyle getDateCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setDataFormat(wb.createDataFormat().getFormat(DATE_FORMAT));
		return cellStyle;
	}

	public static XSSFCellStyle getZawgyiCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontName("Zawgyi-One");
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static void setRegionBorder(int borderWidth, CellRangeAddress crAddress, Sheet sheet, Workbook workBook) {
		// RegionUtil.setBorderTop(borderWidth, crAddress, sheet, workBook);
		// RegionUtil.setBorderBottom(borderWidth, crAddress, sheet, workBook);
		// RegionUtil.setBorderRight(borderWidth, crAddress, sheet, workBook);
		// RegionUtil.setBorderLeft(borderWidth, crAddress, sheet, workBook);
		BorderStyle borderStyle = mapBorderWidthToStyle(borderWidth);
		RegionUtil.setBorderTop(BorderStyle.MEDIUM, crAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, crAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, crAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, crAddress, sheet);
	}
	
	private static BorderStyle mapBorderWidthToStyle(int borderWidth) {
        switch (borderWidth) {
            case 1:
                return BorderStyle.THIN;
            case 2:
                return BorderStyle.MEDIUM;
            case 3:
                return BorderStyle.THICK;
            // Add more cases as needed or provide a default value
            default:
                return BorderStyle.NONE; // Or any other default value
        }
    }
	
	public static XSSFCellStyle getPyiDaungSuStyleRight(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (11 * 20));
		// font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	
	public static XSSFCellStyle getPDSFontBoldAlignCenterStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (11 * 20));
		font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	public static XSSFCellStyle getPDSCurrencyFontBoldCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getPDSFontBoldAlignCenterStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setDataFormat(wb.createDataFormat().getFormat(CURRENCY_FORMAT));
		return cellStyle;
	}
	
	public static XSSFCellStyle getZawgyiCellStyleLeft(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (11 * 20));
		// font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static XSSFCellStyle getZawgyiCellStyleLeftBorder(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (11 * 20));
		// font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	public static XSSFCellStyle getPDSDateCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getPDSDefaultCellStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setDataFormat(wb.createDataFormat().getFormat(DATE_FORMAT));
		return cellStyle;
	}
	
	public static XSSFCellStyle getTextCellLeftStyleII(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		return cellStyle;
	}
	
	public static XSSFCellStyle getPDSFontBoldAlignLeftStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (12 * 20));
		font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	public static XSSFCellStyle getPyiDaungSuStyleLeft(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (11 * 20));
		// font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static XSSFCellStyle getPDSDefaultCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		;
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		;
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (14 * 20));
		cellStyle.setFont(font);
		font.setBold(true);
		cellStyle.setWrapText(true);
		return cellStyle;
	}
	
	public static void setRegionBorderThin(CellRangeAddress crAddress, Sheet sheet) {
		// RegionUtil.setBorderTop(borderWidth, crAddress, sheet, workBook);
		RegionUtil.setBorderTop(BorderStyle.THIN, crAddress, sheet);
		// RegionUtil.setBorderBottom(BorderStyle.THIN, crAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, crAddress, sheet);
		// RegionUtil.setBorderLeft(BorderStyle.THIN, crAddress, sheet);
		// RegionUtil.setBorderBottom(borderWidth, crAddress, sheet, workBook);
		// RegionUtil.setBorderRight(borderWidth, crAddress, sheet, workBook);
		// RegionUtil.setBorderLeft(borderWidth, crAddress, sheet, workBook);
	}

	
	public static XSSFCellStyle getDefaultCellStyleII(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		;
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		;
		Font font = wb.createFont();
		font.setFontName("Pyidaungsu");
		font.setFontHeight((short) (11 * 20));
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		return cellStyle;
	}
	
	public static XSSFCellStyle getTextCellLeftStyleI(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyleII(wb);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		return cellStyle;
	}
	
	public static XSSFCellStyle getCurrencyCellStyleII(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyleII(wb);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setDataFormat(wb.createDataFormat().getFormat(CURRENCY_FORMAT));
		return cellStyle;
	}
	
	public static XSSFCellStyle getTextCellStyleII(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyleII(wb);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		return cellStyle;
	}
	
	public static XSSFCellStyle getTextCellLeftStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = getDefaultCellStyleII(wb);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		return cellStyle;
	}
	
	public static XSSFCellStyle getZawgyiCellStyleRight(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		Font font = wb.createFont();
		font.setFontName("Zawgyi-One");
		font.setFontHeight((short) (9 * 20));
		font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	//Set Background Color
		public static XSSFCellStyle getBackgroundcolor(XSSFWorkbook wb) {
			XSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Font font = wb.createFont();
			font.setFontName("Pyidaungsu");
			font.setFontHeight((short) (11 * 20));
			font.setBold(true);
			cellStyle.setFont(font);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setBorderBottom(BorderStyle.THIN);;
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			
			return cellStyle;
		}


}
