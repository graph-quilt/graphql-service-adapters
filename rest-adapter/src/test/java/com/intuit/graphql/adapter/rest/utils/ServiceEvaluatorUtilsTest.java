package com.intuit.graphql.adapter.rest.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.graphql.adapter.core.ServiceAdapterResponse;
import org.junit.Test;

public class ServiceEvaluatorUtilsTest {

  @Test
  public void toServiceAdapterResponse() throws JsonProcessingException {
    ServiceAdapterResponse serviceAdapterResponse = ServiceEvaluatorUtils.toServiceAdapterResponse("field", "{}");
    assertThat(serviceAdapterResponse).isNotNull();
    assertThat(serviceAdapterResponse.getData()).isNotNull();
  }

  @Test
  public void toServiceAdapterResponseNull() throws JsonProcessingException {
    ServiceAdapterResponse serviceAdapterResponse = ServiceEvaluatorUtils.toServiceAdapterResponse("field", null);
    assertThat(serviceAdapterResponse).isNotNull();
    assertThat(serviceAdapterResponse.getData()).isNull();
  }

  @Test
  public void toServiceAdapterResponseException() {
    assertThatThrownBy(
        ()->ServiceEvaluatorUtils.toServiceAdapterResponse("field", "{bad}"))
    .isInstanceOf(JsonParseException.class);

  }
}
