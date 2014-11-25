package com.qubaopen.doctor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.qubaopen.core.repository.MyRepositoryFactoryBean;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
	basePackages = "com.qubaopen.doctor.**.repository",
	repositoryFactoryBeanClass = MyRepositoryFactoryBean.class
)
public class RepoConfiguration {

}
