package com.longteng.framework.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.longteng.framework.config.Config;
import com.longteng.framework.config.Constants;

public class OracleDB implements IBaseDB {

    public static Logger logger = Logger.getLogger(MysqlDB.class);
    private static String username;
    private static String password;
    private static String url;

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        username = Config.get("oracle.jdbc.username");
        password = Config.get("oracle.jdbc.password");
        url = Config.get("oracle.jdbc.url");
    }

    public void close(Connection con, Statement statement, ResultSet resultSet) {

        if (resultSet != null) {

            try {
                resultSet.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if (statement != null) {

            try {
                statement.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if (con != null) {

            try {
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 根据sql更新数据库
     * 
     * @param sql
     * @return
     */
    @Override
    public int update(String sql) {

        Connection con = null;
        Statement statement = null;

        try {

            con = DriverManager.getConnection(url, username, password);
            statement = con.createStatement();

            logger.info("更新数据库:" + sql);
            return statement.executeUpdate(sql);
        }
        catch (Exception e) {
            logger.error("更新失败:" + e.getMessage(), e);
            return -1;
        }
        finally {
            close(con, statement, null);
        }
    }

    /**
     * 根据sql和字段field查询单条记录，以HashMap<String, String>形式返回
     * 
     * @param sql
     * @param field
     * @return
     */
    @Override
    public Map<String, String> selectRecord(String sql, String field) {

        Map<String, String> map = new HashMap<String, String>();
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            con = DriverManager.getConnection(url, username, password);
            statement = con.createStatement();

            logger.info("开始查询数据库:" + sql);
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                String[] fields = field.split(Constants.COMMA);
                for (int i = 0; i < fields.length; i++) {
                    map.put(fields[i], String.valueOf(resultSet.getObject(fields[i])));
                }
            }
        }
        catch (Exception e) {
            logger.error("查询失败:" + e.getMessage(), e);
        }
        finally {
            close(con, statement, resultSet);
        }

        return map;
    }

    /**
     * 根据sql和字段field查询多条记录，以List<HashMap<String, String>>形式返回
     * 
     * @param sql
     * @param field
     * @return
     */
    @Override
    public List<Map<String, String>> selectRecords(String sql, String field) {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            con = DriverManager.getConnection(url, username, password);
            statement = con.createStatement();

            logger.info("开始查询数据库:" + sql);
            resultSet = statement.executeQuery(sql);
            String[] fields = field.split(Constants.COMMA);

            while (resultSet.next()) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < fields.length; i++) {
                    map.put(fields[i], String.valueOf(resultSet.getObject(fields[i])));
                }
                list.add(map);
            }
        }
        catch (Exception e) {
            logger.error("查询失败:" + e.getMessage(), e);
        }

        return list;
    }

    /**
     * 执行sql语句，检查field字段的值与result是否一致
     * 
     * @param sql
     * @param field
     * @param result
     * @return
     */
    @Override
    public boolean compare(String sql, String field, String result) {

        boolean flag = false;

        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            con = DriverManager.getConnection(url, username, password);
            statement = con.createStatement();

            logger.info("开始查询数据库:" + sql);
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                if (resultSet.getString(field).equals(result)) {
                    flag = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            logger.error("查询失败:" + e.getMessage(), e);
        }

        return flag;
    }
}
