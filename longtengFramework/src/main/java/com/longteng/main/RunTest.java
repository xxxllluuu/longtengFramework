package com.longteng.main;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import com.longteng.framework.config.Config;
import com.longteng.framework.config.Constants;
import com.longteng.framework.listener.TestListener;
import com.longteng.framework.report.Report;
import com.longteng.framework.report.ReportSuite;
import com.longteng.framework.report.template.HtmlReport;
import com.longteng.framework.suite.SuiteLoader;
import com.longteng.framework.util.EmailUtil;
import com.longteng.framework.util.FileUtil;

public class RunTest {

    private static Report report = Report.getInstance();
    private static Logger logger = Logger.getLogger(RunTest.class);

    public void run() {

        logger.info("----------------测试开始----------------");

        /**
         * step1:创建TestNG对象
         */
        TestNG testNG = new TestNG();

        ITestNGListener listener = new TestListener();
        // 加入自定义监听
        testNG.addListener(listener);
        // 不使用TestNg默认的测试报告
        testNG.setUseDefaultListeners(false);
        // 设置运行套件
        testNG.setXmlSuites(loadSuite());

        /**
         * step2:运行所有TestNG的test case
         */
        try {

            long startTime = System.currentTimeMillis();
            report.setStartTime(startTime);
            logger.info("开始所有运行测试!");

            testNG.run();

            long entTime = System.currentTimeMillis();
            report.setEndTime(entTime);
            logger.info("所有测试运行结束!");

            long runTime = entTime - startTime;
            report.setRunTime((runTime) + "毫秒");
        }
        catch (Exception e) {
            logger.error("TestNG运行异常:" + e.getMessage(), e);
        }

        /**
         * step3:生成测试报告
         */
        writeReport();

        /**
         * step4:发送测试报告
         */
        // sendReport();

        logger.info("----------------测试结束----------------");
    }

    public static void main(String[] args) {

        RunTest runTest = new RunTest();
        runTest.run();
    }

    /**
     * 生成测试报告
     */
    private void writeReport() {

        FileUtil.deleteFolder(Constants.REPORT_DIR, false);
        List<ReportSuite> reportSuiteList = report.getSuiteList();
        HtmlReport.writeReport(reportSuiteList);
    }

    /**
     * 发送邮件
     */
    private void sendReport() {

        try {
            String sendTo = Config.get("mail.sendto");
            if (!"".equalsIgnoreCase(sendTo)) {

                logger.info("开始发送测试报告!");

                EmailUtil.doSendAttachmentEmail("接口测试报告", "附件为接口测试报告", sendTo, "report\\report.html");

                logger.info("测试报告发送完毕!");
            }
            else {
                logger.info("未设置任何收件人!");
            }
        }
        catch (Exception e) {
            logger.error("发送测试报告异常:" + e.getMessage(), e);
        }
    }

    /**
     * 加载测试套件
     *
     * @return
     */
    private static List<XmlSuite> loadSuite() {

        logger.info("开始加载测试场景!");

        List<XmlSuite> xmlSuiteList = SuiteLoader.getXmlSuites();

        logger.info("加载测试场景结束!");

        return xmlSuiteList;
    }
}
