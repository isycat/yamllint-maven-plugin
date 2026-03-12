package com.isycat.yamllintplugin

import com.github.sbaudoin.yamllint.Format
import com.github.sbaudoin.yamllint.LintProblem
import com.github.sbaudoin.yamllint.Linter
import com.github.sbaudoin.yamllint.YamlLintConfig
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong

/**
 * YAML Linter
 */
@Mojo(name = "lint", defaultPhase = LifecyclePhase.VERIFY)
class YamllintMavenPlugin : AbstractMojo() {
    /**
     * Location of the dir to check.
     */
    @Parameter(defaultValue = "\${project.basedir}", property = "sourceDir")
    internal var sourceDir: File? = null

    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "\${project.basedir}/.yamllint", property = "configFile")
    internal var configFile: File? = null

    fun setSourceDir(sourceDir: File) {
        this.sourceDir = sourceDir
    }

    fun setConfigFile(configFile: File) {
        this.configFile = configFile
    }

    override fun execute() {
        try {
            lint(sourceDir!!, configFile!!)
        } catch (e: Exception) {
            if (e is IOException || e is YamlLintException) {
                throw MojoExecutionException(e.message, e)
            }
            throw e
        }
    }

    private fun lint(
        sourceDir: File,
        configFile: File,
    ) {
        log.debug("sourceDir: $sourceDir")
        log.debug("configFile: $configFile")

        if (!sourceDir.exists()) {
            throw IOException("sourceDir not found: ${sourceDir.absolutePath}")
        }

        val config: YamlLintConfig =
            try {
                if (configFile.exists()) {
                    YamlLintConfig(configFile.toURI().toURL())
                } else {
                    YamlLintConfig("---\nextends: default\n...")
                }
            } catch (e: Exception) {
                throw IOException("Failed to load config", e)
            }

        val files =
            sourceDir
                .walkTopDown()
                .filter {
                    it.isFile && (it.extension == "yaml" || it.extension == "yml") && it.name != "pom.xml" && it.name != ".yamllint" &&
                        !it.absolutePath.contains("${File.separator}target${File.separator}") &&
                        !it.absolutePath.contains("${File.separator}build${File.separator}")
                }.toList()

        val errorCount = AtomicLong(0)
        for (yamlFile in files) {
            log.debug("checking file: $yamlFile")
            val problems: List<LintProblem> = Linter.run(config, yamlFile)
            var fileErrorCount = 0L
            for (problem in problems) {
                if (problem.level.equals("error")) {
                    fileErrorCount++
                }
            }
            errorCount.addAndGet(fileErrorCount)
            if (problems.size > 0) {
                log.info(Format.format(yamlFile.path, problems, Format.OutputFormat.COLORED))
            }
        }

        if (errorCount.get() > 0) {
            throw YamlLintException("${errorCount.get()} error(s) found while parsing yaml files.")
        }
    }
}
