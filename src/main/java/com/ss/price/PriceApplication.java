package com.ss.price;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangxuejin
 * @version 1.0.0 2024-02-21
 *
 *
 * 入口地址：
 *
 * https://api-v2.sensor-smart.cn:22085/ddinguia/web/first_index
 *
 * http://localhost:28002/ddinguia/web/first_index
 *
 */

@SpringBootApplication
@MapperScan("com.ss.price.mapper")
public class PriceApplication {
	public static void main(String[] args) {

		SpringApplication.run(PriceApplication.class, args);
	}
}
