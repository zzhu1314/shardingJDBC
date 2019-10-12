package com.example.demosharing.dsconfig;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Author:zhuzhou
 * @Date: 2019/10/12  10:32
 **/
@ConfigurationProperties(prefix = "spring.datasource.druid")
@Component
@Setter
public class DruidProperties {

    private String initialSize;
    private String maxActive;
    private String minIdle;
    private String maxWait;
    private String timeBetweenEvictionRunsMillis;
    private String minEvictableIdleTimeMillis;
    private String validationQuery;
    private String testWhileIdle;
    private String testOnBorrow;
    private String testOnReturn;
    private String poolPreparedStatements;
    private String maxPoolPreparedStatementPerConnectionSize;
    private String filters;


    //配置连接池信息
    public Properties setCustomProperties() {
        Properties properties = new Properties();
        properties.put(DruidDataSourceFactory.PROP_INITIALSIZE, initialSize);//初始化时建立物理连接的个数 默认是0  配置10
        properties.put(DruidDataSourceFactory.PROP_MAXACTIVE, maxActive);//最大连接池数量默认是8  50
        properties.put(DruidDataSourceFactory.PROP_MINIDLE, minIdle);//最小连接池数量 10
        properties.put(DruidDataSourceFactory.PROP_MAXWAIT, maxWait);//获取连接时最大等待时间  如果配置了这个值之后 默认使用公平锁 并发效率会有所下降  可以通过配置userUnfairLock 为true使用非公平锁
        properties.put(DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS,timeBetweenEvictionRunsMillis);//有两个含义： 1) Destroy线程会检测连接的间隔时间2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
        properties.put(DruidDataSourceFactory.PROP_MINEVICTABLEIDLETIMEMILLIS, minEvictableIdleTimeMillis);
        properties.put(DruidDataSourceFactory.PROP_VALIDATIONQUERY, validationQuery);//用来检测连接是否有效的sql，要求是一个查询语句  如果此处为null 后面三个不会起作用
        properties.put(DruidDataSourceFactory.PROP_TESTWHILEIDLE, testWhileIdle);
        properties.put(DruidDataSourceFactory.PROP_TESTONBORROW, testOnBorrow);
        properties.put(DruidDataSourceFactory.PROP_TESTONRETURN, testOnReturn);
        properties.put(DruidDataSourceFactory.PROP_POOLPREPAREDSTATEMENTS, poolPreparedStatements);//是否缓存是否缓存preparedStatement
        properties.put(DruidDataSourceFactory.PROP_MAXOPENPREPAREDSTATEMENTS, maxPoolPreparedStatementPerConnectionSize);
        properties.put(DruidDataSourceFactory.PROP_FILTERS, filters);
        //properties.put(DruidDataSourceFactory.PROP_CONNECTIONPROPERTIES, connectionProperties);//物理连接初始化的时候执行的sql
        return properties;
    }
}
