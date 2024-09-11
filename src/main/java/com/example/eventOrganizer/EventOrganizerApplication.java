package com.example.eventOrganizer;

import com.example.eventOrganizer.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class EventOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventOrganizerApplication.class, args);
	}

}
