export type AmplifyDependentResourcesAttributes = {
  "analytics": {
    "taskmaster": {
      "Id": "string",
      "Region": "string",
      "appName": "string"
    }
  },
  "api": {
    "taskmaster": {
      "GraphQLAPIEndpointOutput": "string",
      "GraphQLAPIIdOutput": "string",
      "GraphQLAPIKeyOutput": "string"
    }
  },
  "auth": {
    "taskmastere11a030e": {
      "AppClientID": "string",
      "AppClientIDWeb": "string",
      "IdentityPoolId": "string",
      "IdentityPoolName": "string",
      "UserPoolArn": "string",
      "UserPoolId": "string",
      "UserPoolName": "string"
    }
  },
  "predictions": {
    "speechGeneratoreeea78d9taskmaster": {
      "language": "string",
      "region": "string",
      "voice": "string"
    },
    "translateText307a958dTaskMaster": {
      "region": "string",
      "sourceLang": "string",
      "targetLang": "string"
    }
  },
  "storage": {
    "taskmaster": {
      "BucketName": "string",
      "Region": "string"
    }
  }
}