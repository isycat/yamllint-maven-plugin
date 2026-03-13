package com.isycat.yamllintplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class YamlLintGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("yamllint", YamlLintExtension::class.java)
        extension.sourceDir.convention(project.layout.projectDirectory)
        extension.configFile.convention(project.layout.projectDirectory.file(".yamllint"))

        project.tasks.register("yamllint", YamlLintTask::class.java) {
            it.sourceDir.set(extension.sourceDir)
            it.configFile.set(extension.configFile)
        }
    }
}
