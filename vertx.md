Follow the below steps to integrate `Pibify` with Vert.x

1. Update the model classes with the `@Pibify` annotation on all the required fields
2. Follow the `Usage` steps in the [Readme](README.md)
3. In the application code where new routes are created, Use the decorator

```java

// Old Code
Router router = Router.router(vertx);

// to be replaced with
// PibifyHandlerCache will be generated as part of the code gen
Router router = PibifyDecoratedRouter.decorate(Router.router(vertx), PibifyHandlerCache.getInstance());
```

The method `PibifyDecoratedRouter.decorate` optionally takes an instance of `AbstractPibifySampler` to wire up a
sampler.
The sampler lets you control whether Pibify is enabled and if yes, at what percentage of requests.

4. Do this for all the routes where you want Pibify to be enabled
5. Hitting the api with the header `Accept:application/proto` will trigger the Pibify serializers to send the response.
   Missing header or any other value will take to the default behavior.