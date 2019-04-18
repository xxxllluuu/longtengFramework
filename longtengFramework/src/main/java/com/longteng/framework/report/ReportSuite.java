package com.longteng.framework.report;

import java.util.ArrayList;
import java.util.List;

public class ReportSuite {

    private String suiteName;
    private String suiteType;
    private List<ReportCase> caseList = new ArrayList<ReportCase>();

    public String getSuiteName() {

        return suiteName;
    }

    public String getSuiteType() {

        return suiteType;
    }

    public void setSuiteType(String suiteType) {

        this.suiteType = suiteType;
    }

    public void setSuiteName(String suiteName) {

        this.suiteName = suiteName;
    }

    public void addCase(ReportCase reportCase) {

        caseList.add(reportCase);
    }

    public List<ReportCase> getCaseList() {

        return caseList;
    }

    public void setCaseList(List<ReportCase> caseList) {

        this.caseList = caseList;
    }
}
