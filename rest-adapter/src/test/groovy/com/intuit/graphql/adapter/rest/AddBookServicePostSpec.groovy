package com.intuit.graphql.adapter.rest

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.intuit.graphql.adapter.core.ServiceAdapterRequest
import com.intuit.graphql.adapter.rest.utils.TestUtil
import graphql.GraphQLContext
import groovy.json.JsonSlurper
import org.springframework.web.reactive.function.server.ServerRequest
import spock.lang.Specification

class AddBookServicePostSpec extends Specification {

    public static String addBookDSL = '''
        Service service as addBooks method POST  {
          Url -> @Config("endpoint")
          Path -> ${"/books"}
          Timeout -> ${@Config("timeout")}
          @Header accept -> ${"application/json"}
          @Header "user_channel" -> ${requestContext.headers.user_channel}
          @Body -> ${requestContext.arguments.newBook}
        }
        
        Service service as addBooksWithBody method POST {
          Url -> @Config("endpoint")
          Path -> ${"/books"}
          Timeout -> ${@Config("timeout")}
          @Header accept -> ${"application/json"}
          @Header "user_channel" -> ${requestContext.headers.user_channel}
          @Body newBook -> ${requestContext.arguments.newBook}
          @Body newBookId -> ${requestContext.arguments.newBook.id}
          @Body chapters -> ${requestContext.arguments.newBook.chapters}
          @Body price -> ${requestContext.arguments.newBook.price}
          @Body isPublished -> ${requestContext.arguments.newBook.isPublished}
          @Body isHardPrint -> ${requestContext.arguments.newBook.isHardPrint}
          @Body author -> ${requestContext.arguments.newBook.author1}
        }
        
        Service service as addBooksNoKeyBody method POST {
          Url -> @Config("endpoint")
          Path -> ${"/custombody/nokey/books"}
          Timeout -> ${@Config("timeout")}
          @Header accept -> ${"application/json"}
          @Header "user_channel" -> ${requestContext.headers.user_channel}
          @Body -> ${requestContext.arguments.newBook}
        }
        
        Service service as addBooksPUT method PUT {
          Url -> @Config("endpoint")
          Path -> ${"/books"}
          Timeout -> ${@Config("timeout")}
          @Header accept -> ${"application/json"}
          @Header "user_channel" -> ${requestContext.headers.user_channel}
          @Body -> ${requestContext.arguments.newBook}
        }'''

    public static final ServiceAdapterRequest addBookServiceAdapterRequest;

    static {
        ServerRequest serverRequest = TestUtil.createServerRequest()
        GraphQLContext testGraphQLContext = TestUtil.createGraphQLContext(serverRequest)
        def jsonSlurper = new JsonSlurper()
        def addBookSingleArgument = jsonSlurper.parseText('''
            {   
                "newBook": {
                    "id": "book-1",
                    "name": "The Book",
                    "price": 100.00,
                    "chapters": ["Chapter-1", "Chapter-2", "Chapter-3"],
                    "isPublished": true,
                    "isHardPrint": false,
                    "author": null
                }  
            }
        ''')

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestContextNode = objectMapper.createObjectNode();
        requestContextNode.replace("arguments", objectMapper.valueToTree(addBookSingleArgument))

        Map<String, JsonNode> inputMap = new HashMap<>()
        inputMap.put("requestContext", requestContextNode)

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
