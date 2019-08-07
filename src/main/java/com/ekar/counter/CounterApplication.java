package com.ekar.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"com.ekar.counter"})
@EnableJpaRepositories("com.ekar.counter.repository")
public class CounterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CounterApplication.class, args);
	}

}
