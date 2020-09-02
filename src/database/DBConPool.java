/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.alibaba.druid.pool.DruidDataSource;
import constants.ServerConstants;
import server.ServerProperties;

/**
 *
 * @author wubin
 */
public class DBConPool {

    private static DruidDataSource dataSource = null;
    public static final int RETURN_GENERATED_KEYS = 1;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("[数据库信息] 找不到JDBC驱动.");
            System.exit(0);
        }

    }

    private static class InstanceHolder {

        public static final DBConPool instance = new DBConPool();
    }

    public static DBConPool getInstance() {
        return InstanceHolder.instance;
    }

    private DBConPool() {
    }

    public DruidDataSource getDataSource() {
        if (dataSource == null) {
            InitDBConPool();
        }
        return dataSource;
    }

    private void InitDBConPool() {
        dataSource = new DruidDataSource();
        dataSource.setName("mysql_pool");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + "127.0.0.1" + ":" + ServerConstants.SQL_PORT + "/" + ServerConstants.SQL_DATABASE + "?useUnicode=true&characterEncoding=GBK");
        dataSource.setUsername(ServerConstants.SQL_USER);
        dataSource.setPassword(ServerConstants.SQL_PASSWORD);
        /*dataSource.setInitialSize(30);
        dataSource.setMinIdle(50);
        dataSource.setMaxActive(400);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setMaxWait(60000);
        dataSource.setUseUnfairLock(true);*/
        dataSource.setInitialSize(30);
        dataSource.setMinIdle(20);
        dataSource.setMaxActive(400);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        //dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(3000);
        
        dataSource.setUseUnfairLock(true);


    }

}
