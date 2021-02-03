package com.isycat;


import com.isycat.yamllintplugin.YamlLintException;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class YamllintMavenPluginTest {
    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }
    };

    @Test
    public void testFailureProject() throws Exception {
        File pom = new File("target/test-classes/line-too-long-project/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        YamllintMavenPlugin yamllintMavenPlugin = (YamllintMavenPlugin) rule.lookupConfiguredMojo(pom, "lint");

        boolean thrown = false;
        try {
            yamllintMavenPlugin.execute();
        } catch (final YamlLintException e) {
            thrown = true;
            assertEquals("1 error(s) found while parsing yaml files.", e.getMessage());
        }

        assertEquals("Expected YamlLintException but no error was thrown", true, thrown);
    }

    @Test
    public void testNoErrorProject() throws Exception {
        File pom = new File("target/test-classes/successful-project/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        YamllintMavenPlugin yamllintMavenPlugin = (YamllintMavenPlugin) rule.lookupConfiguredMojo(pom, "lint");
        yamllintMavenPlugin.execute();
    }
}

