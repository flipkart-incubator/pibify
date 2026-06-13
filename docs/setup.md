# Setup and First Run

This page is focused on getting a first successful run quickly.

## Prerequisites

- Java 17+
- Maven 3.8+

Verify:

```bash
java -version
mvn -version
```

## Clone and run tests

```bash
git clone <repo-url>
cd pibify
mvn clean test
```

## Build all modules

```bash
mvn clean package
```

## Quick local validation

Run the vanilla test environment:

```bash
mvn -pl test-environments/pibify-test-vanilla -am clean test
```

Run Dropwizard sample:

```bash
mvn -pl test-environments/dropwizard/test-dropwizard -am clean test
```

Run Vert.x sample:

```bash
mvn -pl test-environments/vertx -am clean test
```

## Typical integration steps

1. Add `pibify-core` as a dependency.
2. Add `pibify-maven-plugin` in the model module. Use `<excludes>` to skip packages/classes that should not be scanned. See [Maven include/exclude docs](https://maven.apache.org/plugins/maven-resources-plugin/examples/include-exclude.html) for pattern syntax. If the plugin cannot resolve types that are only on the model module's classpath, add them as explicit `<dependencies>` to the plugin declaration.
3. Add `@Pibify(<index>)` annotations to required fields, or run the auto-annotate goal:

```bash
mvn com.flipkart.pibify:pibify-maven-plugin:annotate
```

   Re-annotate existing fields with new indexes by passing `-Dreindex`:

```bash
mvn com.flipkart.pibify:pibify-maven-plugin:annotate -Dreindex
```

4. Generate handlers during build and include generated sources.
5. Use generated `PibifyHandlerCache` in your runtime layer.

## Common issues

### Handlers are not generated

- Check that fields are annotated with `@Pibify`.
- Check plugin execution phase in module `pom.xml`.
- Ensure generated source directory is added to compile sources.

### Plugin cannot compile model types

`pibify-maven-plugin` may not resolve transitive dependencies of the model module. Add required compile-time model dependencies explicitly as `<dependencies>` inside the plugin declaration in `pom.xml`.

### `annotate` does not update existing indexes

Pass `-Dreindex` (see step 3 under [Typical integration steps](#typical-integration-steps)).
