package com.intuit.graphql.adapter.core;

import java.util.concurrent.CompletableFuture;

/**
 * Base contract for an adapter to a target service.
 */
public interface ServiceAdapter {

  /**
   * transforms and sends the request to the target service.
   *
   * @param request contains GraphQL Query operation and top level fields which will be used for request
   *                transformation.
   * @return a publisher that emits a {@link ServiceAdapterResponse}
   */
  CompletableFuture<ServiceAdapterResponse> execute(ServiceAdapterRequest request);
}