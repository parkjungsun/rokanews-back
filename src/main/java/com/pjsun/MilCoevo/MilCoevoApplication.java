package com.pjsun.MilCoevo;

import com.pjsun.MilCoevo.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class MilCoevoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilCoevoApplication.class, args);
	}

}
