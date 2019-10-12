package com.example.demosharing.dsconfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author:zhuzhou
 * @Date: 2019/10/12  11:17
 * 数据源
 **/
@Component
@ConfigurationProperties(prefix = "data01")
@Setter
public class DataSource01 {
    @Autowired
    DruidProperties druidProperties;
    private String driverClassName;
    private String url;
    private String userName;
    private String password;

    //配置数据源
    public DataSource buildBaseDataSource() throws Exception {
        Properties properties = druidProperties.setCustomProperties();
        properties.put(DruidDataSourceFactory.PROP_URL, url);//数据库url
        properties.put(DruidDataSourceFactory.PROP_USERNAME, userName);//用户名
        properties.put(DruidDataSourceFactory.PROP_PASSWORD, password);//密码
        properties.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, driverClassName);//Driver  数据库驱动
        return DruidDataSourceFactory.createDataSource(properties);
    }


}
