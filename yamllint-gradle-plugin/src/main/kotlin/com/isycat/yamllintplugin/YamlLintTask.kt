package com.isycat.yamllintplugin

import com.github.sbaudoin.yamllint.Format
import com.github.sbaudoin.yamllint.Linter
import com.github.sbaudoin.yamllint.YamlLintConfig
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException

abstract class YamlLintTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceDir: DirectoryProperty

    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val configFile: RegularFileProperty

    @TaskAction
    fun lint() {
        val srcDir =
            (sourceDir.get().asFile)
                .takeIf { it.exists() }
                .also { logger.debug("sourceDir: $it") }
                ?: throw IOException("sourceDir not found: $sourceDir")
        val cfgFile =
            configFile.takeIf { it.isPresent }?.get()?.asFile
                ?: File(project.projectDir, ".yamllint")
        logger.debug("configFile: $cfgFile")
        val config =
            try {
                cfgFile
                    .takeIf { it.exists() }
                    ?.let { YamlLintConfig(it.toURI().toURL()) }
                    ?: YamlLintConfig("---\nextends: default\n...")
            } catch (e: Exception) {
                throw IOException("Failed to load config", e)
            }
        srcDir
            .walkTopDown()
            .filter { it.isFile && (it.extension == "yaml" || it.extension == "yml") }
            .toList()
            .asSequence()
            .onEach { logger.debug("checking file: $it") }
            .map { it to Linter.run(config, it) }
            .onEach { (file, problems) ->
                if (problems.isEmpty()) return@onEach
                logger.lifecycle(Format.format(file.path, problems, Format.OutputFormat.COLORED))
            }.sumOf { (file, problems) -> problems.count { it.level.equals("error") } }
            .takeIf { it > 0 }
            ?.let { errs ->
                throw YamlLintException("$errs error${"(s)".takeIf { errs > 1 } ?: ""} found while parsing yaml files.")
            }
    }
}
