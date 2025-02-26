Follow the below steps to integrate `Pibify` with Dropwizard

1. Update the model classes with the `@Pibify` annotation on all the required fields using the mvn goal `annotate`. Refer to #2. 
2. Follow the `Usage` steps in the [Readme](README.md)
3. Register the Provider `PibifyMessageBodyWriter`, just like any other Resource/Filter is registered. There are 2 versions based on the version of dropwizard - either pick `JavaxPibifyMessageBodyWriter` or `JakartaPibifyMessageBodyWriter`

```java
// PibifyHandlerCache will be generated as part of the code gen
environment.jersey().register(new JavaxPibifyMessageBodyWriter(PibifyHandlerCache.getInstance()));
```

The constructor of `JavaxPibifyMessageBodyWriter` optionally takes an instance of `AbstractPibifySampler` to wire up a
sampler.
The sampler lets you control whether Pibify is enabled and if yes, at what percentage of requests.

```java

4. Update all the Resource classes/methods to add the `@Produces("application/proto")` annotation

```java
@Produces({MediaType.APPLICATION_JSON, "application/proto"})
```

5. Hitting the api with the header `Accept:application/proto` will trigger the Pibify serializers to send the response.
   Missing header or any other value will take to the default behavior.
