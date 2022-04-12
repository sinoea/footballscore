package com.willhamhill.football;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.willhamhill")
public class ScoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScoreApplication.class, args);
	}
}