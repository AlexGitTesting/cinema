package com.example.cinema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration(proxyBeanMethods = false)
public class MessageConfig {
    private final ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    public MessageConfig(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Bean
    @DependsOn("messageSource")
    public LocalValidatorFactoryBean validator() {
        final LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }
}
