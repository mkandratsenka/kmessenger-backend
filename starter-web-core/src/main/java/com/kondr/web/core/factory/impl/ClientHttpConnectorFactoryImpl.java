package com.kondr.web.core.factory.impl;

import com.kondr.web.core.factory.ClientHttpConnectorFactory;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.Assert;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Slf4j
public class ClientHttpConnectorFactoryImpl implements ClientHttpConnectorFactory {

  @Value("${global.connection.tcp.max-idle-time}")
  private Duration globalMaxIdleTime;

  @Override
  public ClientHttpConnector build(String poolName, Duration maxIdleTime) {
    Assert.hasText(poolName, "poolName must not be empty");
    Assert.notNull(maxIdleTime, "maxIdleTime must not be null");

    if (maxIdleTime.compareTo(globalMaxIdleTime) > 0) {
      log.warn(
          "Pool '{}': maxIdleTime {} exceeds safe global limit {}. This may increase risk of 'Connection reset by peer' errors.",
          poolName, maxIdleTime, globalMaxIdleTime);
    }

    ConnectionProvider provider = ConnectionProvider.builder(poolName)
        .maxIdleTime(maxIdleTime)
        .build();

    return new ReactorClientHttpConnector(HttpClient.create(provider));
  }

}