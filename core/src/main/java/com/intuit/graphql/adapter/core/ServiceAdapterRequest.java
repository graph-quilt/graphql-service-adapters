package com.intuit.graphql.adapter.core;

import com.fasterxml.jackson.databind.JsonNode;
import graphql.ExecutionInput;
import graphql.GraphQLContext;
import graphql.language.Field;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A representation of a GraphQL sub-query for a downstream provider that will be used by a {@link ServiceAdapter} for
 * request transformation.
 */
@Getter
@Builder
public class ServiceAdapterRequest {

  public static final String ADAPTER = "adapter";
  public static final String ARGUMENT_SERVICE = "service";
  @NonNull
  private String operation;

  @NonNull
  private String requestedField;

  @NonNull
  private Map<String, JsonNode> inputMap;

  @NonNull
  private GraphQLContext graphQLContext;

  @NonNull
  private String requestType;
  @Builder.Default
  private Optional<String> adapter = Optional.empty();


  /**
   * Creates an instance of this class from {@link ExecutionInput}
   *
   * @param inputMap GraphQL execution input
   * @param graphQLContext GraphQL Context
   * @param requestType an arbitrary value to identify the type of request.
   * @return an instance of {@link ServiceAdapterRequest}
   */
  public static ServiceAdapterRequest from(final Map<String, JsonNode> inputMap, GraphQLContext graphQLContext,
      String requestType) {

    DataFetchingEnvironment dfe = graphQLContext.get(DataFetchingEnvironment.class);
    Field field = dfe.getField();

    return ServiceAdapterRequest
        .builder()
        .operation(dfe.getOperationDefinition().getOperation().name().toLowerCase())
        .requestedField(field.getAlias() != null? field.getAlias(): field.getName())
        .inputMap(inputMap)
        .graphQLContext(graphQLContext)
        .adapter(getAdapter(dfe.getFieldDefinition()))
        .requestType(requestType)
        .build();
  }

  private static Optional<String> getAdapter(GraphQLFieldDefinition definition) {
    return definition.getDirectives().stream()
        .filter(directive -> ADAPTER.equals(directive.getName()))
        .findFirst()
        .map(directive -> directive.getArgument(ARGUMENT_SERVICE))
        .map(graphQLArgument -> graphQLArgument.getValue().toString());
  }

}