package com.mprtcz.timeloggerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

@EntityScan(basePackageClasses = { TimeLoggerApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class})
public class TimeLoggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeLoggerApplication.class, args);
	}

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		return new HibernateJpaSessionFactoryBean();
	}


}