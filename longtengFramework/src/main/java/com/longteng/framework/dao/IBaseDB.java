package com.longteng.framework.dao;

import java.util.List;
import java.util.Map;

public interface IBaseDB {

    /**
     * 根据sql和字段field查询单条记录
     * 
     * @param sql
     * @param field
     * @return
     */
    public Map<String, String> selectRecord(String sql, String field);

    /**
     * 根据sql更新数据库
     * 
     * @param sql
     * @return
     */
    public int update(String sql);

    /**
     * 根据sql和字段field查询多条记录
     * 
     * @param sql
     * @param field
     * @return
     */
    public List<Map<String, String>> selectRecords(String sql, String field);

    /**
     * 执行sql语句，检查field字段的值与expect是否一致
     * 
     * @param sql
     * @param field
     * @param expect
     * @return
     */
    public boolean compare(String sql, String field, String expect);

}
