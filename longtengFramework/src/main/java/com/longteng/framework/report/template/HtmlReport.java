package com.longteng.framework.report.template;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.data.general.DefaultPieDataset;

import com.longteng.framework.report.Report;
import com.longteng.framework.report.ReportAssert;
import com.longteng.framework.report.ReportCase;
import com.longteng.framework.report.ReportSuite;
import com.longteng.framework.util.DateUtil;
import com.longteng.framework.util.FileUtil;
import com.longteng.framework.util.OSUtil;

public class HtmlReport {

    private static Report report = Report.getInstance();
    public static Logger logger = Logger.getLogger(HtmlReport.class);
    private static int totalCase = 0;
    private static int passCase = 0;
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    public static void writeReport(List<ReportSuite> suiteList) {

        logger.info("开始生成测试报告...");

        String template = FileUtil.readFile(getTemplatePath());

        template = replaceSuites(template, suiteList);
        template = replaceCases(template, suiteList);

        template = replaceTitle(template);
        template = replaceOutline(template);

        String path = getReportDirPath();

        String reportPath = path + "/report.html";
        FileUtil.writeFile(reportPath, template);

        logger.info("测试报告生成结束...");

        logger.info("开始生成饼状图...");
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();
            String passCaseCount = String.valueOf(passCase);
            String failCaseCount = String.valueOf(totalCase - passCase);
            dataset.setValue("PassCase", new Double(passCaseCount));
            dataset.setValue("FailCase", new Double(failCaseCount));
            String pngPath = path + "/report.png";
            PicReport.pieChart3D(dataset, pngPath, "通过率"); // 写入生成饼状图
            logger.info("饼状图生成结束...");
        }
        catch (Exception e) {
            logger.error("饼图生成失败:" + e.getMessage(), e);
        }
    }

    private static String replaceTitle(String template) {

        return template.replace("${title}", "自动化测试报告");
    }

    private static String replaceOutline(String template) {

        String date = DateUtil.getCurrentDate();
        template = template.replace("${reportDate}", date);

        String passRate = "0%";
        if (totalCase > 0) {
            passRate = numberFormat.format((float) passCase / (float) totalCase * 100) + "%"; // 通过率
        }

        report.setPassRate(passRate);

        template = template.replace("${totalCase}", String.valueOf(totalCase))
                .replace("${passCase}", String.valueOf(passCase))
                .replace("${failCase}", String.valueOf(totalCase - passCase))
                .replace("${passRate}", String.valueOf(passRate)).replace("${runTime}", report.getRunTime())
                .replace("${ip}", OSUtil.getLocalIP()).replace("${executor}", "龙腾测试");

        return template;
    }

    private static int caseCount = 0;

    private static String replaceSuites(String template, List<ReportSuite> suiteList) {

        StringBuilder suites = new StringBuilder();
        for (int i = 0; i < suiteList.size(); i++) {
            ReportSuite suite = suiteList.get(i);
            String suiteName = suite.getSuiteName();
            String tbodyId = "test" + i;
            String scenarioId = "scenario" + i;
            suites.append("<thead><tr><th class=\"header suite\" onclick=\"displayElement('" + tbodyId
                    + "', 'table-row-group'); switchScene('" + scenarioId + "')\">" + "<span id=\"" + scenarioId
                    + "\" class=\"toggle\">&#x25bc;</span><span>" + suiteName + "</span>" + "</th></tr></thead>");
            String cases = getCases(tbodyId, suite.getCaseList());
            suites.append(cases);
        }

        report.setCaseCount(totalCase);
        report.setPassCaseCount(passCase);
        return template.replace("${suites}", suites.toString());
    }

    private static String getCases(String tbodyId, List<ReportCase> caseList) {

        StringBuilder builder = new StringBuilder("<tbody id=\"" + tbodyId + "\" class=\"tests\">");
        for (int i = 0; i < caseList.size(); i++) {
            ReportCase reportCase = caseList.get(i);
            String caseName = reportCase.getCaseName();
            boolean caseStatus = reportCase.isCaseStatus();
            builder.append("<tr><td class=\"test\">");

            totalCase++;

            if (caseStatus) {
                passCase++;
                builder.append("<span class=\"successIndicator\" title=\"全部通过\">&#x2714;</span>");
            }
            else {
                builder.append("<span class=\"failureIndicator\" title=\"部分失败\">&#x2718;</span>");
            }

            builder.append(
                    "<a id=\"case" + caseCount + "\" href=\"#\" onclick=\"show(this)\">" + caseName + "</a></td></tr>");
            caseCount++;
        }

        builder.append("</tbody>");

        return builder.toString();
    }

    private static String replaceCases(String template, List<ReportSuite> suiteList) {

        caseCount = 0;
        String cases = "";

        for (int i = 0; i < suiteList.size(); i++) {
            ReportSuite suite = suiteList.get(i);
            List<ReportCase> caseList = suite.getCaseList();
            String caseDivs = "";

            for (int k = 0; k < caseList.size(); k++) {
                ReportCase aCase = caseList.get(k);
                List<ReportAssert> assertList = aCase.getAssertList();
                String display = "none";
                caseDivs += "<div id =\"case" + caseCount + "Div\" style=\"display: " + display + "\">";
                String resultTable = "<table  class=\"resultsTable\" >";
                for (int j = 0; j < assertList.size(); j++) {
                    ReportAssert step = assertList.get(j);
                    String stepName = step.getName();
                    String stepStatus = step.getStatus();
                    String actual = step.getActual();
                    String expected = step.getExpected();
                    String message = step.getMessage();

                    resultTable += "<tr><td class=\"method\" style=\"width:15%\">" + stepName + "</td>";

                    if (stepStatus.equalsIgnoreCase("pass")) {
                        resultTable += "<td class=\"passed\" style=\"width:5%\">" + stepStatus + "</td>";
                    }
                    else {
                        resultTable += "<td class=\"failed\" style=\"width:5%\">" + stepStatus + "</td>";
                    }

                    resultTable += " <td style=\"width:85%\"><i>预期结果:</i><span class=\"arguments\">" + expected
                            + "</span><br><i>实际结果:</i><span class=\"arguments\">" + actual + "</span><br>"
                            + "<i>描述:</i><span class=\"arguments\">" + message + "</span><br>" + "</td></tr>";
                }

                resultTable += "</table></div>";
                caseDivs += resultTable;
                caseCount++;
            }

            cases += caseDivs;
        }

        return template.replace("${cases}", cases).replace("${caseCount}", String.valueOf(caseCount));
    }

    /**
     * 获取report文件夹绝对路径
     *
     * @return String
     */
    private static String getReportDirPath() {

        File directory = new File("report");

        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory.getAbsolutePath();
    }

    /**
     * 获取report文件夹绝对路径
     *
     * @return String
     */
    private static String getTemplatePath() {

        File file = new File("");

        return file.getAbsolutePath() + "/config/report.html";
    }
}
