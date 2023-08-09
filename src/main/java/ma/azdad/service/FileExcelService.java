package ma.azdad.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Component;

@Component
public class FileExcelService {
	final static String format = "yyyy-MM-dd";
	final static DateFormat df = new SimpleDateFormat(format);

	public static List<Object[]> readExcelFile(InputStream inputStream, ExcelColumnType... columnTypeTab) throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();

		POIFSFileSystem fs;
		try {
			fs = new POIFSFileSystem(inputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row;
			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();
			int cols = 0; // No of columns
			int tmp = 0;
			for (int i = 0; i < 10 || i < rows; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					tmp = sheet.getRow(i).getPhysicalNumberOfCells();
					if (tmp > cols)
						cols = tmp;
				}
			}
			if (cols != columnTypeTab.length)
				throw new Exception("number of columns should be " + columnTypeTab.length + " with this order (" + Arrays.toString(columnTypeTab) + ")");
			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					Object[] tab = new Object[columnTypeTab.length];
					for (int i = 0; i < columnTypeTab.length; i++) {
						switch (columnTypeTab[i]) {
						case STRING:
							tab[i] = getStringFromCell(row.getCell(i));
							break;
						case NUMERIC:
							tab[i] = getNumericFromCell(row.getCell(i));
							break;
						case DATE:
							tab[i] = getDateFromCell(row.getCell(i));
							break;
						default:
							break;
						}
					}
					result.add(tab);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String getStringFromCell(HSSFCell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case HSSFCell.CELL_TYPE_NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		default:
			return null;
		}
	}

	private static Double getNumericFromCell(HSSFCell cell) throws Exception {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			try {
				return Double.valueOf(cell.getStringCellValue().trim().replace(",", ".").replace("\\s", ""));
			} catch (NumberFormatException e) {
				throw new Exception("can not convert " + cell.getStringCellValue() + " to number");
			}
		case HSSFCell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		default:
			return null;
		}
	}

	private static Date getDateFromCell(HSSFCell cell) throws Exception {
		if (cell == null)
			return null;

		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			try {
				return df.parse(cell.getStringCellValue());
			} catch (NumberFormatException e) {
				throw new Exception("can not convert " + cell.getStringCellValue() + " to date, format should be : " + format);
			}
		case HSSFCell.CELL_TYPE_NUMERIC:
			return cell.getDateCellValue();
		default:
			return null;
		}
	}

	public static enum ExcelColumnType {
		STRING, NUMERIC, DATE
	}

}
