pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "yamllint-plugins"
include("yamllint-maven-plugin")
include("yamllint-gradle-plugin")
