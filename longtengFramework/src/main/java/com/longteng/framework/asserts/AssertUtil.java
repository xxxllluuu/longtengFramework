package com.longteng.framework.asserts;

import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.longteng.framework.dao.MysqlDB;
import com.longteng.framework.report.Report;
import com.longteng.framework.report.ReportAssert;
import com.longteng.framework.report.ReportCase;

public class AssertUtil {

    public static Report report = Report.getInstance();
    public static Logger logger = Logger.getLogger(AssertUtil.class);

    /**
     * 断言是否相等(从数据库查询结果)
     * 
     * @param sql
     * @param map
     * @param message
     */
    public static void assertDBFieldEquals(String sql, Map<String, String> map, String message) {

        ReportCase currentCase = report.getCurrentCase();

        if (currentCase == null) {
            return;
        }

        int assertSize = currentCase.getAssertList().size();

        ReportAssert reportAssert = new ReportAssert();
        reportAssert.setName("断言" + (assertSize + 1));

        MysqlDB db = new MysqlDB();

        String field = null;
        String result = null;

        for (String key : map.keySet()) {
            field = key;
            result = map.get(key);
        }

        if (db.compare(sql, field, result)) {
            reportAssert.setStatus("Pass");
        }
        else {
            report.getCurrentCase().setCaseStatus(false);
            reportAssert.setStatus("Fail");
        }

        reportAssert.setActual(sql);
        reportAssert.setExpected("field:" + field + " result:" + result);
        reportAssert.setMessage(message);
        currentCase.addAssert(reportAssert);
    }

    /**
     * 断言是否相等
     * 
     * @param actual
     * @param expected
     * @param message
     */
    public static void assertEquals(String actual, String expected, String message) {

        ReportCase currentCase = report.getCurrentCase();

        if (currentCase == null) {
            return;
        }

        int assertSize = currentCase.getAssertList().size();

        ReportAssert reportAssert = new ReportAssert();
        reportAssert.setName("断言" + (assertSize + 1));

        try {
            Assert.assertEquals(actual, expected, message);
            reportAssert.setStatus("Pass");
        }
        catch (Error e) {
            currentCase.setCaseStatus(false);
            reportAssert.setStatus("Fail");
        }

        reportAssert.setActual(actual);
        reportAssert.setExpected(expected);
        reportAssert.setMessage(message);
        currentCase.addAssert(reportAssert);
    }

    /**
     * 断言是否包含
     * 
     * @param actual
     * @param expected
     * @param message
     */
    public static void assertContains(String actual, String expected, String message) {

        ReportCase currentCase = report.getCurrentCase();

        if (currentCase == null) {
            return;
        }

        int assertSize = currentCase.getAssertList().size();

        ReportAssert reportAssert = new ReportAssert();
        reportAssert.setName("断言" + (assertSize + 1));

        if (null != actual && actual.contains(expected)) {
            reportAssert.setStatus("Pass");
        }
        else {
            report.getCurrentCase().setCaseStatus(false);
            reportAssert.setStatus("Fail");
        }

        reportAssert.setActual(actual);
        reportAssert.setExpected(expected);
        reportAssert.setMessage(message);
        currentCase.addAssert(reportAssert);
    }
}
