package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class U5Dbc1Application {
	private final JdbcTemplate jdbcTemplate;

	public U5Dbc1Application(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(U5Dbc1Application.class, args);
	}

}
