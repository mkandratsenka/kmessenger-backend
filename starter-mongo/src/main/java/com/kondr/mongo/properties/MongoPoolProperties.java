package com.kondr.mongo.properties;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки пула соединений MongoDB.
 */
@Getter
@Setter
@ConfigurationProperties("app.mongo.pool")
public class MongoPoolProperties {

  /**
   * Минимальный размер пула соединений.
   */
  private int minSize = 0;

  /**
   * Максимальный размер пула соединений.
   */
  private int maxSize = 50;

  /**
   * Максимальное время ожидания свободного соединения из пула.
   */
  private Duration maxWaitTime = Duration.ofSeconds(5);

  /**
   * Максимальное время, в течение которого неактивное соединение может оставаться в пуле, прежде
   * чем будет закрыто.
   */
  private Duration maxConnectionIdleTime = Duration.ofSeconds(120);

}