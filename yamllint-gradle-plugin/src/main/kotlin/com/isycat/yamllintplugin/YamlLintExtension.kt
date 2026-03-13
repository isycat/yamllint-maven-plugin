package com.isycat.yamllintplugin

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class YamlLintExtension @Inject constructor(objects: ObjectFactory) {
    val sourceDir: DirectoryProperty = objects.directoryProperty()
    val configFile: RegularFileProperty = objects.fileProperty()
}
