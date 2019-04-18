package com.longteng.framework.listener;

import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.longteng.framework.report.ReportCase;
import com.longteng.framework.report.Report;
import com.longteng.framework.report.ReportSuite;
import com.longteng.framework.suite.SenarioLoader;

public class TestListener implements ITestListener {

    private static SenarioLoader loader = SenarioLoader.getInstance();
    private static Logger logger = Logger.getLogger(TestListener.class);
    private static Report report = Report.getInstance();

    /**
     * 所有测试执行完成和对应的构造函数调用完成之后执行
     *
     * @param arg0
     */
    @Override
    public void onFinish(ITestContext arg0) {

        logger.info("测试场景执行结束:" + arg0.getSuite().getName());
    }

    /**
     * 在测试类实例化之后，构造函数调用之前执行 打印log信息，并注入场景名
     *
     * @param arg0
     */
    @Override
    public void onStart(ITestContext arg0) {

        String suiteName = arg0.getSuite().getName();
        ReportSuite suite = report.getCurrentSuite();
        if (suite == null) {
            logger.info("开始执行测试场景:" + suiteName);
            suite = new ReportSuite();
            suite.setSuiteName(suiteName);
            report.addSuite(suite);
        }
        else {
            if (!suiteName.equalsIgnoreCase(suite.getSuiteName())) {
                logger.info("开始执行测试场景:" + suiteName);
                suite = new ReportSuite();
                suite.setSuiteName(suiteName);
                report.addSuite(suite);
            }
        }
        loader.setSenario(suiteName);
        report.setCurrentSuite(suite);
    }

    /**
     * 每次一个方法失败但已经用successPercentage注解并且这个失败仍然保持在要求的成功率内时执行
     *
     * @param arg0
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {

    }

    /**
     * 每次测试失败时执行
     *
     * @param arg0
     */
    @Override
    public void onTestFailure(ITestResult arg0) {

    }

    /**
     * 每次跳过测试时执行
     *
     * @param arg0
     */
    @Override
    public void onTestSkipped(ITestResult arg0) {

    }

    /**
     * 每次调用测试之前执行 打印log信息，并注入当前测试用例名称 打印检查点：初始化用例信息成功
     *
     * @param arg0
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void onTestStart(ITestResult arg0) {

        String caseName = arg0.getTestContext().getName();
        loader.setCurrentTestCase(caseName);
        ReportCase reportCase = new ReportCase();
        report.getCurrentSuite().addCase(reportCase);
        report.setCurrentCase(reportCase);
        Object[] o = arg0.getParameters();
        Map<String, String> caseMap = (Map) o[0];
        String interfaceCaseName = caseMap.get("用例名称");
        reportCase.setCaseName(interfaceCaseName);
        reportCase.setCaseType("interface");
        arg0.getTestContext().getCurrentXmlTest().setName(interfaceCaseName);
        arg0.getTestContext().setAttribute("caseName", interfaceCaseName);
        logger.info("开始执行测试用例:" + interfaceCaseName);
    }

    /**
     * 每次测试成功时执行
     *
     * @param arg0
     */
    @Override
    public void onTestSuccess(ITestResult arg0) {

    }
}
