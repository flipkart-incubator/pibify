## Parity Check

## High Level Summary

An application can run in parity check mode where the response object is intercepted before sending to the client.
The response object is run through Pibify Serializer and then deserialized back.  
This object is then compared with the expected response object is deepEquals and callbacks are triggered based on the
result.

Client can hook a `IParityCheckerListener` to get the details and emit metrics/log as needed.

### Implementation in Dropwizard

1. Add the maven dependency for assertJ

```xml

<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.26.3</version>
</dependency>
```

2. Register the appropriate filter in the Dropwizard application - `JakartaJsonResponseFilter` or
   `JavaxJsonResponseFilter`
2. Pass an instance of `PibifyParityChecker`. This would expect an implementation of `IParityCheckerListener` and a
   supplier for request context to be passed
3. Implement the `IParityCheckerListener` to get the callbacks for the comparison results