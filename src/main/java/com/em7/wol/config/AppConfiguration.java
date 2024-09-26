package com.em7.wol.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(
    value="server.insecure.enabled",
    havingValue = "true",
    matchIfMissing = false)
public class AppConfiguration {

    @Value("${server.http.port}")
    private int httpPort;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return (TomcatServletWebServerFactory factory) -> {
            // also listen on http
            final Connector connector = new Connector();
            log.info(String.valueOf(httpPort));

            connector.setPort(httpPort);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }
}