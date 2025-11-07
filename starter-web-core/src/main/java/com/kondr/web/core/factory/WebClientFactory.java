package com.kondr.web.core.factory;

import java.time.Duration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Фабрика для создания экземпляров {@link WebClient}.
 */
public interface WebClientFactory {

  WebClient build(ClientHttpConnector connector);

  WebClient build(String poolName, Duration maxIdleTime);

}