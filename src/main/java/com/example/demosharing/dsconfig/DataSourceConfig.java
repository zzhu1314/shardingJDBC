
package com.example.demosharing.dsconfig;

import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.core.constant.properties.ShardingPropertiesConstant;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
@Slf4j
public class DataSourceConfig {

    private String subTbByMonthNames = "face_swipe_record";

    @Value("${face.database.name01}")
    private String faceDBName01;
    @Value("${face.database.name02}")
    private String faceDBName02;

    private String faceDBName = "test0";
    @Value("${face.database.thread_num}")
    private int threadNum;
    @Autowired
    private DataSource01 dataSource01;
    @Autowired
    private DataSource02 dataSource02;



    @Primary
    @Bean(name = "shardingDataSource")
    @Qualifier("shardingDataSource")
    public DataSource buildDataSource() throws Exception {
        log.trace("build shardingDataSource");
        Map<String, DataSource> dsMap = new HashMap<>();
        dsMap.put(faceDBName01, dataSource01.buildBaseDataSource());
        dsMap.put(faceDBName02,dataSource02.buildBaseDataSource());

        List<TableRuleConfiguration> tbRuleConfigs = createTbRuleConfigs();

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().addAll(tbRuleConfigs);
        //设置不需要分库的数据源   即 默认的
//		shardingRuleConfiguration.setDefaultDatabaseShardingStrategyConfig(null);
//		shardingRuleConfiguration.setDefaultDataSourceName(baseDBName);

        try {
            Properties properties = new Properties();
            log.info("-----------分片线程数-----------:" + threadNum);
            properties.put(ShardingPropertiesConstant.EXECUTOR_SIZE.getKey(), threadNum);
            properties.put(ShardingPropertiesConstant.SQL_SHOW.getKey(), false);
            return ShardingDataSourceFactory.createDataSource(dsMap, shardingRuleConfiguration,
                    new ConcurrentHashMap<String, Object>(), properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<TableRuleConfiguration> createTbRuleConfigs() {
        ArrayList<TableRuleConfiguration> tbRuleConfigs = new ArrayList<>();
        //首先是按照月份分表	//自定义的按照月份分表的分片算法实现  分表  第一个参数  根据哪一个字段 做分片  第二个参数 根据哪一种分片策略
        StandardShardingStrategyConfiguration standardTbByMonthStrategyConfig = new StandardShardingStrategyConfiguration("swipe_time",
                new TbMonthPreciseShardingAlgorithm());
        for (String tbName : subTbByMonthNames.split(",")) {
            TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
            //设置逻辑表
            tableRuleConfiguration.setLogicTable(tbName);
            //按月份分  分12张表  用行级表达式配置数据节点即数据库，表（1-12），
            //也可配置多个库%ds${1..2}.%table_${%d..%d} 注：配置了ds1和ds2两个库，table1到table12共12张表
            String nodeName = String.format("%s${1..2}.%s_${%d..%d}", faceDBName, tbName, 1, 12);
            tableRuleConfiguration.setActualDataNodes(nodeName);
            tableRuleConfiguration.setTableShardingStrategyConfig(standardTbByMonthStrategyConfig);
            tbRuleConfigs.add(tableRuleConfiguration);
        }
        return tbRuleConfigs;
    }


}

