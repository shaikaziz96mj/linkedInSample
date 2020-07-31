package com.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SampleLinkedInBootProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleLinkedInBootProjectApplication.class, args);
	}

}
