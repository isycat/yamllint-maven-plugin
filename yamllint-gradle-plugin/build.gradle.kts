plugins {
    `java-gradle-plugin`
    kotlin("jvm")
    id("com.isycat.maven-central-publisher")
}

mavenCentralPublishing {
    groupId = "com.isycat"
    artifactId = "yamllint-gradle-plugin"
    name = "yamllint Gradle Plugin"
    description = "yamllint provided as a Gradle plugin"
    url = "https://github.com/isycat/yamllint-maven-plugin"
    developerId = "isycat"
    license {
        name = "Apache-2.0"
        url = "https://github.com/isycat/yamllint-maven-plugin"
    }
}

dependencies {
    implementation("com.github.sbaudoin:yamllint:1.6.1")
    testImplementation(gradleTestKit())
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(21)
}

gradlePlugin {
    plugins {
        create("yamllintPlugin") {
            id = "com.isycat.yamllint"
            implementationClass = "com.isycat.yamllintplugin.YamlLintGradlePlugin"
        }
    }
}

tasks.withType<org.gradle.api.publish.maven.tasks.PublishToMavenRepository>().configureEach {
    val signingTasks = tasks.withType<org.gradle.plugins.signing.Sign>()
    dependsOn(signingTasks)
}
