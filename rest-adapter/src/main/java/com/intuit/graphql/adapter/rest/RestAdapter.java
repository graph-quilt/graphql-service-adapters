package com.intuit.graphql.adapter.rest;
import static com.intuit.graphql.adapter.rest.utils.ServiceEvaluatorUtils.toServiceAdapterResponse;
import com.intuit.graphql.adapter.core.ServiceAdapter;
import com.intuit.graphql.adapter.core.ServiceAdapterException;
import com.intuit.graphql.adapter.core.ServiceAdapterRequest;
import com.intuit.graphql.adapter.core.ServiceAdapterResponse;
import com.intuit.service.dsl.evaluator.ServiceConfiguration;
import com.intuit.service.dsl.evaluator.ServiceEvaluator;
import com.intuit.service.dsl.evaluator.ServiceEvaluatorRequest;
import graphql.GraphQLContext;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.Builder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.util.context.Context;

/**
 * GraphQL to REST adapter.
 */
public class RestAdapter implements ServiceAdapter {

  private final ServiceConfiguration serviceConfiguration;
  private final WebClient webClient;
  private final String serviceId;
  private final ServiceLoader serviceLoader;

  @Builder
  private RestAdapter(String dslResource, String serviceId,
      ServiceConfiguration svcConfig, WebClient webClient) {
    Objects.requireNonNull(dslResource, "dslResource");
    Objects.requireNonNull(serviceId, "serviceId");
    Objects.requireNonNull(svcConfig, "svcConfig");
    Objects.requireNonNull(webClient, "webClient");
    this.serviceConfiguration = svcConfig;
    this.webClient = webClient;
    this.serviceId = serviceId;
    this.serviceLoader = new ServiceLoader(dslResource);
  }

  @Override
  public CompletableFuture<ServiceAdapterResponse> execute(ServiceAdapterRequest request) {
    GraphQLContext graphQLcontext = request.getGraphQLContext();

    ServerRequest serverRequest = graphQLcontext.get(ServerRequest.class);
    Objects.requireNonNull(serverRequest, "ServerRequest is null");

    Context reactorContext = graphQLcontext.get(Context.class);
    Objects.requireNonNull(reactorContext, "ReactorContext is null");

    ServiceEvaluatorRequest serviceEvaluatorRequest =
        ServiceEvaluatorRequest.builder()
            .inputMap(request.getInputMap())
            .reactorContext(reactorContext)
            .serviceName(request.getAdapter())
            .service(serviceLoader.getService(request.getAdapter().orElse(null)))
            .build();

    return ServiceEvaluator.builder()
        .serviceConfiguration(serviceConfiguration)
        .webClient(webClient)
        .build()
        .evaluate(serviceEvaluatorRequest)
        .handle((serviceEvaluatorResponse, throwable) -> {
          if (Objects.nonNull(throwable)) {
            String message = "Error calling rest provider. ";
            throw new ServiceAdapterException(message + throwable.getMessage(), throwable);
          }
          try {
            return toServiceAdapterResponse(request.getRequestedField(), serviceEvaluatorResponse);
          } catch (Throwable ex) {
            String message = "Error processing json response. ";
            throw new ServiceAdapterException(message + ex.getMessage(), ex);
          }
        });
  }

}
