package com.longteng.testcase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.longteng.framework.asserts.AssertUtil;
import com.longteng.framework.param.ExcelDataProvider;
import com.longteng.framework.suite.SenarioLoader;
import com.longteng.framework.util.HttpClientUtil;

public class InterfaceTest {

    private static SenarioLoader loader = SenarioLoader.getInstance();

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "dataProvider")
    public void run(Map<String, String> dataMap) {

        String url = dataMap.get("接口地址(名称)");
        String method = dataMap.get("方法");
        String param = dataMap.get("入参");
        String assertType = dataMap.get("断言类型");
        String expected = dataMap.get("预期结果");
        String sql = dataMap.get("SQL");
        String field = dataMap.get("字段");

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("url", url);
        paramMap.put("method", method);
        paramMap.put("requestBody", param);
        paramMap.put("Content-Type", "application/json");

        Map<String, String> resultMap = HttpClientUtil.request(paramMap, null);

        String actual = resultMap.get("responseBody");

        String[] assertTypeArr = assertType.split(";");

        for (String aAssertType : assertTypeArr) {

            if ("返回值全部匹配".equalsIgnoreCase(aAssertType)) {
                AssertUtil.assertEquals(actual, expected, "HTTP返回值字符串全匹配");
            }

            if ("返回值包含".equalsIgnoreCase(aAssertType)) {
                AssertUtil.assertContains(actual, expected, "HTTP返回值字符串包含");
            }

            if ("数据库匹配".equalsIgnoreCase(aAssertType)) {
                Map<String, String> map = JSON.parseObject(field, Map.class);
                AssertUtil.assertDBFieldEquals(sql, map, "数据库查询结果字段值匹配");
            }
        }
    }

    @DataProvider(name = "dataProvider")
    public Iterator<Object[]> provideData() throws Exception {

        String senarioName = loader.getSenario();
        // 加油卡接口测试.xlsx
        List<String> fileList = loader.getTestCaseFiles(".\\data", senarioName + ".xlsx");

        if (fileList.size() == 0) {
            throw new RuntimeException("接口测试数据文件未找到...");
        }

        Iterator<Object[]> iterator = new ExcelDataProvider(fileList.get(0));
        return iterator;
    }
}
