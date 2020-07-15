package com.accurascience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 项目启动入口
 *
 * @author zhuchaojie
 * @version 1.0.0
 */
@EnableDiscoveryClient
@EnableTransactionManagement//开启事务管理
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
