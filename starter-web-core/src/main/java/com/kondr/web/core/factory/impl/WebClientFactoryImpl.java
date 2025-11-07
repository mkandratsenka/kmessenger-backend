package com.kondr.web.core.factory.impl;

import com.kondr.web.core.factory.ClientHttpConnectorFactory;
import com.kondr.web.core.factory.WebClientFactory;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class WebClientFactoryImpl implements WebClientFactory {

  private final ClientHttpConnectorFactory clientHttpConnectorFactory;

  @Override
  public WebClient build(ClientHttpConnector connector) {
    return WebClient.builder()
        .clientConnector(connector)
        .build();
  }

  @Override
  public WebClient build(String poolName, Duration maxIdleTime) {
    return build(clientHttpConnectorFactory.build(poolName, maxIdleTime));
  }

}