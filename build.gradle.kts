plugins {
    kotlin("jvm") version "2.1.21"
    id("com.isycat.maven-central-publisher") version "0.0.2" apply false
}

allprojects {
    group = "com.isycat"
    version = properties["version"] ?: "0.0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    kotlin {
        jvmToolchain(21)
    }
}
