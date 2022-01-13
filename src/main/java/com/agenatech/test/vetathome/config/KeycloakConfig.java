package com.agenatech.test.vetathome.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth-server")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakConfig {
    private String client;
    private String testUsername;
    private String testPassword;
    private String grantType;
    private String adminSecret;
}
