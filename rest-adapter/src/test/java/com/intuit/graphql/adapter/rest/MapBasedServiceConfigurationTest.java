package com.intuit.graphql.adapter.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class MapBasedServiceConfigurationTest {


  @Test
  public void testCreationWithNull() {
    MapBasedServiceConfiguration mapBasedServiceConfiguration = new MapBasedServiceConfiguration(null);
    assertThat(mapBasedServiceConfiguration).isNotNull();
    mapBasedServiceConfiguration.add("foo", "bar");
    mapBasedServiceConfiguration.add("bar", "foo");
    assertThat(mapBasedServiceConfiguration.get("foo")).isEqualTo("bar");
    assertThat(mapBasedServiceConfiguration.get("bar")).isEqualTo("foo");
  }

  @Test
  public void testCreationWithPropertyMap() {
    Map<String, String> propertyMap = new HashMap<>();
    propertyMap.put("foo", "bar");
    propertyMap.put("bar", "foo");
    MapBasedServiceConfiguration mapBasedServiceConfiguration = new MapBasedServiceConfiguration(propertyMap);
    assertThat(mapBasedServiceConfiguration).isNotNull();
    assertThat(mapBasedServiceConfiguration.get("foo")).isEqualTo("bar");
    assertThat(mapBasedServiceConfiguration.get("bar")).isEqualTo("foo");
  }

}
