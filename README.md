# yamllint-maven-plugin
yamllint provided as a maven plugin using com.github.sbaudoin:yamllint

## Usage 

```xml
    <plugin>
        <groupId>com.isycat</groupId>
        <artifactId>yamllint-maven-plugin</artifactId>
        <version>1.0.2</version>
        <configuration>
            <sourceDir>${project.build.outputDirectory}/yamlFiles</sourceDir>
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
For linter configuration please refer to https://github.com/sbaudoin/yamllint#configuration
