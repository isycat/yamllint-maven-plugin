package com.isycat.yamllintplugin

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class YamlLintGradlePluginTest {
    @get:Rule
    val tempFolder = TemporaryFolder(java.io.File("build/tmp").apply { mkdirs() })

    private lateinit var projectDir: java.io.File
    private lateinit var buildFile: java.io.File

    @Before
    fun setup() {
        projectDir = tempFolder.newFolder("yamllint-test")
        projectDir.resolve("settings.gradle").writeText("")
        buildFile = projectDir.resolve("build.gradle")
        buildFile.writeText(
            """
            plugins {
                id 'com.isycat.yamllint'
            }
            """.trimIndent(),
        )
    }

    @Test
    fun `test successful linting`() {
        projectDir.resolve(".yamllint").writeText(
            """
            extends: default
            rules:
              line-length:
                max: 10
              new-lines: disable
              document-start:
                level: error
              document-end:
                level: error
            """.trimIndent(),
        )
        projectDir.resolve("test.yaml").writeText(
            """
            ---
            key: short
            ...
            
            """.trimIndent(),
        )

        val result =
            GradleRunner
                .create()
                .withProjectDir(projectDir)
                .withArguments("yamllint")
                .withPluginClasspath()
                .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":yamllint")?.outcome)
    }

    @Test
    fun `test failing linting`() {
        projectDir.resolve(".yamllint").writeText(
            """
            extends: default
            rules:
              line-length:
                max: 10
              new-lines: disable
              document-end:
                level: error
            """.trimIndent(),
        )
        projectDir.resolve("test.yaml").writeText(
            """
            key: this line is definitely longer than 10 characters
            ...
            
            """.trimIndent(),
        )

        val result =
            GradleRunner
                .create()
                .withProjectDir(projectDir)
                .withArguments("yamllint")
                .withPluginClasspath()
                .buildAndFail()

        assertEquals(TaskOutcome.FAILED, result.task(":yamllint")?.outcome)
        assertTrue(result.output.contains("1 error found while parsing yaml files."))
    }

    @Test
    fun `test custom configuration`() {
        val customYamlDir = projectDir.resolve("custom-yaml")
        customYamlDir.mkdirs()
        customYamlDir.resolve("test.yaml").writeText(
            """
            ---
            key: value
            ...
            """.trimIndent(),
        )

        val customConfig = projectDir.resolve("custom.yamllint")
        customConfig.writeText(
            """
            extends: default
            """.trimIndent(),
        )

        buildFile.writeText(
            """
            plugins {
                id 'com.isycat.yamllint'
            }
            yamllint {
                sourceDir = layout.projectDirectory.dir("custom-yaml")
                configFile = layout.projectDirectory.file("custom.yamllint")
            }
            """.trimIndent(),
        )

        val result =
            GradleRunner
                .create()
                .withProjectDir(projectDir)
                .withArguments("yamllint")
                .withPluginClasspath()
                .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":yamllint")?.outcome)
    }
}
