package com.enn.energy.price.web.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * @Description TODO
 * @ClassName ExcelUtil
 * @Date 2020/10/20/020 11:24
 * @Author
 * @since jdk 1.8
 */
@Slf4j
public class ExcelUtil {


    /**
     * 导出多个sheet的excel
     *
     * @param name
     * @param mapList
     * @param response
     * @param <T>
     */
    public static <T> void exportMultisheetExcel(String name, List<Map> mapList, HttpServletResponse response)  {
        BufferedOutputStream bos = null;
        try {
            String fileName = java.net.URLEncoder.encode(name, "UTF-8");
            bos = getBufferedOutputStream(fileName, response);
            doExport(mapList, bos);
        } catch (Exception e) {
            log.error("create excel error:",e);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从excel中读内容
     *
     * @param sheetIndex
     * @return
     */
    public static ResponseVo importExcel(MultipartFile file, Integer sheetIndex) {
        Set<Map<String, Object>> dataList = new HashSet<>();
        try {

            String fileName = file.getOriginalFilename();

            boolean isExcel2003 = true;
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            } else {
                wb = new XSSFWorkbook(file.getInputStream());
            }

            if (wb != null) {
                Sheet sheet = wb.getSheetAt(sheetIndex);
                int maxRownum = sheet.getPhysicalNumberOfRows();
                Row firstRow = sheet.getRow(0);
                if (null == firstRow) {
                    return ResponseVo.error("请勿修改模板第一行!");
                }
                int maxColnum = firstRow.getPhysicalNumberOfCells();
                String columns[] = new String[maxColnum];
                for (int i = 0; i < maxRownum; i++) {
                    Map<String, Object> map = null;
                    if (i > 0) {
                        map = new LinkedHashMap<>();
                        firstRow = sheet.getRow(i);
                    }
                    if (firstRow != null) {
                        String cellData = null;
                        for (int j = 0; j < maxColnum; j++) {
                            cellData = ExcelUtil.getStringValueFromCell(firstRow.getCell(j));
                            if (i == 0) {
                                columns[j] = cellData;
                            } else {
                                map.put(columns[j], cellData);
                            }
                        }
                    } else {
                        break;
                    }
                    if (i > 0) {
                        dataList.add(map);
                    }
                }
                return ResponseVo.success(dataList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseVo.success();
    }

    private static BufferedOutputStream getBufferedOutputStream(String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName + ".xls");
        return new BufferedOutputStream(response.getOutputStream());
    }

    private static <T> void doExport(List<Map> mapList, OutputStream outputStream) {
        int maxBuff = 100;
        // 创建excel工作文本，100表示默认允许保存在内存中的行数
        SXSSFWorkbook wb = new SXSSFWorkbook(maxBuff);
        try {
            for (int i = 0; i < mapList.size(); i++) {
                Map map = mapList.get(i);
                String[] headers = (String[]) map.get("headers");
                Collection<T> dataList = (Collection<T>) map.get("dataList");
                String fileName = (String) map.get("fileName");
                String[] exportFields =  (String[]) map.get("exportFields");
                String[] convertMethods =  (String[]) map.get("converts");
                createSheet(wb, exportFields, convertMethods,headers, dataList, fileName, maxBuff);
            }

            if (outputStream != null) {
                wb.write(outputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static <T> void createSheet(SXSSFWorkbook wb, String[] exportFields,String[] convertMethods, String[] headers, Collection<T> dataList, String fileName, int maxBuff) throws NoSuchFieldException, IllegalAccessException, IOException {

        Sheet sh = wb.createSheet(fileName);

        CellStyle style = wb.createCellStyle();
        CellStyle style2 = wb.createCellStyle();
        //创建表头
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        style.setFont(font);//选择需要用到的字体格式

        style.setFillForegroundColor(HSSFColor.YELLOW.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style2.setFont(font);//选择需要用到的字体格式

        style2.setFillForegroundColor(HSSFColor.WHITE.index);// 设置背景色
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平向下居中
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框

        Row headerRow = sh.createRow(0); //表头

        int headerSize = headers.length;
        for (int cellnum = 0; cellnum < headerSize; cellnum++) {
            Cell cell = headerRow.createCell(cellnum);
            cell.setCellStyle(style);
            sh.setColumnWidth(cellnum, 4000);
            cell.setCellValue(headers[cellnum]);
        }

        int rownum = 0;
        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            T data = iterator.next();
            Row row = sh.createRow(rownum + 1);

            Field[] fields = getExportFields(data.getClass(), exportFields);
            for (int cellnum = 0; cellnum < headerSize; cellnum++) {
                Cell cell = row.createCell(cellnum);
                cell.setCellStyle(style2);
                Field field = fields[cellnum];

                field.setAccessible(true); //允许访问private属性
                Object val = field.get(data); //获取属性值

                if(convertMethods[cellnum] != null){
                    String methodName = convertMethods[cellnum];
                    try {
                        Method method = data.getClass().getMethod(methodName,null);
                        val = method.invoke(data,null);
                    } catch (NoSuchMethodException e) {
                        log.error("NoSuchMethodException：",e);
                    } catch (InvocationTargetException e) {
                        log.error("InvocationTargetException：",e);
                    }
                }



                setData(field, val, field.getName(), cell);
            }
            rownum = sh.getLastRowNum();
            // 大数据量时将之前的数据保存到硬盘
            if (rownum % maxBuff == 0) {
                ((SXSSFSheet) sh).flushRows(maxBuff); // 超过100行后将之前的数据刷新到硬盘

            }
        }
    }


    private static <T> void doExport(String[] headers, String[] exportFields, Collection<T> dataList,
                                     String fileName, OutputStream outputStream) {

        int maxBuff = 100;
        // 创建excel工作文本，100表示默认允许保存在内存中的行数
        SXSSFWorkbook wb = new SXSSFWorkbook(maxBuff);
        try {
            createSheet(wb, exportFields,null, headers, dataList, fileName, maxBuff);
            if (outputStream != null) {
                wb.write(outputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取单条数据的属性
     *
     * @param object
     * @param property
     * @param <T>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static <T> Field getDataField(T object, String property) throws NoSuchFieldException, IllegalAccessException {
        Field dataField;
        if (property.contains(".")) {
            String p = property.substring(0, property.indexOf("."));
            dataField = object.getClass().getDeclaredField(p);
            return dataField;
        } else {
            dataField = object.getClass().getDeclaredField(property);
        }
        return dataField;
    }

    private static Field[] getExportFields(Class<?> targetClass, String[] exportFieldNames) {
        Field[] fields = null;
        if (exportFieldNames == null || exportFieldNames.length < 1) {
            fields = targetClass.getDeclaredFields();
        } else {
            fields = new Field[exportFieldNames.length];
            for (int i = 0; i < exportFieldNames.length; i++) {
                try {
                    fields[i] = targetClass.getDeclaredField(exportFieldNames[i]);
                } catch (Exception e) {
                    try {
                        fields[i] = targetClass.getSuperclass().getDeclaredField(exportFieldNames[i]);
                    } catch (Exception e1) {
                        throw new IllegalArgumentException("无法获取导出字段", e);
                    }

                }
            }
        }
        return fields;
    }

    /**
     * 根据属性设置对应的属性值
     *
     * @param dataField 属性
     * @param val    数据对象
     * @param property  表头的属性映射
     * @param cell      单元格
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private static <T> void setData(Field dataField, T val, String property, Cell cell)
            throws IllegalAccessException, NoSuchFieldException {

        Sheet sh = cell.getSheet(); //获取excel工作区
        CellStyle style = cell.getCellStyle(); //获取单元格样式
        int cellnum = cell.getColumnIndex();
        if (val != null) {
            if (val.getClass().toString().endsWith("String")) {
                cell.setCellValue((String) val);
            } else if (val.getClass().toString().endsWith("Integer") || dataField.getType().toString().endsWith("int")) {
                cell.setCellValue((Integer) val);
            } else if (val.getClass().toString().endsWith("Long") || dataField.getType().toString().endsWith("long")) {
                cell.setCellValue(val.toString());
            } else if (val.getClass().toString().endsWith("Double") || dataField.getType().toString().endsWith("double")) {
                cell.setCellValue((Double) val);
            }else if(val.getClass().toString().endsWith("LocalDate")){
                Date d = localDate2Date((LocalDate) val);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                cell.setCellValue(format.format( d));
            }else if(val.getClass().toString().endsWith("LocalDateTime")){
                Date d = localDateTime2Date((LocalDateTime) val);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cell.setCellValue(format.format( d));
            } else if (val.getClass().toString().endsWith("Date")) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cell.setCellValue(format.format((Date) val));
            } else if (val.getClass().toString().endsWith("List")) {
                List list1 = (List) val;
                int size = list1.size();
                for (int i = 0; i < size; i++) {
                    //加1是因为要去掉点号
                    int start = property.indexOf(dataField.getName()) + dataField.getName().length() + 1;
                    String tempProperty = property.substring(start, property.length());
                    Field field = getDataField(list1.get(i), tempProperty);
                    Cell tempCell = cell;
                    if (i > 0) {
                        int rowNum = cell.getRowIndex() + i;
                        Row row = sh.getRow(rowNum);
                        if (row == null) {//另起一行
                            row = sh.createRow(rowNum);
                            //合并之前的空白单元格（在这里需要在header中按照顺序把list类型的字段放到最后，方便显示和合并单元格）
                            for (int j = 0; j < cell.getColumnIndex(); j++) {
                                sh.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + size - 1, j, j));
                                Cell c = row.createCell(j);
                                c.setCellStyle(style);
                            }
                        }
                        tempCell = row.createCell(cellnum);
                        tempCell.setCellStyle(style);
                    }
                    //递归传参到单元格并获取偏移量（这里获取到的偏移量都是第二层后list的偏移量）
                    setData(field, list1.get(i), tempProperty, tempCell);
                }
            } else {
                if (property.contains(".")) {
                    String p = property.substring(property.indexOf(".") + 1, property.length());
                    Field field = getDataField(val, p);
                    setData(field, val, p, cell);
                } else {
                    cell.setCellValue(val.toString());
                }
            }
        }
    }


    private static Workbook createWorkBook(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }


    private static String getStringValueFromCell(Cell cell) {
        SimpleDateFormat sFormat = new SimpleDateFormat("MM/dd/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            cellValue = cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                double d = cell.getNumericCellValue();
                Date date = HSSFDateUtil.getJavaDate(d);
                cellValue = sFormat.format(date);
            } else {
                cellValue = decimalFormat.format((cell.getNumericCellValue()));
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            cellValue = "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            cellValue = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            cellValue = "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            cellValue = cell.getCellFormula().toString();
        }
        return cellValue;
    }


    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());

        return date;
    }

    /**
     * Date转换为LocalDateTime
     * @param date
     */
    public static LocalDateTime date2LocalDateTime( Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        return localDateTime;
    }


    public static Date localDate2Date(LocalDate localDate){

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);

        return date;
    }
}
