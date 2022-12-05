package com.ssafy.home;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan(basePackages = "com.ssafy.home.model")
public class WhereIsMyHomeSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereIsMyHomeSpringApplication.class, args);
	}

}
