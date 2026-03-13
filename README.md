# yamllint-plugins
yamllint provided as a Maven and Gradle plugin using [com.github.sbaudoin:yamllint](https://github.com/sbaudoin/yamllint).
## Gradle Usage

### Configuration

The plugin registers a `yamllint` task. You can configure it using the `yamllint` extension in your `build.gradle.kts`:

```kotlin
plugins {
    id("com.isycat.yamllint") version "1.1.0"
}

yamllint {
    sourceDir = layout.projectDirectory.dir("path/to/yaml/files")
    configFile = layout.projectDirectory.file(".yamllint")
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
