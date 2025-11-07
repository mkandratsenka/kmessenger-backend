package com.kondr.starter.configuration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

/**
 * EnvironmentPostProcessor для загрузки дефолтных конфигураций из classpath и добавления их в
 * {@link ConfigurableEnvironment} приложения.
 *
 * <p><b>Отключение</b>:
 * По умолчанию поведение включено. Для отключения флаг необходимо установить на ранней стадии
 * запуска (system property / environment variable), поскольку post-processor выполняется до
 * загрузки application.yml.</p>
 *
 * <p><b>Примеры</b>:
 * <pre>
 * // системное свойство: отключить
 * -Dstarter.defaults.enabled=false
 *
 * // структура ресурсов в стартере:
 * /configurations/defaults-local.yml
 * /configurations/defaults-prod.yml
 * </pre>
 * </p>
 */
public class DefaultsEnvironmentPostProcessor implements EnvironmentPostProcessor {

  private static final String DEFAULTS_ENABLED_PROPERTY = "starter.defaults.enabled";

  private static final String DISABLED_VALUE = "false";

  private static final String LOCAL_PROFILE = "local";

  private static final String DEFAULTS_LOCATION_PATTERN = "classpath*:/configurations/defaults-%s.yml";

  private final YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
    if (DISABLED_VALUE.equalsIgnoreCase(env.getProperty(DEFAULTS_ENABLED_PROPERTY))) {
      return;
    }

    String[] profiles = env.getActiveProfiles();
    // Многопрофильный запуск пока не поддерживается и не может быть отлажен
    Assert.isTrue(profiles.length <= 1, "Multiple profiles are not supported");

    String activeProfile = profiles.length == 1 ? profiles[0] : LOCAL_PROFILE;
    String defaultsLocationPattern = DEFAULTS_LOCATION_PATTERN.formatted(activeProfile);

    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    MutablePropertySources propertySources = env.getPropertySources();

    try {
      Arrays.stream(resolver.getResources(defaultsLocationPattern))
          .filter(Resource::exists)
          .sorted(resourceComparator()) // устанавливаем детерминированный порядок
          .forEach(defaultResource -> addResource(propertySources, defaultResource));

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Comparator<Resource> resourceComparator() {
    return Comparator.comparing(resource -> {
      try {
        return resource.getURI().toString();
      } catch (IOException e) {
        throw new UncheckedIOException(
            "Failed to get URI for sorting: " + resource.getDescription(), e);
      }
    });
  }

  private void addResource(MutablePropertySources propertySources, Resource defaultResource) {
    try {
      String uniqueName = defaultResource.getURI().toString();
      List<PropertySource<?>> loadedPropertySources = yamlLoader.load(uniqueName, defaultResource);

      loadedPropertySources.forEach(propertySources::addLast);

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}