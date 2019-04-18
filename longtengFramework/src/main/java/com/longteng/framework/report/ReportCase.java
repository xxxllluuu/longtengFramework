package com.longteng.framework.report;

import java.util.ArrayList;
import java.util.List;

public class ReportCase {

    private String caseName;
    private boolean caseStatus = true;
    private String caseType;
    private List<ReportAssert> assertList = new ArrayList<ReportAssert>();

    public void addAssert(ReportAssert reportAssert) {

        assertList.add(reportAssert);
    }

    /**
     * @return the caseName
     */
    public String getCaseName() {

        return caseName;
    }

    /**
     * @param caseName
     *            the caseName to set
     */
    public void setCaseName(String caseName) {

        this.caseName = caseName;
    }

    /**
     * @return the caseStatus
     */
    public boolean isCaseStatus() {

        return caseStatus;
    }

    /**
     * @param caseStatus
     *            the caseStatus to set
     */
    public void setCaseStatus(boolean caseStatus) {

        this.caseStatus = caseStatus;
    }

    /**
     * @return the caseType
     */
    public String getCaseType() {

        return caseType;
    }

    /**
     * @param caseType
     *            the caseType to set
     */
    public void setCaseType(String caseType) {

        this.caseType = caseType;
    }

    /**
     * @return the assertList
     */
    public List<ReportAssert> getAssertList() {

        return assertList;
    }

    /**
     * @param assertList
     *            the assertList to set
     */
    public void setAssertList(List<ReportAssert> assertList) {

        this.assertList = assertList;
    }
}
