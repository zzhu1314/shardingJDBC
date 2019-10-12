package com.example.demosharing;

import com.example.demosharing.dsconfig.DruidProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { io.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration.class})
@EnableConfigurationProperties(value = {DruidProperties.class})
public class DemoSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSharingApplication.class, args);
    }

}
