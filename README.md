# Pibify

Protobuf-ify --> PB-IFY -> Pibify

Serve Java pojos as protobuf over the wire

# Concept

## Terminologies

#### `@Pibify`
This is the annotation to be added on any field of a Pojo class that has to be transported

```java

@Pibify(1)
private String aStringVariable;

@Pibify(2)
private int anIntegerVariable;
```

#### Handler
This is the generated source corresponding to the Pojo where a `@Pibify` annotation is present.

```java
public abstract class PibifyGenerated<T> {
    public abstract byte[] serialize(T object) throws PibifyCodeExecException;

    public abstract T deserialize(byte[] bytes) throws PibifyCodeExecException;
}
```

#### `PibifyHandlerCache`
This is the class that the clients use to get an instance of Handler for the supplied class and then call the serialize or deserialize method on it.

## Flow
1. Add the dependency on pibify-core library
2. In the desired pojos, add the `@Pibify(<index>)` annotation
3. Clients configure a maven plugin which scans the source of the project during the build phase and collects all pojos which have the `@Pibify` annotation.
4. This plugin generates the `Handler` and `PibifyHandlerCache` for the configured module and places them at a suitable place in the package(jar)
5. Clients use the `PibifyHandlerCache` to get a reference to a `Handler` and call the serialize/deserialize method on it.


# Usage
1. Add the `pibify-core` dependency
```xml
<dependency>
    <groupId>com.flipkart.pibify</groupId>
    <artifactId>pibify-core</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>

```
2. Configure the model mvn module by adding the below plugin config
```xml
<plugin>
    <groupId>com.flipkart.pibify</groupId>
    <artifactId>pibify-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <id>generate-sources</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <phase>process-classes</phase>
        </execution>
    </executions>
</plugin>
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>build-helper-maven-plugin</artifactId>
    <version>3.2.0</version>
    <executions>
        <execution>
            <id>add-source</id>
            <phase>process-classes</phase>
            <goals>
                <goal>add-source</goal>
            </goals>
            <configuration>
                <sources>
                    <source>${project.build.directory}/generated-sources/pibify</source>
                </sources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

3. As part of the `package` mvn goal, the handler code will be generated

# Integration with application containers

1. [vert.x](vertx.md)

