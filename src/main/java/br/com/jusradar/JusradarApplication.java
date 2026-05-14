package br.com.jusradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JusradarApplication {

	public static void main(String[] args) {
		SpringApplication.run(JusradarApplication.class, args);
	}

}
