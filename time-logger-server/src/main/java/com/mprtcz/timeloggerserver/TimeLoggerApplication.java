package com.mprtcz.timeloggerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(basePackageClasses = { TimeLoggerApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication
public class TimeLoggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeLoggerApplication.class, args);
	}
}