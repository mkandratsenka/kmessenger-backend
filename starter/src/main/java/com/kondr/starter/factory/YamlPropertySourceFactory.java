package com.kondr.starter.factory;

import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;

/**
 * {@link PropertySourceFactory}, позволяющая загружать YAML-файлы в качестве источников свойств для
 * аннотации {@link org.springframework.context.annotation.PropertySource}.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

  // Spring сам делает Asserts и бросает понятные исключения
  @SuppressWarnings("ConstantConditions")
  @NonNull
  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
    Resource resource = encodedResource.getResource();

    YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(resource);

    String actualName = name != null ? name : resource.getFilename();
    Properties properties = factory.getObject();

    return new PropertiesPropertySource(actualName, properties);
  }

}