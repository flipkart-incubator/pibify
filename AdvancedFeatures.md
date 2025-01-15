This page lists down the advanced features supported by Pibify

### Multi-Module Support

Pibify generates one `PibifyCacheHandler` class per maven module where its configured.
If your maven project consists of more than 1 `model` modules, there will be need to have a combined
cache that can be used by the clients.

For this, initialize MultiModulePibifyCacheHandler with the list of `PibifyCacheHandler` instances in your code.
It takes a var-arg of `PibifyCacheHandler` instances and creates a combined cache.

```java
new MultiModulePibifyHandlerCache(
                new PibifyHandlerCache1(handler1), new

PibifyHandlerCache2(handler2));
```

Refer
to [MultiModulePibifyHandlerCacheTest.java](pibify-core/src/test/java/com/flipkart/pibify/codegen/stub/MultiModulePibifyHandlerCacheTest.java)
for more details.

### Parity Check

[Parity Check](ParityCheck.md)

### String Dictionary

If a field in a Pojo is a String and has a repeated set of values, it can be annotated with
`@Pibify(value = 1, dictionary = true)`.
This will ensure that the serialized form of the Pojo will have the dictionary values replaced with the index of the
value in the dictionary.
This can improve the serialization size of the Pojo and reduce cpu for serde ops.

### Getting an instance of PibifyCacheHandler

The `PibifyHandlerCache` is a singleton class that can be accessed by calling `getInstance()` method.
But this class is generated as part of the maven `package` and is not available in the IDE for direct import readily.
This can cause compilation issues in the IDE (although it would always work in mvn). To resolve this, clients can use
a helper method

```java
// Replace the FQDN with the actual FQDN of the PibifyHandlerCache
AbstractPibifyHandlerCache handlerCache = AbstractPibifyHandlerCache.getConcreteInstance(
                "<<FQDN.Of.PibifyHandlerCache>>");
```

This ensures that the IDE is able to resolve the class and the code compiles without any issues.

### Reserved Indices

If the client is deprecating a field and removing it from the codebase, it is recommended to mark the index as reserved.
This can be done by using the `@PibifyClassMetadata` annotation on the pojo and mark the indices which should not be
used.
Once this is in place, a validation step will ensure any Pojo that attempts to use a reserved index will fail during
compile time and the client must choose a different index for their field.
This helps prevent accidental reuse of indices from deprecated fields, which could cause backward compatibility issues
when deserializing older data.

For example, if you remove a field that used index 2, you should mark index 2 as reserved to ensure
it's not accidentally reused by a new field in the future.

```java
// Indices 2 and 3 were previously used for deprecated fields 'oldStatus' and 'legacyId'
@PibifyClassMetadata(reservedIndices = {2, 3})
public class ClassWithSimpleFields {
    @Pibify(1)
    private String name;
    @Pibify(4) // Note: Skipping indices 2 and 3 as they are reserved
    private int count;
}
```