package org.isite.commons.lang.file;

import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.file.FileUtils.EXTENSION_XLS;
import static org.isite.commons.lang.file.FileUtils.getExtension;
import static org.isite.commons.lang.utils.IoUtils.close;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ExcelAccessor {
	@Getter
	private final Workbook workbook;
	private final InputStream input;

	public ExcelAccessor(InputStream input) throws IOException {
		this.input = input;
		this.workbook = new XSSFWorkbook(input);
	}

	public ExcelAccessor(String pathname) throws IOException {
		this.input = new FileInputStream(pathname);
		this.workbook = EXTENSION_XLS.equals(getExtension(pathname)) ?
				new HSSFWorkbook(input) : new XSSFWorkbook(input);
	}

	/**
	 * 读取所有工作表
	 */
	public List<Sheet> sheets() {
		List<Sheet> sheets = new ArrayList<>();
		for (int i = ZERO; i < workbook.getNumberOfSheets(); i++) {
			if (null != workbook.getSheetAt(i)) {
				sheets.add(workbook.getSheetAt(i));
			}
		}
		return sheets;
	}

	/**
	 * 返回工作表中的所有行
	 */
	public List<Row> rows(Sheet sheet) {
		List<Row> rows = new ArrayList<>();
		for (int i = ZERO; i <= sheet.getLastRowNum(); i++) {
			if (null == sheet.getRow(i)) {
				continue;
			}
			rows.add(sheet.getRow(i));
		}
		return rows;
	}

	/**
	 * 返回单元格值
	 */
	public String value(Cell cell) {
		if (null == cell) {
			return null;
		}
		switch (cell.getCellType()) {
			case BOOLEAN: return cell.getBooleanCellValue() + BLANK_STR;
			//返回不带指数字段的字符串表示形式
			case NUMERIC: return valueOf(cell.getNumericCellValue()).toPlainString();
			case STRING: return cell.getStringCellValue();
			case FORMULA: {
				RichTextString text = cell.getRichStringCellValue();
				return null == text ? null : text.getString();
			}
			default: return null;
		}
	}

	/**
	 * 将Excel文件写入到输出流
	 * @param output 使用完不关闭，可继续写入
	 * @throws IOException IO异常信息
	 */
	public void write(OutputStream output) throws IOException {
		try {
			workbook.write(output);
		} finally {
			close(workbook, input);
		}
	}
}
