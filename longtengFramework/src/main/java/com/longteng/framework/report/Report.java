package com.longteng.framework.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Report {

    private Report() {

    }

    private static class ReportHolder {

        private final static Report instance = new Report();
    }

    /**
     * 单例模式，获取Report实例
     *
     * @return
     */
    public static Report getInstance() {

        return ReportHolder.instance;
    }

    private ReportSuite currentSuite;
    private ReportCase currentCase;
    private ReportAssert currentStep;

    private long startTime;
    private long endTime;
    private String runTime;

    private int caseCount;
    private int passCaseCount;

    private String passRate;

    private Map<String, ReportSuite> suiteMap;

    private List<ReportSuite> suiteList = new ArrayList<ReportSuite>();

    public void addSuite(ReportSuite suite) {

        suiteList.add(suite);
    }

    /**
     * @return the currentSuite
     */
    public ReportSuite getCurrentSuite() {

        return currentSuite;
    }

    /**
     * @param currentSuite
     *            the currentSuite to set
     */
    public void setCurrentSuite(ReportSuite currentSuite) {

        this.currentSuite = currentSuite;
    }

    /**
     * @return the currentCase
     */
    public ReportCase getCurrentCase() {

        return currentCase;
    }

    /**
     * @param currentCase
     *            the currentCase to set
     */
    public void setCurrentCase(ReportCase currentCase) {

        this.currentCase = currentCase;
    }

    /**
     * @return the currentStep
     */
    public ReportAssert getCurrentStep() {

        return currentStep;
    }

    /**
     * @param currentStep
     *            the currentStep to set
     */
    public void setCurrentStep(ReportAssert currentStep) {

        this.currentStep = currentStep;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {

        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(long startTime) {

        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public long getEndTime() {

        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(long endTime) {

        this.endTime = endTime;
    }

    /**
     * @return the runTime
     */
    public String getRunTime() {

        return runTime;
    }

    /**
     * @param runTime
     *            the runTime to set
     */
    public void setRunTime(String runTime) {

        this.runTime = runTime;
    }

    /**
     * @return the caseCount
     */
    public int getCaseCount() {

        return caseCount;
    }

    /**
     * @param caseCount
     *            the caseCount to set
     */
    public void setCaseCount(int caseCount) {

        this.caseCount = caseCount;
    }

    /**
     * @return the passCaseCount
     */
    public int getPassCaseCount() {

        return passCaseCount;
    }

    /**
     * @param passCaseCount
     *            the passCaseCount to set
     */
    public void setPassCaseCount(int passCaseCount) {

        this.passCaseCount = passCaseCount;
    }

    /**
     * @return the passRate
     */
    public String getPassRate() {

        return passRate;
    }

    /**
     * @param passRate
     *            the passRate to set
     */
    public void setPassRate(String passRate) {

        this.passRate = passRate;
    }

    /**
     * @return the suiteMap
     */
    public Map<String, ReportSuite> getSuiteMap() {

        return suiteMap;
    }

    /**
     * @param suiteMap
     *            the suiteMap to set
     */
    public void setSuiteMap(Map<String, ReportSuite> suiteMap) {

        this.suiteMap = suiteMap;
    }

    /**
     * @return the suiteList
     */
    public List<ReportSuite> getSuiteList() {

        return suiteList;
    }

    /**
     * @param suiteList
     *            the suiteList to set
     */
    public void setSuiteList(List<ReportSuite> suiteList) {

        this.suiteList = suiteList;
    }
}
