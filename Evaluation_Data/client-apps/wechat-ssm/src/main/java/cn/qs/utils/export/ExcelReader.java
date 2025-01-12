package cn.qs.utils.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

	private Workbook workBook;
	private Map<Sheet, String[]> sheetHeaders;

	public ExcelReader(String filePath) {
		this(new File(filePath));
	}

	public ExcelReader(File file) {
		// 解决版本问题，HSSFWorkbook是97-03版本的xls版本，XSSFWorkbook是07版本的xlsx
		try {
			workBook = new XSSFWorkbook(new FileInputStream(file));
			LOGGER.debug("是03版本的excel，采用XSSFWorkbook读取");
		} catch (Exception e) {
			try {
				workBook = new HSSFWorkbook(new FileInputStream(file));
				LOGGER.debug("是07版本的excel，采用HSSFWorkbook读取");
			} catch (Exception e1) {
				LOGGER.error("Excel格式不正确", e1);
				throw new RuntimeException(e1);
			}
		}
	}

	public ExcelReader(InputStream inputStream) {
		// 解决版本问题，HSSFWorkbook是97-03版本的xls版本，XSSFWorkbook是07版本的xlsx
		try {
			workBook = new XSSFWorkbook(inputStream);
			LOGGER.debug("是03版本的excel，采用XSSFWorkbook读取");
		} catch (Exception e) {
			try {
				workBook = new HSSFWorkbook(inputStream);
				LOGGER.debug("是07版本的excel，采用HSSFWorkbook读取");
			} catch (Exception e1) {
				LOGGER.error("Excel格式不正确", e1);
				throw new RuntimeException(e1);
			}
		}
	}

	/**
	 * 初始化sheet和表头信息，默认以每个sheet的第一行作为表头
	 */
	private void initDefaultSheetHeaders() {
		sheetHeaders = new LinkedHashMap<>();

		if (workBook == null) {
			return;
		}

		int numberOfSheets = workBook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workBook.getSheetAt(i);
			String sheetName = workBook.getSheetName(i);
			LOGGER.debug("sheetName[{}]: {}", i, sheetName);

			// 默认以第一行作为表头
			Row row = sheet.getRow(0);
			if (row == null) {
				sheetHeaders.put(sheet, new String[] {});
				continue;
			}

			String[] headers = new String[] {};
			short lastCellNum = row.getLastCellNum();
			for (int j = 0; j < lastCellNum; j++) {
				String cellValue = getCellValue(row.getCell(j));
				headers = (String[]) ArrayUtils.add(headers, cellValue);
			}
			sheetHeaders.put(sheet, headers);
		}
	}

	public List<Map<String, Object>> readAllSheetDatas() {
		List<Map<String, Object>> result = new LinkedList<>();
		int numberOfSheets = workBook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			List<Map<String, Object>> datas = readSheetDatas(i);
			if (CollectionUtils.isNotEmpty(datas)) {
				result.addAll(datas);
			}
		}

		return result;
	}

	public List<Map<String, Object>> readSheetDatas(int sheetIndex) {
		if (sheetHeaders == null) {
			initDefaultSheetHeaders();
		}

		Sheet sheet = workBook.getSheetAt(sheetIndex);
		return readSheetDatas(sheetIndex, sheetHeaders.get(sheet), 1);
	}

	public List<Map<String, Object>> readSheetDatas(int sheetIndex, String[] headers) {
		return readSheetDatas(sheetIndex, headers, 0);
	}

	/**
	 * 读取指定sheet数据
	 * 
	 * @param sheetIndex
	 *            sheet的下标
	 * @param headers
	 *            表头
	 * @param startRow
	 *            起始行数
	 * @return
	 */
	public List<Map<String, Object>> readSheetDatas(int sheetIndex, String[] headers, int startRow) {
		List<Map<String, Object>> result = new LinkedList<>();
		if (ArrayUtils.isEmpty(headers)) {
			return result;
		}

		Sheet sheet = workBook.getSheetAt(sheetIndex);
		if (sheet == null) {
			return result;
		}

		int lastRowNum = sheet.getLastRowNum();
		for (int i = startRow; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			Map<String, Object> rowMap = new LinkedHashMap<>();
			result.add(rowMap);
			for (int j = 0, length_1 = headers.length; j < length_1; j++) {
				String cellValue = getCellStringValue(row.getCell(j));
				String header = headers[j];
				rowMap.put(header, cellValue);
			}
		}

		return result;
	}

	/**
	 * 读取cell的值
	 * 
	 * @param cell
	 *            需要读取的cell
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	private String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		cell.setCellType(CellType.STRING);
		return StringUtils.defaultIfBlank(cell.getStringCellValue(), "");
	}

	/**
	 * POI3.15之后的读取方法(建议用这个)
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellStringValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		String cellValue = null;
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				cellValue = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd");
			} else {
				NumberFormat nf = NumberFormat.getInstance();
				cellValue = String.valueOf(nf.format(cell.getNumericCellValue())).replace(",", "");
			}
		} else if (cell.getCellTypeEnum() == CellType.STRING) {
			cellValue = cell.getStringCellValue();
		} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellTypeEnum() == CellType.ERROR) {
			cellValue = "错误类型";
		} else {
			cellValue = "";
		}

		return cellValue;
	}

	public static void main(String[] args) {
		ExcelReader excelReader = new ExcelReader("e:/test1.xls");
		System.out.println("==========读取所有sheet数据，默认以第一行作为header==========");
		List<Map<String, Object>> readAllSheetDatas2 = excelReader.readAllSheetDatas();
		System.out.println(readAllSheetDatas2);
	}

}
