# yamllint-plugins
yamllint provided as a Maven and Gradle plugin using [com.github.sbaudoin:yamllint](https://github.com/sbaudoin/yamllint).
## Gradle Usage

### Using the Plugins DSL

Add the following to your `build.gradle.kts`:

```kotlin
plugins {
    id("com.isycat.yamllint") version "1.1.0"
}
```

Or for `build.gradle` (Groovy):

```groovy
plugins {
    id 'com.isycat.yamllint' version '1.1.0'
}
```

### Configuration

The plugin registers a `yamllint` task. You can configure it as follows:

```kotlin
tasks.withType<com.isycat.yamllint.YamlLintTask> {
    sourceDir.set(file("path/to/yaml/files"))
    configFile.set(file(".yamllint"))
}
```

By default, `sourceDir` is set to the project directory and `configFile` defaults to `.yamllint` in the project root.

### Running the Linter

Run the following command:

```bash
./gradlew yamllint
```

## Maven Usage

Add the following to your `pom.xml`:

```xml
<plugin>
    <groupId>com.isycat</groupId>
    <artifactId>yamllint-maven-plugin</artifactId>
    <version>1.1.0</version>
    <configuration>
        <sourceDir>${project.basedir}</sourceDir>
        <configFile>${project.basedir}/.yamllint</configFile>
    </configuration>
    <executions>
        <execution>
            <phase>verify</phase>
            <goals>
                <goal>lint</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Configuration
For linter rules configuration, please refer to the [yamllint documentation](https://github.com/sbaudoin/yamllint#configuration).
