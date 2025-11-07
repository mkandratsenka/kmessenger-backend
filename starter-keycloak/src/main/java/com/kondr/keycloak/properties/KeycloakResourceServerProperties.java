package com.kondr.keycloak.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства конфигурации для настройки Keycloak Resource Server.
 */
@Getter
@Setter
@ConfigurationProperties("app.keycloak.resource-server")
public class KeycloakResourceServerProperties {

  private String issuerUri;

  private String jwkSetUri;

}