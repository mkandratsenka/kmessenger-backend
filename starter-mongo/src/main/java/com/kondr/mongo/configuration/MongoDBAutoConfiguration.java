package com.kondr.mongo.configuration;

import com.kondr.mongo.factory.ObjectIdFactory;
import com.kondr.mongo.factory.impl.DefaultObjectIdFactory;
import com.kondr.mongo.mapper.ObjectIdMapper;
import com.kondr.mongo.properties.MongoPoolProperties;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.transaction.reactive.TransactionalOperator;

/**
 * Автоматическая конфигурация, настраивающая инфраструктуру MongoDB для реактивного
 * взаимодействия.
 */
@AutoConfiguration(before = MongoReactiveAutoConfiguration.class)
@EnableConfigurationProperties(MongoPoolProperties.class)
@EnableReactiveMongoAuditing
public class MongoDBAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
    return new ReactiveMongoTransactionManager(factory);
  }

  @Bean
  @ConditionalOnMissingBean
  public TransactionalOperator transactionalOperator(
      ReactiveMongoTransactionManager transactionManager) {

    return TransactionalOperator.create(transactionManager);
  }

  @Bean
  @ConditionalOnMissingBean
  public MongoClientSettingsBuilderCustomizer poolCustomizer(MongoPoolProperties props) {
    return builder -> builder.applyToConnectionPoolSettings(pool -> pool
        .minSize(props.getMinSize())
        .maxSize(props.getMaxSize())
        .maxWaitTime(props.getMaxWaitTime().toMillis(), TimeUnit.MILLISECONDS)
        .maxConnectionIdleTime(props.getMaxConnectionIdleTime().toMillis(), TimeUnit.MILLISECONDS)
    );
  }

  @Bean
  @ConditionalOnMissingBean
  public ObjectIdMapper objectIdMapper() {
    return new ObjectIdMapper();
  }

  @Bean
  @ConditionalOnMissingBean
  public ObjectIdFactory objectIdFactory() {
    return new DefaultObjectIdFactory();
  }

}