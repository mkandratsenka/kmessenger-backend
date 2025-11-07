package com.kondr.keycloak.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства конфигурации для настройки M2M клиента Keycloak.
 */
@Getter
@Setter
@ConfigurationProperties("app.keycloak.client")
public class KeycloakClientProperties {

  private String registrationId;

}
