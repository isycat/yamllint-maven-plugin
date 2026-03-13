plugins {
    `maven-publish`
    kotlin("jvm")
    id("com.isycat.maven-central-publisher")
}

mavenCentralPublishing {
    groupId = "com.isycat"
    artifactId = "yamllint-maven-plugin"
    name = "yamllint Maven Plugin"
    description = "yamllint provided as a Maven plugin"
    url = "https://github.com/isycat/yamllint-maven-plugin"
    developerId = "isycat"
    license {
        name = "MIT"
        url = "https://github.com/isycat/yamllint-maven-plugin/blob/main/yamllint-gradle-plugin/LICENSE"
    }
}

dependencies {
    implementation("com.github.sbaudoin:yamllint:1.6.1")
    implementation("org.apache.maven:maven-plugin-api:3.6.3")
    implementation("org.apache.maven.plugin-tools:maven-plugin-annotations:3.6.0")

    testImplementation("org.apache.maven.plugin-testing:maven-plugin-testing-harness:3.3.0")
    testImplementation("org.apache.maven:maven-compat:3.6.3")
    testImplementation("org.apache.maven:maven-core:3.6.3")
    testImplementation("org.apache.maven:maven-model:3.6.3")
    testImplementation("org.apache.maven:maven-artifact:3.6.3")
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(21)
}

// Ensure the Maven plugin descriptor is generated during the build if using Gradle
tasks.jar {
    from("${project.projectDir}/target/classes/META-INF/maven") {
        into("META-INF/maven")
    }
}
