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

[ParityCheck.md](Parity Check)

### String Dictionary