package com.longteng.framework.report;

public class ReportAssert {

    private String status;

    private String actual;

    private String expected;

    private String message;

    private String name;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getActual() {

        return actual;
    }

    public void setActual(String actual) {

        this.actual = actual;
    }

    public String getExpected() {

        return expected;
    }

    public void setExpected(String expected) {

        this.expected = expected;
    }

}
