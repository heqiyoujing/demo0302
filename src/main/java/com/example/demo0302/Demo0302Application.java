package com.example.demo0302;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Demo0302Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo0302Application.class, args);
	}

	/**
	 * spring boot版本问题:
	 * 1.5.X  :server.context-path=/XXXXX
	 * 2.0    :server.servlet.context-path=/XXXXX
	 */
}
