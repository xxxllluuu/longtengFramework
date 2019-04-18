package com.longteng.framework.suite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.longteng.framework.config.Config;

public class SenarioLoader {

    public static Logger logger = Logger.getLogger(SenarioLoader.class);
    private String senario = "";
    private String currentTestCase = "";

    private List<String> senarioList = new ArrayList<String>();

    private SenarioLoader() {

    }

    private static class SenarioHolder {

        private final static SenarioLoader instance = new SenarioLoader();
    }

    /**
     * 单例模式，获取SenarioLoader实例
     *
     * @return
     */
    public static SenarioLoader getInstance() {

        return SenarioHolder.instance;
    }

    /**
     * 获取data文件夹绝对路径
     *
     * @return String
     */
    private String getAbsolutePath() {

        String path = null;
        File directory = new File("data");// 设定为当前文件夹

        try {
            path = directory.getAbsolutePath();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    /**
     * 获取测试场景excel文件路径
     *
     * @return String
     */
    private String getScenarioFilePath() {

        String fileName = "";

        try {

            fileName = new String(Config.get("SCENARIO_EXCEL_PATH").getBytes(), "UTF-8");

            return getAbsolutePath() + fileName;
        }
        catch (UnsupportedEncodingException e) {

        }

        return null;
    }

    /**
     * 获取excel workbook
     *
     * @param filePath
     * @return HSSFWorkbook
     */
    public Workbook getTestCaseWorkbook(String filePath) {

        Workbook wb = getWorkbook(filePath);
        return wb;
    }

    /**
     * 获取excel workbook
     *
     * @return HSSFWorkbook
     */
    private Workbook getWorkbook(String filePath) {

        boolean is2007 = false;
        Workbook wb = null;
        if (filePath.endsWith(".xlsx")) {
            is2007 = true;
        }
        InputStream input = null; // 建立输入流
        try {
            input = new FileInputStream(filePath);
            // 根据文件格式(2003或者2007)来初始化
            if (is2007) {
                wb = new XSSFWorkbook(input);
            }
            else {
                wb = new HSSFWorkbook(input);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (input != null) {
                    input.close();
                }
            }
            catch (IOException e) {
                // ignore
            }
        }

        return wb;
    }

    /**
     * 获取excel表格
     *
     * @return String
     */
    private Sheet getSheet() {

        // 测试场景.xlsx
        String filePath = getScenarioFilePath();

        Workbook wb = getWorkbook(filePath);
        Sheet sheet = wb.getSheetAt(0);
        return sheet;
    }

    /**
     * 获取当前测试用例名称
     *
     * @return
     */
    public String getCurrentTestCase() {

        return currentTestCase;
    }

    /**
     * 设置当前测试用例为currentTestCase
     *
     * @param currentTestCase
     */
    public void setCurrentTestCase(String currentTestCase) {

        this.currentTestCase = currentTestCase;
    }

    /**
     * 获取场景名称
     *
     * @return
     */
    public String getSenario() {

        return senario;
    }

    /**
     * 获取场景名称
     *
     * @return
     */
    public String getSenario(int index) {

        senario = getSenarioList().get(index);

        return senario;
    }

    /**
     * 设置场景名
     *
     * @param senario
     */
    public void setSenario(String senario) {

        this.senario = senario;
    }

    /**
     * 获取测试场景.xls中第一个sheet的第一列，即所有场景名称，封装到 List<String>
     *
     * @return List<String>
     */
    public List<String> getScenarios() {

        List<String> list = new ArrayList<String>();
        Sheet sheet = getSheet();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i); // 取得第 i Row
            if (row != null) {
                sheet.getColumnBreaks();
                if (row.getCell(0).toString() != "" && !"".equals(row.getCell(0).toString())) {
                    String name = row.getCell(0).toString();
                    list.add(name);
                }
            }
        }
        setScenarioList(list);
        return list;
    }

    /**
     * 获取baseDirName目录下，以targetFileName为名的文件，将文件的绝对路径add到filepath中
     *
     * @param baseDirName
     * @param targetFileName
     * @param filePath
     * @return
     */
    public List<String> getTestCaseFiles(String baseDirName, String targetFileName) {

        List<String> filePathList = new ArrayList<String>();

        File baseDir = new File(baseDirName); // 创建一个File对象
        if (!baseDir.exists() || !baseDir.isDirectory()) { // 判断目录是否存在
            logger.error("文件查找失败:" + baseDirName + "不是一个目录!");
        }
        String tempName = null;
        File tempFile = null;
        File[] files = baseDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];

            if (tempFile.isDirectory()) {
                filePathList.addAll(getTestCaseFiles(tempFile.getAbsolutePath(), targetFileName));
            }
            else if (tempFile.isFile()) {
                tempName = tempFile.getName();
                if (tempName.equalsIgnoreCase(targetFileName)) {
                    filePathList.add(tempFile.getAbsolutePath());
                }
            }
        }

        return filePathList;
    }

    public List<String> getSenarioList() {

        return senarioList;
    }

    public void setScenarioList(List<String> senarioList) {

        this.senarioList = senarioList;
    }

}
