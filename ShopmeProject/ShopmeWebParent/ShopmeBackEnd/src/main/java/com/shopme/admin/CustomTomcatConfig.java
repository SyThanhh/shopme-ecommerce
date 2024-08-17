package com.shopme.admin;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.catalina.connector.Connector;

@Configuration
public class CustomTomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
        	 connector.setMaxPostSize(20 * 1024 * 1024); // 20MB for POST data
             connector.setMaxParameterCount(60000); // Increase max parameter count
             connector.setMaxSavePostSize(20 * 1024 * 1024); // Ensure POST size is also configured
        });
    }
}
