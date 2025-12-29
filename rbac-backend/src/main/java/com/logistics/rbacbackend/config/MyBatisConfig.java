package com.logistics.rbacbackend.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/29-21:47
 */
@Configuration
@MapperScan({"com.logistics.rbacbackend.mbg.mapper", "com.logistics.rbacbackend.dao"})
public class MyBatisConfig {
}
