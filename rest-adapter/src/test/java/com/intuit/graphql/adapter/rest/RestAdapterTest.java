package com.intuit.graphql.adapter.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.intuit.graphql.adapter.core.ServiceAdapterException;
import com.intuit.graphql.adapter.core.ServiceAdapterRequest;
import com.intuit.graphql.adapter.core.ServiceAdapterResponse;
import com.intuit.graphql.adapter.rest.utils.TestUtil;
import com.intuit.service.dsl.evaluator.exceptions.ServiceDataRetrieverException;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestAdapterTest {


  private RestAdapter addBookRestAdapter;

  private static int WIREMOCK_PORT = 4050;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT);

  @Before
  public void setup() {
//    Map<String, String> addBookDSLResourcesMap = new HashMap<>();
//    addBookDSLResourcesMap.put("main/flow/service.service", AddBookServicePost.addBookDSL);

    addBookRestAdapter = RestAdapter.builder()
        .dslResource(AddBookServicePost.addBookDSL)
        .serviceId("TEST_SERVICE")
        .svcConfig(TestUtil.createServiceConfiguration(TestUtil.TIMEOUT, WIREMOCK_PORT))
        .webClient(TestUtil.webClient)
        .build();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void canExecuteRequest() throws ExecutionException, InterruptedException {
    // GIVEN
    ServiceAdapterRequest serviceAdapterRequest = AddBookServicePost.addBookServiceAdapterRequest;
    stubFor(any(urlPathMatching("/books"))
        .withRequestBody(containing("\"id\":\"book-1\""))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("{\"id\": \"book-1\",\"name\": \"The Book\",\"price\": 100}")
            .withHeader("Content-Type", "application/json;charset=UTF-8")));

    // WHEN
    ServiceAdapterResponse response = addBookRestAdapter.execute(serviceAdapterRequest).get();

    //THEN
    assertThat(response.getData()).isNotNull();
    JsonNode data = response.getData();

    assertThat(data.has("addBook")).isTrue();
    assertThat(data.size()).isEqualTo(1);
    JsonNode addBook = data.get("addBook");
    assertThat(addBook.has("id")).isTrue();
    assertThat(addBook.has("price")).isTrue();
    assertThat(addBook.has("name")).isTrue();
  }

  @Test
  public void cannotExecuteRequestDueto400HttpErr() throws InterruptedException {
    // GIVEN
    ServiceAdapterRequest serviceAdapterRequest = AddBookServicePost.addBookServiceAdapterRequest;
    stubFor(any(urlPathMatching("/books"))
        .withRequestBody(containing("\"id\":\"book-1\""))
        .willReturn(aResponse()
            .withStatus(400)
            .withHeader("Content-Type", "application/json;charset=UTF-8")));

    // WHEN
    Throwable cause = null;
    try {
      addBookRestAdapter.execute(serviceAdapterRequest).get();
    } catch (ExecutionException e) {
      cause = e.getCause();
    } finally {
      // THEN
      assertThat(cause).isInstanceOf(ServiceAdapterException.class);
    }
  }

  @Test
  public void cannotExecuteRequestDueto500HttpErr() throws InterruptedException {
    // GIVEN
    ServiceAdapterRequest serviceAdapterRequest = AddBookServicePost.addBookServiceAdapterRequest;
    stubFor(any(urlPathMatching("/books"))
        .withRequestBody(containing("\"id\":\"book-1\""))
        .willReturn(aResponse()
            .withStatus(500)
            .withHeader("Content-Type", "application/json;charset=UTF-8")));

    // WHEN
    Throwable cause = null;
    try {
      addBookRestAdapter.execute(serviceAdapterRequest).get();
    } catch (ExecutionException e) {
      cause = e.getCause();
    } finally {
      // THEN
      assertThat(cause).isInstanceOf(ServiceAdapterException.class);
    }
  }

  @Test
  public void cannotExecuteRequestDueto300HttpErr() throws InterruptedException {
    // GIVEN
    ServiceAdapterRequest serviceAdapterRequest = AddBookServicePost.addBookServiceAdapterRequest;
    stubFor(any(urlPathMatching("/books"))
        .withRequestBody(containing("\"id\":\"book-1\""))
        .willReturn(aResponse()
            .withStatus(300)
            .withHeader("Content-Type", "application/json;charset=UTF-8")));

    // WHEN
    Throwable cause = null;
    try {
      addBookRestAdapter.execute(serviceAdapterRequest).get();
    } catch (ExecutionException e) {
      cause = e.getCause();
    } finally {
      // THEN
      assertThat(cause).isInstanceOf(ServiceAdapterException.class);
    }
  }
}
