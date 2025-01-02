## Parity Check

To be able to validate the response object from the application, Pibify provides a Parity Check feature.

## Parity Check Listener

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
   `JavaxJsonResponseFilter`. This filter needs an instance of `IParityChecker` and `AbstractPibifySampler` to be
   passed.

#### IParityChecker

This class holds the logic of running the parity check. It expects an implementation of `IParityCheckerListener`
for triggering parity check status callbacks and a supplier for request context to be passed.
The `PibifyHandlerCache` is needed to run a cycle of serde for the actual parity check.

```java
IParityChecker parityChecker = new PibifyParityChecker(PibifyHandlerCache.getInstance(), new IParityCheckerListener() {...
}, Optional.of(() -> null), new AbstractPibifySampler() {...
});
```

#### AbstractPibifySampler

This class holds the logic of sampling the request for parity check. Implementation expect a sample % to be returned in
a call back method.

#### IParityCheckerListener

This interface contains the callbacks for the parity check results. The methods are called with the expected and actual
objects and the result of the comparison.
Implementations of this class can hook up to the respective metric/logging systems to emit the results.

### Implementation in Vert.x

1. Add the maven dependency for assertJ

```xml

<dependency>
   <groupId>org.assertj</groupId>
   <artifactId>assertj-core</artifactId>
   <version>3.26.3</version>
</dependency>
```

2. Pass the parity checker instance to the decorator

```java
IParityChecker parityChecker = new PibifyParityChecker(PibifyHandlerCache.getInstance(), new IParityCheckerListener() {...
},
        null, new AbstractPibifySampler() {...
});
```

## Parity Check Resource

This resource is exposed to help visualize the proto returned from the server.
The proto can be sent back to server along with the FQDN of the class to deserialize the proto.

### Implementation in Dropwizard

1. Register the appropriate resource by instance in the Dropwizard application - `JakartaParityCheckResource` or
   `JavaxParityCheckResource`

```java
environment.jersey().

register(new JakartaParityCheckResource(PibifyHandlerCache.getInstance()));
```

2. Curl for storing the proto on local

```
curl http://localhost:8080/sample -H 'Accept:application/proto' --output protoout.bin
```

3. Curl for sending the proto to server

```
curl --request POST --data-binary "@protoout.bin" 
         http://localhost:8080/pibify/paritychecker?fqdn=com.flipkart.pibify.resources.SampleResponse 
         -H 'Content-Type:application/octet-stream' 
```