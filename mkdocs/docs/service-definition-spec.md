The Service Adapter configuration uses a Domain Specific Language(DSL) to specify how to call the downstream service.  

It has the following syntax:

```java
Service service as <SERVICE_ALIAS_NAME> method <HTTP_METHOD>  {  
    Url -> @Config(“endpoint”)  
    Path -> ${"<RESOURCE_PATH>"}  
    Timeout -> ${@Config("timeout")}  
    @PathParam <PATH_PARAM_NAME> -> ${requestContext.arguments.<PATH_TO_ARGUMENT_NAME>}  
    //...
    @Query <QUERY_PARAM_NAME> -> ${requestContext.arguments.<PATH_TO_ARGUMENT_NAME>}  
    //...
    @Header <HEADER_NAME> -> <HEADER_VALUE> 
    //... 
    @Body -> ${requestContext.arguments.<PATH_TO_ARGUMENT_NAME>}
}  
```

Example:

```java
Service service as ServiceClient method GET {
  Url -> @Config("endpoint")
  Path -> ${"/books/{bookId}/{author}"}
  Timeout -> ${@Config("timeout")}
  @PathParam bookId -> ${requestContext.arguments.id}
  @PathParam author -> ${requestContext.arguments.author}
  @Query edition -> ${"latest"}
  @Header Accept -> ${"application/json"}
  @Header "user_channel" -> ${requestContext.headers.user_channel}
}
```

**Service**

The Service element is the start of the Service Definition.  It is followed 
by service name and service alias name.

**method**

The method element specifies the method to use.  For now, only `GET`, `PUT` and `POST`is supported.

**Url**

The Url element specifies the base URL to be used.   

The downstream service endpoints are defined in`config.json`.  Use `@Config(“endpoint”)` to refer to the 
downstream `endpoint` where an **active** dapi environment will connect to.  Example: 

`Url -> @Config("endpoint")`

**Path**

This specifies the resource path which will be concatenated with the **Url**.  The 
resource path MUST start with `/`.  The path may include a path parameter which is enclosed in curly braces: `{` and `}`.


**Timeout**

This element specifies the timeout in milli seconds for the HTTP call.  One can specify a  
a numeric **String literal** or refer to the `timeout` property for the **active** environment by specifying 
`@Config(“timeout”)`.  The `timeout` property is defined per environment in `config.json` .

**@Query**

This element allows one to define query parameter(s) that will be sent to the downstream service.  
The value can be a string literal or a reference to a graphql argument  which accessible from `requestContext.arguments`.  

The `@Query` element may be specified more than 1 or none.

**@PathParam**

This element allows one to specify the value for path parameter(s) specified in 
the resource path.  The value can be a string literal or a reference to a graphql 
argument  which accessible from `requestContext.arguments`.  

The `@PathParam` element may be specified more than 1 or none.

**@Body**

This element allows one to specify the value for request body to be sent
to the downstream service. The value can be a reference to a graphql 
argument which is accessible from `requestContext.arguments`.  

**@Header**

This element is used to specify the header(s) to be sent along with the request.  The 
header name, which is case sensitive, is arbitrary, and the header value can be a string literal or referred from the `requestContext`.

The `Accept` header must be defined with `application/json`, i.e:

```
@Header Accept -> ${"application/json"}
```

For multi-word header names, double quotes should be used:

```
@Header "Content-Type" -> ${"application/json"}
```

The @Header element may be specified more than 1.  

## HTTP Request Body Construction

Request body is constructed automatically (without @Body) or declaratively(with @Body).

### Service evaluator with GET as method
if @Body is not present, the body will be empty.  

@Body annotation may be used for Service Evaluator with GET method.  See @Body section above.

### Service evaluator with POST or PUT as method
If @Body is not present, only one field argument, in the mutation field definition, is supported.  Otherwise, 
a `ServiceEvaluatorException` will be thrown.  The adapter will construct the body like so:

GraphQL Query:
```
mutation {
    field(fieldArgName: fieldArgValue)
}  
```

Corresponding HTTP POST Request Body
```
fieldArgValue
```

where `fieldArgValue` can be a scalar or an object.  if `fieldArgValue` is an object, it will be represented as JSON.
