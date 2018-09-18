package com.velcro.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.velcro.base.BaseContext;
import com.velcro.base.DataService;
import com.velcro.base.TitleTypePair;
import com.velcro.base.setitem.service.SetitemService;
import com.velcro.document.base.model.Attach;
import com.velcro.document.base.service.AttachService;

public class ExcelHelper {
	protected final Log logger = LogFactory.getLog(getClass());
	private int cellNum = 0;

	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}

	/**
	 * 读取EXCEL文件:excel文件第一行为标题，第二行为标题对应的数据库字段名;在文件的最后一行加end标记;
	 * 
	 * @param filepath
	 */
	public List<Map> readExcel(String filepath, String filetype) throws IOException {
		List<Map> list = new ArrayList();

		File xlsfile = new File(filepath);
		Workbook workbook = null;
		if (xlsfile.exists()) {
			InputStream fIn;
			try {
				FileReader fr = new FileReader(xlsfile);
				fIn = new FileInputStream(xlsfile);
				workbook = WorkbookFactory.create(fIn);
				int sheetNum = workbook.getNumberOfSheets();// 获取sheet数
				Map objmap = null;
				// for(int i=0;i < sheetNum; i++){
				Sheet sheet = (Sheet) workbook.getSheetAt(0);
				sheet.getSheetName();
				if (null != sheet) {
					int fristrow = sheet.getFirstRowNum();
					int rowNum = sheet.getLastRowNum();
					this.setCellNum(rowNum);
					int j = 1;
					Row row = sheet.getRow(j);
					String[] fieldnames = this.getRowvalues(row);// 用于存入系列对应的数据表中的字段名.

					for (j = 2; j <= rowNum; j++) {
						try {
							row = sheet.getRow(j);
							if (row != null) {
								String[] cellvalues = this.getRowvalues(row);
								String linevalue = "";
								for (int k = 0; k < cellvalues.length; k++) {
									if ("end".equalsIgnoreCase(cellvalues[k])) {
										return list;
									}
									linevalue += cellvalues[k];
								}
								if(StringHelper.isEmpty(linevalue)){
									return list;
								}
								objmap = this.getObject(fieldnames, cellvalues);
								if (objmap != null) {
									list.add(objmap);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.error("the file not exists......................");
		}

		try {
			workbook = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map getObject(String[] fieldnames, String[] cellvalues) {
		Map _map = new LinkedHashMap();
		if (fieldnames == null) {
			return _map;
		}
		StringBuffer failwhys = new StringBuffer();
		if (_map != null) {
			int _colsize = fieldnames.length;
			for (int i = 0; i < _colsize; i++) {
				_map.put(fieldnames[i], StringHelper.null2String(cellvalues[i]));
			}
		}
		return _map;
	}

	/**
	 * 取excel中一行的值
	 * 
	 * @param row
	 * @return
	 */
	public String[] getRowvalues(Row row) {
		String[] cellvalues = null;
		if (null != row) {
			int fristcell = row.getFirstCellNum();
			int cellNum2 = row.getLastCellNum();
			cellNum = this.cellNum;
			if (cellNum2 > cellNum) {
				cellNum = cellNum2;
			}
			cellvalues = new String[cellNum];
			for (int k = 0; k < cellNum; k++) {
				Cell cell = row.getCell(k);
				if (null != cell) {

					int celltype = cell.getCellType();
					String strcell = "";
					switch (celltype) {
						case Cell.CELL_TYPE_NUMERIC :
							//数值型
//							strcell = String.valueOf(cell.getNumericCellValue());
//							strcell = this.getValue(cell);
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								// 如果是date类型则 ，获取该cell的date值
								Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
								strcell = format.format(date);
							} else {// 纯数字
								BigDecimal big = new BigDecimal(cell.getNumericCellValue());
								big =  big.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入保留2位小数
								strcell = big.toString();
								// 解决1234.0 去掉后面的.0
								if (null != strcell && !"".equals(strcell.trim())) {
									String[] item = strcell.split("[.]");
									if (1 < item.length && ("0".equals(item[1]) || "00".equals(item[1]))) {//处理四舍五入后保留2位小数时带00的情况
										strcell = item[0];
									}
								}
							}
							break;
						case Cell.CELL_TYPE_STRING :
							//字符串类型
							strcell = cell.getStringCellValue();
							break;
						
						case Cell.CELL_TYPE_FORMULA :
							// 公式类型  
							try {
								strcell = String.valueOf(cell.getNumericCellValue());
								 if (HSSFDateUtil.isCellDateFormatted(cell)){
									Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()); 
									SimpleDateFormat dformat=new SimpleDateFormat("yyyy-MM-dd");
									strcell=dformat.format(date);
								 }
							} catch (Exception e) {
								strcell = String.valueOf(cell.getCellFormula());
								
								FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
								evaluator.evaluateFormulaCell(cell);
								org.apache.poi.ss.usermodel.CellValue cellValue = evaluator.evaluate(cell);
								strcell = cellValue.getStringValue();
								//e.printStackTrace();
							}
							break;
						
						case Cell.CELL_TYPE_BOOLEAN:
							// 布尔类型  
							strcell = " "+ cell.getBooleanCellValue();
							break;
						
						case Cell.CELL_TYPE_BLANK :
							// 空值  
							strcell = "";  
							break;
						
						case Cell.CELL_TYPE_ERROR:
							// 错误
							strcell = "";
						default :
							strcell = cell.getStringCellValue().toString();
					}
					if (StringHelper.isEmpty(strcell) || strcell.trim().equals("null")) {
						strcell = "";
					}
					if (strcell.endsWith(".0")) {
						strcell = strcell.substring(0, strcell.indexOf(".0"));
					}
					cellvalues[k] = strcell.trim();
				} else {
					cellvalues[k] = "";
				}
			}
		}
		return cellvalues;
	}

	/**
	 * 
	 * @param sql
	 * @param xlsName
	 * @param titleMap
	 * @param conMap
	 * @throws Exception
	 */
	public void createExcel(String[] sql, String xlsName, Map titleMap, Map conMap) throws Exception {
		String filedir = xlsName.substring(0, xlsName.lastIndexOf("\\"));
		File xlsfiledir = new File(filedir);
		if (!xlsfiledir.exists()) {
			xlsfiledir.mkdir();
		}

		DataService dataService = (DataService) BaseContext.getBean("dataService");
		File xlsfile = new File(xlsName);
		Workbook workbook = null;
		if (!xlsfile.exists()) {
			xlsfile.createNewFile();
		}
		try {
			InputStream fIn = new FileInputStream(BaseContext.getRootPath() + File.separatorChar + "\\srtemplate\\adsinfodownload.xls");
			workbook = WorkbookFactory.create(fIn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int k = 0; k < sql.length; k++) {
			if (StringHelper.isEmpty(sql[k]))
				break;
			List<Map> list = dataService.getValues(sql[k]);
			Sheet sheet = workbook.getSheetAt(0);// workbook.createSheet();

			Row row = sheet.createRow(0);
			Cell cell;

			Iterator conit = conMap.keySet().iterator();
			while (conit.hasNext()) {
				String key = (String) conit.next();
				if ("广告开始日期".contains(key)) {
					String value = StringHelper.null2String(conMap.get(key));
					cell = row.createCell(2);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(key + "：");

					cell = row.createCell(3);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(value);
				}
			}
			row = sheet.createRow(1);
			Iterator conit2 = conMap.keySet().iterator();
			while (conit2.hasNext()) {
				String key = (String) conit2.next();
				if ("广告结束日期".contains(key)) {
					String value = StringHelper.null2String(conMap.get(key));
					cell = row.createCell(2);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(key + "：");

					cell = row.createCell(3);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(value);
				}
			}
			row = sheet.createRow(2);
			Iterator conit3 = conMap.keySet().iterator();
			while (conit3.hasNext()) {
				String key = (String) conit3.next();
				if ("媒体".contains(key)) {
					String value = StringHelper.null2String(conMap.get(key));
					cell = row.createCell(0);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(key + "：");

					cell = row.createCell(1);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(value);
				}
			}
			row = sheet.createRow(3);
			Iterator titleit = titleMap.keySet().iterator();
			int j = 0;
			while (titleit.hasNext()) {
				String key = (String) titleit.next();
				String value = StringHelper.null2String(titleMap.get(key));
				cell = row.createCell(j);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(value);
				j++;
			}
			int iRow = 4;
			// 写入各条记录，每条记录对应Excel中的一行
			for (Map tmap : list) {
				row = sheet.createRow(iRow);
				Iterator it = tmap.keySet().iterator();
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = StringHelper.null2String(tmap.get(key));
					cell = row.createCell(i);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(value);
					i++;
				}
				iRow++;
			}
		}
		FileOutputStream fileOut = new FileOutputStream(xlsfile);
		workbook.write(fileOut);
		fileOut.close();
	}

	/*
	 * 把数据集rs中的数据导出至Excel工作表中。
	 * 
	 * 传入参数：sql，Excel文件名称xlsName，工作表名称sheetName。
	 */
	public void createExcel(String[] sql, String xlsName, Map titleMap) throws Exception {
		DataService dataService = (DataService) BaseContext.getBean("dataService");
		File xlsfile = new File(xlsName);
		Workbook workbook = null;
		if (!xlsfile.exists()) {
			xlsfile.createNewFile();
		}
		try {
			workbook = new HSSFWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map> list = null;
		for (int k = 0; k < sql.length; k++) {
			if (StringHelper.isEmpty(sql[k]))
				break;
			list = dataService.getValues(sql[k]);
			Sheet sheet = workbook.createSheet();
			Row row = sheet.createRow(0);
			Cell cell;
			Iterator titleit = titleMap.keySet().iterator();
			int j = 0;
			while (titleit.hasNext()) {
				String key = (String) titleit.next();
				String value = StringHelper.null2String(titleMap.get(key));
				cell = row.createCell(j);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(value);
				j++;
			}
			int iRow = 1;
			// 写入各条记录，每条记录对应Excel中的一行
			for (Map tmap : list) {
				row = sheet.createRow(iRow);
				Iterator it = tmap.keySet().iterator();
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = StringHelper.null2String(tmap.get(key));
					cell = row.createCell(i);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(value);
					i++;
				}
				iRow++;
			}
		}
		FileOutputStream fileOut = new FileOutputStream(xlsfile);
		workbook.write(fileOut);
		list.clear();
		fileOut.close();
	}

	/**
	 * 通过sql将数据存在指定的excel模板中,生成附件,返回附件id
	 * 
	 * @param sql
	 *            ,数组,对应多个sheet
	 * @param xlsName
	 *            excel模板文件名,对应的文件存在srtemplate下,必须是xls格式
	 * @param iRow
	 *            ,指定从第几行开始写数据
	 * @return 附件id
	 * @throws IOException 
	 * @throws Exception
	 */
	public String createExcelBySql(String[] sql, String xlsName, int[] Row) throws IOException {
		DataService dataService = (DataService) BaseContext.getBean("dataService");
		SetitemService setitemService = (SetitemService) BaseContext.getBean("setitemService");
		AttachService attachService = (AttachService) BaseContext.getBean("attachService");
		String fileRootPath = setitemService.getSetitem("402881e80b7544bb010b754c7cd8000a").getItemvalue();// 服务器目录
		String uploadDir = "vfiles";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		String date = sf.format(new Date());
		char letter = (char) (Math.round((Math.random() * 100)) % 26 + (int) ('A'));
		String filePathName = fileRootPath + uploadDir + "/" + date + "/" + letter;
		File filePath = new File(filePathName);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		
		//如果配置中有文件类型
		if(!StringHelper.isEmpty(xlsName)){
			if(xlsName.indexOf(".") > 0){
				String xlslist[] = xlsName.split(".");
				xlsName = xlslist[0];
			}
		}

		if (StringHelper.isEmpty(xlsName))
			xlsName = "default";//不要文件类型

		String xlsName2 = xlsName + StringHelper.replaceString(DateHelper.getCurrentDate(), "-", "") + "_" + StringHelper.replaceString(DateHelper.getCurrentTime(), ":", "") + ".xlsx";
		File xlsfile = new File(filePath, xlsName2);

		Workbook workbook = null;
		if (!xlsfile.exists()) {
			xlsfile.createNewFile();
		}
		try {
			InputStream fIn = new FileInputStream(BaseContext.getRootPath() + File.separatorChar + "\\srtemplate\\" + xlsName + ".xlsx");
			workbook = WorkbookFactory.create(fIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map> list = null;
		for (int k = 0; k < sql.length; k++) {
			if (StringHelper.isEmpty(sql[k]))
				break;
			list = dataService.getValues(sql[k]);
			Sheet sheet = workbook.getSheetAt(k);
			Row row;
			Cell cell;
			int iRow = 0;
			if (Row[k] > 0)
				iRow = Row[k];
			// 写入各条记录，每条记录对应Excel中的一行
			for (Map tmap : list) {
				row = sheet.createRow(iRow);
				Iterator it = tmap.keySet().iterator();
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = StringHelper.null2String(tmap.get(key));
					cell = row.createCell(i);
					cell.setCellValue(value);
					i++;
				}
				iRow++;
			}
		}
		FileOutputStream fileOut = new FileOutputStream(xlsfile);
		workbook.write(fileOut);
		list.clear();
		fileOut.close();

		Attach attach = new Attach();
		attach.setObjname(xlsName2);
		attach.setFiletype("application/octet-stream");
		attach.setFiledir(xlsfile.getAbsolutePath());
		attach.setIszip(0);
		attach.setIsencrypt(0);
		attachService.createAttach(attach);
		return attach.getId();
	}

	public Map<String, TitleTypePair> getXlsTemplateConfig(String xlsTemplate) {
		String filepath = BaseContext.getRootPath() + "srtemplate" + "/" + xlsTemplate;

		File xlsfile = new File(filepath);
		Workbook workbook = null;
		if (!xlsfile.exists()) {
			throw new IllegalArgumentException("the template file " + xlsTemplate + "is notexist..");
		}
		// 1、读取指定位置的模板
		InputStream fIn;
		try {
			fIn = new FileInputStream(xlsfile);
			workbook = WorkbookFactory.create(fIn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Sheet sheet = (Sheet) workbook.getSheetAt(0);
		int firstRowIdx = 0;
		// 2、获取第一行数据的表头名称和类型（该列数据的类型
		Map<String, TitleTypePair> pairMap = new LinkedHashMap<String, TitleTypePair>();
		if (null != sheet) {
			Row row = sheet.getRow(firstRowIdx);
			int fristcell = row.getFirstCellNum();
			int cellNum2 = row.getLastCellNum();
			cellNum = this.cellNum;
			if (cellNum2 > cellNum) {
				cellNum = cellNum2;
			}
			for (int k = 0; k < cellNum; k++) {
				Cell cell = row.getCell(k);
				TitleTypePair pair = new TitleTypePair();
				String title = "";
				int type;
				if (null != cell) {
					int cellType = cell.getCellType();
					pair.setType(cellType);
					switch (cellType) {
						case Cell.CELL_TYPE_NUMERIC :
							title = String.valueOf(cell.getNumericCellValue());
							break;
						case Cell.CELL_TYPE_STRING :
							title = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_FORMULA :
							// title =
							// String.valueOf(cell.getNumericCellValue());
							title = String.valueOf(cell.getCellFormula());
							break;

						case Cell.CELL_TYPE_BLANK :
							title = cell.getStringCellValue();

						default :
							break;
					}
					pair.setTitle(title);
				}
				pairMap.put(String.valueOf(k), pair);
			}
		}
		return pairMap;

	}

	public ResultSet getRS(String sql) {
		DataService dataService = (DataService) BaseContext.getBean("dataService");
		DataSource ds = dataService.getBaseJdbcDao().getDataSource();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Connection con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

		}
		return rs;
	}

	/**
	 * 根据提供的模板生成新的excel
	 * 
	 * @param datalist
	 * @param tempfileName
	 * @param newxlsfileName
	 * @param titleMap
	 * @throws Exception
	 */
	public void createExcel(List datalist, String tempfileName, String newxlsfileName, Map titleMap) throws Exception {
		//DataService dataService = (DataService) BaseContext.getBean("dataService");
		File xlsfile = new File(tempfileName);
		Workbook workbook = null;
		if (xlsfile.exists()) {
			InputStream fIn;
			try {
				FileReader fr = new FileReader(xlsfile);
				fIn = new FileInputStream(xlsfile);
				workbook = WorkbookFactory.create(fIn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File newxlsfile = new File(newxlsfileName);
		if (!newxlsfile.exists()) {
			newxlsfile.createNewFile();
		}
		List<Map> list = datalist;
		// Sheet sheet = workbook.createSheet();
		// Row row= sheet.createRow(0);
		// Cell cell;
		// Iterator titleit = titleMap.keySet().iterator();
		// int j=0;
		// while(titleit.hasNext()){
		// String key = (String)titleit.next();
		// String value = StringHelper.null2String(titleMap.get(key));
		// cell = row.createCell(j);
		// cell.setCellType(Cell.CELL_TYPE_STRING);
		// cell.setCellValue(value);
		// j++;
		// }
		Sheet sheet = (Sheet) workbook.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short) 10);//设置字体大小
		cellStyle.setFont(font);
		int iRow = 1;
		// 写入各条记录，每条记录对应Excel中的一行
		for (Map tmap : list) {
			row = sheet.createRow( iRow);
			Iterator it = tmap.keySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				String key = (String) it.next();
				Object oValue = tmap.get(key);
				String value = StringHelper.null2String(oValue);
				
				cell = row.createCell(i);
				String type = "";
				if(!StringHelper.isEmpty(value)){
					type = oValue.getClass().getName().toLowerCase();
				}
				if(type.contains("bigdecimal")){//检测到数据类型为java.math.BigDecimal时设置为NUMRIC格式
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.parseDouble(value));
				}else {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(value);
				}
				cell.setCellStyle(cellStyle);
				i++;
			}
			iRow++;
		}
		FileOutputStream fileOut = new FileOutputStream(newxlsfile);
		workbook.write(fileOut);
		list.clear();
		fileOut.close();
		try {
			workbook = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getvalueByrowcell(Sheet sheet, int rownum, int cellnum) {
		if (sheet == null)
			return "";
		try {
			Row row = sheet.getRow(rownum);
			if (row != null) {
				Cell cell = row.getCell(cellnum);
				if (null != cell) {
					int celltype = cell.getCellType();
					String strcell = "";
					switch (celltype) {
						case Cell.CELL_TYPE_NUMERIC :
							strcell = String.valueOf(cell.getNumericCellValue());
							break;
						case Cell.CELL_TYPE_STRING :
							strcell = cell.getStringCellValue();
							break;

						case Cell.CELL_TYPE_FORMULA :
							try {
								strcell = String.valueOf(cell.getNumericCellValue());
							} catch (Exception e) {
								strcell = String.valueOf(cell.getCellFormula());
							}
							break;

						case Cell.CELL_TYPE_BLANK :
							strcell = cell.getStringCellValue();

						default :

					}
					if (StringHelper.isEmpty(strcell) || strcell.trim().equals("null")) {
						strcell = "";
					}
					if (strcell.endsWith(".0")) {
						strcell = strcell.substring(0, strcell.indexOf(".0"));
					}
					return strcell.trim();
				} else {
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 设置指定单元格的值
	 * @param sheet
	 * @param rownum
	 * @param cellnum
	 * @param value
	 */
	public void setvalueByrowcell(Sheet sheet, int rownum, int cellnum, String value, boolean ischange, CellStyle cellStyle) {
		if (sheet == null)
			return;
		try {
			Row row = sheet.getRow(rownum);
			if(row == null){
				row = sheet.createRow(rownum);
			}
			Cell cell = row.getCell(cellnum);
			if(null == cell) {
				cell = row.createCell(cellnum);
			}
			//设置单元格值
			cell.setCellValue(value);
			//如果有变化标黄
			if(ischange)
				cell.setCellStyle(cellStyle);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * 删除Excel中存在名称包含sheetname的sheet表
	 * @param workbook
	 * @param sheetName
	 * @return
	 */
	public Workbook deleteSheet(Workbook workbook, String sheetName) {
		Sheet sheet = null;
		int size = workbook.getNumberOfSheets();
		for (int i = size-1; i > 0 ; i--) {// 获取每个Sheet表
			sheet = workbook.getSheetAt(i);
			String wbSheetName = sheet.getSheetName();
			if(wbSheetName.contains(sheetName)){
				workbook.removeSheetAt(i); 
			}
		}
		return workbook;
	}
	
	/**
	 * POI读取excel按数据类型获得数值
	 * @param cell
	 * @return
	 */
	public String getValue(Cell cell) {
		String value = "";
		if (null == cell) {
			return value;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			// 数值型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				// 如果是date类型则 ，获取该cell的date值
				Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//日期格式化为yyyy-MM-dd
				value = format.format(date);
			} else {// 纯数字
				BigDecimal big = new BigDecimal(cell.getNumericCellValue());
				big =  big.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入保留2位小数
				value = big.toString();
				// 解决1234.0 去掉后面的.0
				if (null != value && !"".equals(value.trim())) {
					String[] item = value.split("[.]");
					if (1 < item.length && "0".equals(item[1])) {
						value = item[0];
					}
				}
			}
			break;
		case Cell.CELL_TYPE_STRING:
			// 字符串类型
			value = cell.getStringCellValue().toString();
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 公式类型
			// 读公式计算值
			value = String.valueOf(cell.getNumericCellValue());
			if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
				value = cell.getStringCellValue().toString();
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			// 布尔类型
			value = " " + cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			// 空值
			value = "";
			break;
		case Cell.CELL_TYPE_ERROR:
			// 错误
			value = "";
			break;
		default:
			value = cell.getStringCellValue().toString();
		}
		if ("null".endsWith(value.trim())) {
			value = "";
		}
		return value;
	}
	
	
	public static void main(String[] args) {
		ExcelHelper eh = new ExcelHelper();
		try {
			//测试读
		  /*List<Map> list = eh.readExcel("F:\\TEMP\\ztgametest.xlsx", "xlsx");
			eh.setCellNum(40);
			for (Map _map : list) {
				String a1 = (String) _map.get("field001");
				String a2 = (String) _map.get("field003");
				String a3 = (String) _map.get("field005");
				System.out.println(a1 + " | " + a2 + " | " + a3);
			}*/
			
			//测试写
			String tempfileName = "C:\\default.xlsx";
			String newxlsfileName = "C:\\NEW.xlsx";
			List datalist = new ArrayList();
			HashMap data = new HashMap();
			data.put("test", "11111");
			datalist.add(data);
			Map titleMap = new HashMap();
			
			eh.createExcel(datalist, tempfileName, newxlsfileName, titleMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
