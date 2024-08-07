package com.capgemini.csd.tippkick.cukes.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@ComponentScan("com.capgemini.csd.tippkick.cukes.*")
public class CucumberConfiguration {

    @Bean
    public TestRestTemplate spielplanRestTemplate(@Value("${spielplan.url:http://localhost:7080}") String appUrl) {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(appUrl));
        return restTemplate;
    }

    @Bean
    public TestRestTemplate tippabgabeRestTemplate(@Value("${tippabgabe.url:http://localhost:7081}") String appUrl) {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(appUrl));
        return restTemplate;
    }

    @Bean
    public TestRestTemplate tippwertungRestTemplate(@Value("${tippwertung.url:http://localhost:7082}") String appUrl) {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(appUrl));
        return restTemplate;
    }

}
