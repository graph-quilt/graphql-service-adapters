package com.intuit.graphql.adapter.rest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides a reusable jackson's {@link ObjectMapper} instance
 */
public class JsonUtils {

  private JsonUtils() {
  }

  public static final ObjectMapper MAPPER = new ObjectMapper();
}
