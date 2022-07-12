# Config JSON Specification


|Field|Type|Required|Description|
|-------|------|--------|-----------|
|namespace|String|YES|Service namespace|
|appId|String|YES|Service application ID from devportal|
|type|String|YES| **`graphql-sdl`** for SDL based registry <br/> **`rest`** for Rest Adapter <br/> **`graphql-federation`** for Federation service |
|environments|Map of environment|YES|The key will the environment name(QA,E2E,PERF,PROD) and the value will be environment specific configuration mentioned below.  |

### Environment Specific Configuration

|Field|Type|Required|Description|
|-------|------|--------|-----------|
|endpoint|String|YES|Service graphql endpoint to call|
|timeout|Long|NO|Timeout setting between orchestrator and service provider. The maximum allowed value is 10sec. If not defined, the value fefaults to 10 sec |
|uswest2| environment|NO|US West Specific environment specificaion|
|useast2| environment|NO|US East Specific environment specificaion|


### Sample config.json

```json
{
  "namespace":"PET",
  "appId":"Test.appId",
  "type":"graphql-sdl",
  "environments": {
    "QA":{
      "endpoint":"https://petstorehost-qal.api.intuit.com/graphql",
      "timeout": 4000,
    },
    "E2E":{
      "endpoint":"https://petstorehost-dev.api.intuit.com/graphql",
      "timeout": 4000,
      "uswest2" : {
        "endpoint": "https://petstorehost-us-west-2-e2e.api.intuit.com/graphql"
      },
      "useast2" : {
        "endpoint": "https://petstorehost-us-east-2-e2e.api.intuit.com/graphql"
      }
    },
    "PERF":{
      "endpoint":"https://petstorehost-perf.api.intuit.com/graphql",
      "timeout": 4000
    },
    "PROD-STG":{
      "endpoint":"https://petstorehost-prd-stg.api.intuit.com/graphql",
      "timeout": 4000
    },
    "PROD":{
      "endpoint":"https://petstorehost.api.intuit.com/graphql",      
      "timeout": 4000,
      "uswest2" : {
        "endpoint": "https://petstorehost-us-west-2.api.intuit.com/graphql"
      },
      "useast2" : {
        "endpoint": "https://petstorehost-us-east-2.api.intuit.com/graphql"
      }
    }
  }
}  
```
