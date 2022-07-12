package com.intuit.graphql.adapter.rest;

import com.intuit.service.dsl.evaluator.ServiceConfiguration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration lookup help per Service Provider.
 */
public class MapBasedServiceConfiguration implements ServiceConfiguration {

  private final Map<String, String> properties = new HashMap<>();

  public MapBasedServiceConfiguration(Map<String, String> properties) {
    if (properties != null)
      this.properties.putAll(properties);
  }

  public void add(String key, String value) {
    properties.put(key, value);
  }

  public String get(String propertyName) {
    return this.properties.get(propertyName);
  }
}