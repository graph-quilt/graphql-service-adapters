<div align="center">

  ![graphql-service-adapter](./graphql_service_adapters.png)

</div>

[![ Master Build and Publish](https://github.com/intuit/graphql-service-adapters/actions/workflows/master.yml/badge.svg?branch=master&event=push)](https://github.com/intuit/graphql-service-adapters/actions/workflows/master.yml)
[![codecov](https://codecov.io/gh/intuit/graphql-service-adapters/branch/master/graph/badge.svg?token=G392PV1BAI)](https://codecov.io/gh/intuit/graphql-service-adapters) 
[![Apache 2](http://img.shields.io/badge/license-Apache%202-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0) <br/>


### Overview
This library provides adapters as an abstraction layer that enables any Graphql service to consume data from various downstream services (like REST, gRPC, etc) in 
pure GraphQL format.  
It currently supports following adapters: 
* [REST service adapter](mkdocs/docs/rest-adapter.md)
* *gRPC service adapter* (Work In Progress) 


### Pre-requisites

Make sure you have the following installed on your machine

* jdk 1.8
* maven

### Intellij development

* [Steps to enable lombok in IntelliJ](https://www.baeldung.com/lombok-ide)
* IntelliJ style guide is available in `src/format/intellij-styleguide.xml`. 

## Contributing

Please see our [contribution guide](.github/CONTRIBUTING.md)
