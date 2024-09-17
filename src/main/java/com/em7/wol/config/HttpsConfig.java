package com.em7.wol.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfig {

//    @Value("${server.http.port}")
//    private int httpPort;
//
//    @Value("${server.http.interface}")
//    private String httpInterface;
//
//    @Bean
//    public UndertowServletWebServerFactory containerCustomizer() {
//        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
//        factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
//            @Override
//            public void customize(Builder builder) {
//                builder.addHttpListener(httpPort, httpInterface);
//            }
//        });
//        return factory;
//    }
}