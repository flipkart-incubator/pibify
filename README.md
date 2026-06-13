# Pibify

Protobuf-ify -> PB-IFY -> Pibify

Serve Java POJOs as protobuf over the wire.

## What it does

1. Marks fields using `@Pibify`.
2. Generates handlers during build using `pibify-maven-plugin`.
3. Uses generated `PibifyHandlerCache` at runtime for serialization/deserialization.

## Quick onboarding

### Prerequisites

- Java 17+
- Maven 3.8+

### Add dependencies and plugin

Add `pibify-core` dependency:

```xml
<dependency>
    <groupId>com.flipkart.pibify</groupId>
    <artifactId>pibify-core</artifactId>
    <scope>compile</scope>
    <version>1.6</version>
</dependency>
```

Add `pibify-maven-plugin` in the model module:

```xml
<plugin>
    <groupId>com.flipkart.pibify</groupId>
    <artifactId>pibify-maven-plugin</artifactId>
    <version>1.6</version>
    <configuration>
        <excludes>
            <exclude>com/flipkart/pibify/toskip/**</exclude>
            <exclude>com/flipkart/pibify/toskip2/**</exclude>
        </excludes>
    </configuration>
    <dependencies>
        <!--
        Plugin may not resolve transitive dependencies of the model module.
        Add required compile-time model dependencies explicitly here.
        -->
    </dependencies>
    <executions>
        <execution>
            <id>generate-pibify-sources</id>
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
            <id>add-pibify-source</id>
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
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>${maven-compiler-plugin.version}</version>
    <executions>
        <execution>
            <id>compile-generated-pibify-sources</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Annotate model fields

```bash
mvn com.flipkart.pibify:pibify-maven-plugin:annotate
```

You can also annotate fields manually:

```java
@Pibify(1)
private String aStringVariable;

@Pibify(2)
private int anIntegerVariable;
```

### Generate handlers

As part of `mvn package`, handler sources and `PibifyHandlerCache` are generated.

### Integrate with runtime

Use generated `PibifyHandlerCache` in framework integration points.

## For contributors

```bash
mvn clean test        # run all tests
mvn clean package     # build all modules
mvn -pl test-environments/pibify-test-vanilla -am clean test  # try a sample
```

See [Setup and First Run](docs/setup.md) for more commands, including Dropwizard and Vert.x sample validation, troubleshooting, and the full integration guide.

## Integrations

1. [vert.x](docs/vertx.md)
2. [dropwizard](docs/dropwizard.md)

## More docs

- [Onboarding details](docs/setup.md)
- [Architecture overview](docs/architecture.md)
- [Handlers](docs/Handlers.md)
- [Advanced Features](docs/AdvancedFeatures.md)
- [Parity Check](docs/ParityCheck.md)
- [Test Cases](docs/TestCases.md)