package com.isycat.yamllintplugin

import org.junit.Test
import java.io.File
import org.junit.Assert.assertTrue

class YamllintMavenPluginTest {

    @Test
    @Throws(Exception::class)
    fun testFailureProject() {
        val sourceDir = File("src/test/resources/line-too-long-project/")
        val configFile = File(sourceDir, ".yamllint")

        val yamllintMavenPlugin = YamllintMavenPlugin()
        yamllintMavenPlugin.sourceDir = sourceDir
        yamllintMavenPlugin.configFile = configFile

        var thrown = false
        try {
            yamllintMavenPlugin.execute()
        } catch (e: Exception) {
            // Check if it's our exception or wrapped in MojoExecutionException
            val cause = e.cause
            if (e.javaClass.name.endsWith("YamlLintException") ||
                (cause != null && cause.javaClass.name.endsWith("YamlLintException"))
            ) {
                thrown = true
                val msg = cause?.message ?: e.message
                assertTrue(msg!!.contains("error(s) found while parsing yaml files."))
            } else {
                throw e
            }
        }

        assertTrue("Expected YamlLintException but no error was thrown", thrown)
    }

    @Test
    @Throws(Exception::class)
    fun testNoErrorProject() {
        val sourceDir = File("src/test/resources/successful-project-clean/")

        val yamllintMavenPlugin = YamllintMavenPlugin()
        yamllintMavenPlugin.sourceDir = sourceDir
        yamllintMavenPlugin.configFile = File(sourceDir, ".yamllint")

        yamllintMavenPlugin.execute()
    }
}
