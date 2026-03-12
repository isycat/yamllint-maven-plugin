plugins {
    kotlin("jvm") version "2.1.21"
    id("com.isycat.maven-central-publisher") version "0.0.2" apply false
}

allprojects {
    group = "com.isycat"
    version = "1.1.0"

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
