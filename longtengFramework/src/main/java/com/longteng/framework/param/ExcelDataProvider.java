package com.longteng.framework.param;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.longteng.framework.suite.SenarioLoader;
import com.longteng.framework.util.StringUtil;

public class ExcelDataProvider implements Iterator<Object[]> {

    public static Logger logger = Logger.getLogger(ExcelDataProvider.class);
    private static SenarioLoader loader = SenarioLoader.getInstance();
    Sheet sheet;
    int column = 0;
    int row = 0;
    int usedRow = 1;
    String[] columnName = null;

    public ExcelDataProvider(String filePath) throws FileNotFoundException {

        try {
            Workbook wb = loader.getTestCaseWorkbook(filePath);
            sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);

            // 获取第一行(列名的行)的单元格个数
            column = row.getPhysicalNumberOfCells();
            // 创建列名数组
            columnName = new String[column];

            // 给列明数组赋值,其中每一个元素就是一个列名
            for (int i = 0; i < column; i++) {
                columnName[i] = sheet.getRow(0).getCell(i).toString();
            }

            int rows = sheet.getLastRowNum();
            for (int r = 1; r <= rows; r++) {
                Cell cell = null;
                try {
                    cell = sheet.getRow(r).getCell(0);
                    if (cell == null || StringUtil.isEmpty(cell.toString())) {
                        break;
                    }
                }
                catch (NullPointerException e) {
                    break;
                }

                // System.out.println("行号:" + r + "内容:" + cell.toString());
                this.row++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否仍有元素可以迭代
     *
     * @return
     */
    @Override
    public boolean hasNext() {

        if (row > 0 && sheet.getRow(row).getCell(0).getStringCellValue() != "" && usedRow <= row)
            return true;
        else {
            return false;
        }
    }

    /**
     * 返回迭代的下一个元素
     *
     * @return
     */
    @Override
    public Object[] next() {

        Object[] object = new Object[1];
        Map<String, String> map = new LinkedHashMap<String, String>();

        for (int c = 0; c < column; c++) {
            String cellValue = "";
            try {
                cellValue = sheet.getRow(this.usedRow).getCell(c).getStringCellValue();
            }
            catch (Exception e) {
                map.put(columnName[c], cellValue);
                continue;
            }
            map.put(columnName[c], cellValue);
        }

        this.usedRow++;
        object[0] = map;
        return object;
    }

    /**
     * 删除
     */
    @Override
    public void remove() {

    }
}
