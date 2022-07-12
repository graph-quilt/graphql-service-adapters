package com.intuit.graphql.adapter.rest.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.intuit.graphql.adapter.rest.MapBasedServiceConfiguration;
import graphql.GraphQLContext;
import java.io.IOException;
import org.springframework.http.HttpCookie;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.context.Context;

public class TestUtil {

  public static final int TIMEOUT = 1000;

  public static WebClient webClient = WebClient.builder().build();

  public static String loadResourceFromFile(String path) {
    try {
      return Resources
          .toString(Resources.getResource(path), Charsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e.getCause());
    }
  }

  public static MapBasedServiceConfiguration createServiceConfiguration(int timeout, int port) {
    MapBasedServiceConfiguration mapBasedServiceConfiguration = new MapBasedServiceConfiguration(null);
    mapBasedServiceConfiguration.add("timeout", Integer.toString(timeout));
    mapBasedServiceConfiguration.add("endpoint", "http://localhost:" + port);
    return mapBasedServiceConfiguration;
  }

  public static ServerRequest createServerRequest() {
    return MockServerRequest.builder()
        .cookie(new HttpCookie("testCookie", "testCookieValue"))
        .cookie(new HttpCookie("noValueCookie", ""))
        .build();
  }

  public static GraphQLContext createGraphQLContext( ServerRequest serverRequest) {
    return GraphQLContext.newContext()
        .of(Context.class, Context.empty())
        .of(ServerRequest.class, serverRequest)
        .build();
  }

}
