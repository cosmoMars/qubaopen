package com.qubaopen.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
public final class Application {

//	@Bean
//	public AuditorAware<User> auditorAware() {
//		return new AuditorAware<User>() {
//			@Override
//			public User getCurrentAuditor() {
//				return null;
//			}
//		};
//	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Hibernate4Module())
			.registerModule(new JodaModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return mapper;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
