package com.intuit.graphql.adapter.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Test;

public class ServiceAdapterExceptionTest {

  @Test
  public void createdNewServiceEvaluatorException() {
    String message = "Some Error Message";
    NullPointerException nullPointerException = new NullPointerException("Null");
    ServiceAdapterException serviceAdapterException = new ServiceAdapterException(message, nullPointerException);
    assertThat(serviceAdapterException).hasMessage(message);
  }

}
