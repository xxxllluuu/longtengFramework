package com.longteng.framework.suite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class SuiteLoader {

    public static Logger logger = Logger.getLogger(SuiteLoader.class);
    private static SenarioLoader loader = SenarioLoader.getInstance();

    /**
     * 根据 测试场景.xlsx 中场景名称搜索【场景名称.xlsx】 初始化获取所有的场景、每个场景下的测试用例以及每个测试用例相对应的测试类
     *
     * @return
     */
    public static List<XmlSuite> getXmlSuites() {

        List<String> scenarioList = loader.getScenarios();
        List<XmlSuite> suites = new ArrayList<XmlSuite>();

        // scenarioList 所有的场景名称
        for (String name : scenarioList) {

            List<XmlClass> classes = new ArrayList<XmlClass>();
            XmlSuite xmlSuite = new XmlSuite();
            xmlSuite.setName(name);

            XmlTest test = new XmlTest(xmlSuite);
            classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("com.longteng.testcase.InterfaceTest"));
            test.setName(name);
            test.setClasses(classes);

            suites.add(xmlSuite);
        }

        return suites;
    }
}
