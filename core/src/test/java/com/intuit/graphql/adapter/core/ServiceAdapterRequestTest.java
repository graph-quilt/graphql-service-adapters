package com.intuit.graphql.adapter.core;

import static graphql.schema.DataFetchingEnvironmentImpl.newDataFetchingEnvironment;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.ImmutableMap;
import graphql.ExecutionInput;
import graphql.GraphQLContext;
import graphql.Scalars;
import graphql.execution.MergedField;
import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.OperationDefinition.Operation;
import graphql.language.SelectionSet;
import graphql.parser.Parser;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLFieldDefinition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.junit.Test;

public class ServiceAdapterRequestTest {

  private static Parser parser = new Parser();

  @Test
  public void canCreateFromQueryWithNoArguments() {
    String query = "query GetQuery { books { ... bookFragment }} fragment bookFragment "
        + "on BOOKS_Book { id name  author { lastName petId } pageCount weight isFamilyFriendly }";

    ExecutionInput getBookQuery = ExecutionInput
        .newExecutionInput().query(query)
        .build();

    SelectionSet selectionSet = SelectionSet.newSelectionSet()
        .selection(Field.newField("id").build())
        .selection(Field.newField("name").build()).build();

    Field topLevelField = Field.newField("books").selectionSet(selectionSet).build();

    GraphQLFieldDefinition fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
        .name("books")
        .type(Scalars.GraphQLID) // just for testing
        .build();

    DataFetchingEnvironment dfe = newDataFetchingEnvironment()
        .arguments(Collections.emptyMap())
        .mergedField(MergedField.newMergedField(topLevelField).build())
        .operationDefinition(
            OperationDefinition.newOperationDefinition().name("query").operation(Operation.QUERY).build())
        .fieldDefinition(fieldDefinition)
        .build();

    GraphQLContext graphQLContext = GraphQLContext.newContext()
        .of(Document.class, parser.parseDocument(query))
        .of(DataFetchingEnvironment.class, dfe)
        .build();

    ServiceAdapterRequest serviceAdapterRequest = ServiceAdapterRequest
        .from(new HashMap<>(), graphQLContext, "GraphQL.Query");
    assertThat(serviceAdapterRequest.getRequestedField()).isEqualTo("books");
    assertThat(serviceAdapterRequest.getOperation()).isEqualTo("query");
    assertThat(serviceAdapterRequest.getAdapter().isPresent()).isFalse();
    assertThat(MapUtils.isEmpty(serviceAdapterRequest.getInputMap())).isTrue();
  }

  @Test
  public void canCreateFromQueryWithAdapterWithoutArgument() {
    String query = "query GetQuery { books { ... bookFragment }} fragment bookFragment "
        + "on BOOKS_Book { id name  author { lastName petId } pageCount weight isFamilyFriendly }";

    ExecutionInput getBookQuery = ExecutionInput
        .newExecutionInput().query(query)
        .build();

    SelectionSet selectionSet = SelectionSet.newSelectionSet()
        .selection(Field.newField("id").build())
        .selection(Field.newField("name").build()).build();

    Field topLevelField = Field.newField("books").selectionSet(selectionSet).build();

    GraphQLFieldDefinition fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
        .name("books")
        .withDirective(GraphQLDirective.newDirective().name("adapter").build())
        .type(Scalars.GraphQLID) // just for testing
        .build();

    DataFetchingEnvironment dfe = newDataFetchingEnvironment()
        .arguments(Collections.emptyMap())
        .mergedField(MergedField.newMergedField(topLevelField).build())
        .operationDefinition(
            OperationDefinition.newOperationDefinition().name("query").operation(Operation.QUERY).build())
        .fieldDefinition(fieldDefinition)
        .build();

    GraphQLContext graphQLContext = GraphQLContext.newContext()
        .of(Document.class, parser.parseDocument(query))
        .of(DataFetchingEnvironment.class, dfe)
        .build();

    ServiceAdapterRequest serviceAdapterRequest = ServiceAdapterRequest
        .from(new HashMap<>(), graphQLContext, "GraphQL.Query");
    assertThat(serviceAdapterRequest.getRequestedField()).isEqualTo("books");
    assertThat(serviceAdapterRequest.getOperation()).isEqualTo("query");
    assertThat(serviceAdapterRequest.getAdapter().isPresent()).isFalse();
    assertThat(MapUtils.isEmpty(serviceAdapterRequest.getInputMap())).isTrue();
  }

  @Test
  public void canCreateFromQueryWithAdapterWithArgument() {
    String query = "query GetQuery { iamAnAlias: books { ... bookFragment }} fragment bookFragment "
        + "on BOOKS_Book { id name  author { lastName petId } pageCount weight isFamilyFriendly }";

    ExecutionInput getBookQuery = ExecutionInput
        .newExecutionInput().query(query)
        .build();

    SelectionSet selectionSet = SelectionSet.newSelectionSet()
        .selection(Field.newField("id").build())
        .selection(Field.newField("name").build()).build();

    Field topLevelField = Field.newField("books").selectionSet(selectionSet).alias("iamAnAlias").build();

    GraphQLFieldDefinition fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
        .name("books")
        .withDirective(GraphQLDirective.newDirective().name("adapter")
            .argument(GraphQLArgument.newArgument()
                .name("service")
                .type(Scalars.GraphQLString)
                .value("foo")
                .build())
            .build())
        .type(Scalars.GraphQLID) // just for testing
        .build();

    DataFetchingEnvironment dfe = newDataFetchingEnvironment()
        .arguments(Collections.emptyMap())
        .mergedField(MergedField.newMergedField(topLevelField).build())
        .operationDefinition(
            OperationDefinition.newOperationDefinition().name("query").operation(Operation.QUERY).build())
        .fieldDefinition(fieldDefinition)
        .build();

    GraphQLContext graphQLContext = GraphQLContext.newContext()
        .of(Document.class, parser.parseDocument(query))
        .of(DataFetchingEnvironment.class, dfe)
        .build();

    ServiceAdapterRequest serviceAdapterRequest = ServiceAdapterRequest
        .from(new HashMap<>(), graphQLContext, "GraphQL.Query");
    assertThat(serviceAdapterRequest.getRequestedField()).isEqualTo("iamAnAlias");
    assertThat(serviceAdapterRequest.getOperation()).isEqualTo("query");
    assertThat(serviceAdapterRequest.getAdapter().isPresent()).isTrue();
    assertThat(serviceAdapterRequest.getAdapter().get()).isEqualTo("foo");
    assertThat(MapUtils.isEmpty(serviceAdapterRequest.getInputMap())).isTrue();
  }

  @Test
  public void canCreateFromQueryWithVariable() {
    String query =
        "query GetQuery($bookIdVar : String!) { books(id : $bookIdVar) { ... bookFragment }} fragment bookFragment "
            + "on BOOKS_Book { id name  author { lastName petId } pageCount weight isFamilyFriendly }";

    ExecutionInput getBookQuery = ExecutionInput
        .newExecutionInput().query(query)
        .variables(ImmutableMap.of("bookIdVar", "book-1"))
        .build();

    SelectionSet selectionSet = SelectionSet.newSelectionSet()
        .selection(Field.newField("id").build())
        .selection(Field.newField("name").build()).build();

    Field topLevelField = Field.newField("books").selectionSet(selectionSet).build();

    GraphQLFieldDefinition fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
        .name("books")
        .type(Scalars.GraphQLID) // just for testing
        .build();

    DataFetchingEnvironment dfe = newDataFetchingEnvironment()
        .arguments(new HashMap<String, Object>() {{
          put("id", "foo");
        }})
        .fieldDefinition(fieldDefinition)
        .operationDefinition(
            OperationDefinition.newOperationDefinition().name("query").operation(Operation.QUERY).build())
        .mergedField(MergedField.newMergedField(topLevelField).build())
        .build();

    GraphQLContext graphQLContext = GraphQLContext.newContext()
        .of(Document.class, parser.parseDocument(query))
        .of(DataFetchingEnvironment.class, dfe)
        .build();

    ObjectNode objectNode = new ObjectMapper().createObjectNode();
    objectNode.replace("id", new TextNode("foo"));

    Map<String,JsonNode> requestContext = new HashMap<>();
    requestContext.put("requestContext", objectNode);

    ServiceAdapterRequest serviceAdapterRequest = ServiceAdapterRequest
        .from(requestContext, graphQLContext, "GraphQL.Query");
    assertThat(serviceAdapterRequest.getRequestedField()).isEqualTo("books");
    assertThat(serviceAdapterRequest.getOperation()).isEqualTo("query");
    assertThat(MapUtils.isEmpty(serviceAdapterRequest.getInputMap())).isFalse();
    assertThat(serviceAdapterRequest.getInputMap().get("requestContext").get("id").asText()).isEqualTo("foo");
    assertThat(serviceAdapterRequest.getAdapter().isPresent()).isFalse();

  }

  @Test(expected = NullPointerException.class)
  public void cannotCreateFromQueryWithNullGraphQLContext() {
    ServiceAdapterRequest.from(new HashMap<>(), null, "GraphQL.Query");
  }
}
