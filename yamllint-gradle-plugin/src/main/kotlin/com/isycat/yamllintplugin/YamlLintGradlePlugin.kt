package com.isycat.yamllintplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class YamlLintGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("yamllint", YamlLintTask::class.java) {
            it.sourceDir.set(project.projectDir)
            it.configFile.set(project.file(".yamllint"))
        }
    }
}
