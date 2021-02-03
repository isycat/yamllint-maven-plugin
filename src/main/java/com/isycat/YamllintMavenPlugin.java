package com.isycat;


import com.github.sbaudoin.yamllint.Format;
import com.github.sbaudoin.yamllint.LintProblem;
import com.github.sbaudoin.yamllint.Linter;
import com.github.sbaudoin.yamllint.YamlLintConfig;
import com.isycat.yamllintplugin.YamlLintException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * YAML Linter
 */
@Mojo(name = "lint", defaultPhase = LifecyclePhase.VERIFY)
public class YamllintMavenPlugin
        extends AbstractMojo {

    /**
     * Location of the dir to check.
     */
    @Parameter(defaultValue = "${project.basedir}", property = "sourceDir")
    private File sourceDir;

    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.basedir}/.yamllint", property = "configFile")
    private File configFile;

    public void execute()
            throws MojoExecutionException {
        try {
            lint();
        } catch (IOException e) {
            throw new YamlLintException(e.getMessage(), e);
        }
    }

    private void lint() throws IOException {
        // log config details
        getLog().debug("sourceDir: " + sourceDir);
        getLog().debug("configFile: " + configFile);

        // handle config ---
        if (!sourceDir.exists()) {
            throw new YamlLintException(
                    "sourceDir not found: " + sourceDir.getAbsolutePath(),
                    new FileNotFoundException(sourceDir.getAbsolutePath())
            );
        }

        final YamlLintConfig config;
        try {
            config = configFile.exists()
                    ? new YamlLintConfig(configFile.toURI().toURL())
                    : new YamlLintConfig("---\nextends: default\n...");
        } catch (final Exception e) {
            throw new YamlLintException("Failed to load config");
        }
        // ---

        final Collection<File> files = FileUtils.listFiles(
                sourceDir,
                new RegexFileFilter("^(.*?)\\.(yaml|yml)"),
                DirectoryFileFilter.DIRECTORY
        );

        // lint files ---
        final AtomicLong errorCount = new AtomicLong(0);
        for (final File yamlFile : files) {
            getLog().debug("checking file: " + yamlFile);
            final List<LintProblem> problems = Linter.run(config, yamlFile);
            errorCount.addAndGet(
                    problems.stream()
                            .filter(p -> p.getLevel().equalsIgnoreCase("error"))
                            .count()
            );
            if (!problems.isEmpty()) {
                getLog().info(Format.format(yamlFile.getPath(), problems, Format.OutputFormat.COLORED));
            }
        }
        // ---

        if (errorCount.get() > 0) {
            throw new YamlLintException(errorCount.get() + " error(s) found while parsing yaml files.");
        }
    }
}
