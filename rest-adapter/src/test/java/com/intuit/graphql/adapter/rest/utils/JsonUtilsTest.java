package com.intuit.graphql.adapter.rest.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Test;

public class JsonUtilsTest {

  @Test
  public void assertMapper() {
    assertThat(JsonUtils.MAPPER).isNotNull();
  }

}
