package com.ztuo.bc.wallet.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.SpringApplication;

/**
 * MyBatis配置类
 * Created by macro on 2019/4/8.
 */
@Configuration
@EnableTransactionManagement
@MapperScan(scanBasePackages={"com.ztuo.bc.wallet.mapper","com.ztuo.bc.wallet.mapperextend"})
public class MyBatisConfig {
}
