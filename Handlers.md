### Concept

A Handler is an implementation of the `PibifyGenerated<T>` abstract class. It primarily has 2 methods

```java
public abstract T deserialize(IDeserializer deserializer, Class<T> type, SerializationContext context)
        throws PibifyCodeExecException;

public abstract void serialize(T object, ISerializer serializer, SerializationContext context)
        throws PibifyCodeExecException;
```

The Pibify Maven plugin generates a concrete implementation of this abstract class.

For every class in the maven module annotated with `@Pibify`, a handler is generated. The handler is named as
`<ClassName>Handler`.

### Out of the Box Handlers

Few handlers are provided out of the box. They are:

1. PibifyObjectHandler - for cases where the reference type is `java.lang.Object`
2. PibifyMapHandler - for cases where the reference type is `java.util.Map` with missing type parameters
3. PibifyCollectionHandler - for cases where the reference type is `java.util.Collection` with missing type parameters
4. BigDecimalHandler - for cases where the reference type is `java.math.BigDecimal`
5. DateHandler - for cases where the reference type is `java.util.Date`

This well-known handlers are registered in `AbstractPibifyHandlerCache`'s protected constructor.

#### Adding custom hand-written handlers

If you want to add custom handlers, you can do so by extending the `AbstractPibifyHandlerCache` class and registering
your custom handlers in the constructor.

```java
public class CustomHandlerCache extends AbstractPibifyHandlerCache {
    public CustomHandlerCache() {
        super();
        // Type is the class type of the object
        super.mapBuilder.put(type, new CustomHandler());
    }
}
```

Then merge `CustomHandlerCache` with the generated `PibifyHandlerCache` using `MultiModuleHandlerCache`:

```java
new MultiModulePibifyHandlerCache(new CustomHandlerCache(), new PibifyHandlerCache());
```