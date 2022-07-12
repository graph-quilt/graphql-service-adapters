package com.intuit.graphql.adapter.rest.utils;

import static com.intuit.graphql.adapter.rest.utils.JsonUtils.MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intuit.graphql.adapter.core.ServiceAdapterResponse;
import java.util.Objects;

public class ServiceEvaluatorUtils {

  private static final ServiceAdapterResponse NULL_SERVICE_ADAPTER_RESPONSE = ServiceAdapterResponse.builder()
      .data(null)
      .build();

  private ServiceEvaluatorUtils() {
  }

  public static ServiceAdapterResponse toServiceAdapterResponse(String requestedField,
      String serviceEvaluatorResponse) throws JsonProcessingException {

    if (Objects.isNull(serviceEvaluatorResponse)) {
      return NULL_SERVICE_ADAPTER_RESPONSE;
    }

    JsonNode responseNode = MAPPER.reader().readTree(serviceEvaluatorResponse);
    ObjectNode serviceAdapterResponse = MAPPER.createObjectNode();
    serviceAdapterResponse.replace(requestedField, responseNode);
    return ServiceAdapterResponse.builder()
        .data(serviceAdapterResponse)
        .build();
  }
}
