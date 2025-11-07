package com.kondr.web.core.factory;

import java.time.Duration;
import org.springframework.http.client.reactive.ClientHttpConnector;

/**
 * Фабрика для создания экземпляров {@link ClientHttpConnector}.
 */
public interface ClientHttpConnectorFactory {

  ClientHttpConnector build(String poolName, Duration maxIdleTime);

}