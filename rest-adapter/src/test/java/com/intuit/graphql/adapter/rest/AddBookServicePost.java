package com.intuit.graphql.adapter.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intuit.graphql.adapter.core.ServiceAdapterRequest;
import com.intuit.graphql.adapter.rest.utils.TestUtil;
import graphql.GraphQLContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.reactive.function.server.ServerRequest;


public class AddBookServicePost {

  public static String addBookDSL;

  static final ServiceAdapterRequest addBookServiceAdapterRequest;

  static {
    ServerRequest serverRequest = TestUtil.createServerRequest();
    addBookDSL = TestUtil.loadResourceFromFile("addBook/service.service");

    GraphQLContext testGraphQLContext = TestUtil.createGraphQLContext(serverRequest);

    List<String> chapters = Arrays.asList("Chapter-1", "Chapter-2", "Chapter-3");

    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, Object> newBook = new HashMap<>();
    newBook.put("id", "book-1");
    newBook.put("name", "The Book");
    newBook.put("price", BigDecimal.valueOf(100.00)); // graphQL uses BigDecimal
    newBook.put("chapters", chapters); // graphQL uses BigDecimal
    newBook.put("isPublished", Boolean.TRUE); // graphQL uses BigDecimal
    newBook.put("isHardPrint", Boolean.FALSE); // graphQL uses BigDecimal
    newBook.put("author", null);// graphQL uses BigDecimal

    ObjectNode addBookSingleArgument = objectMapper.createObjectNode();
    addBookSingleArgument.replace("newBook", objectMapper.valueToTree(newBook));

    ObjectNode requestContextNode = objectMapper.createObjectNode();
    requestContextNode.replace("arguments", addBookSingleArgument);

    Map<String, JsonNode> inputMap = new HashMap<>();
    inputMap.put("requestContext", requestContextNode);

    addBookServiceAdapterRequest = ServiceAdapterRequest.builder()
        .adapter(Optional.of("addBooks"))
        .inputMap(inputMap)
        .graphQLContext(testGraphQLContext)
        .operation("mutation")
        .requestedField("addBook")
        .requestType("GraphQL.Mutation")
        .build();
  }

}
