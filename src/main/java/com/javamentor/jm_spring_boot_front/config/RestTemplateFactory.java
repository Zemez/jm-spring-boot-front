package com.javamentor.jm_spring_boot_front.config;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource("classpath:backend.properties")
public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean {

    private RestTemplate restTemplate;

    @Value("${backend.connection.host}")
    private String host;
    @Value("${backend.connection.port}")
    private int port;
    @Value("${backend.connection.proto}")
    private String proto;
    @Value("${backend.connection.username}")
    private String username;
    @Value("${backend.connection.password}")
    private String password;

    @Override
    public RestTemplate getObject() {
        return restTemplate;
    }

    @Override
    public Class<RestTemplate> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        HttpHost host = new HttpHost(this.host, this.port, this.proto);
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactoryBasicAuth(host));
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username, password));
    }

}
