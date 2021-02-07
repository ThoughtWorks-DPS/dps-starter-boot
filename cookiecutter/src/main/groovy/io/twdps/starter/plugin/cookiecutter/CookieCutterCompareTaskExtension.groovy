package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

class CookieCutterCompareTaskExtension {

    final Property<String> outputPath
    final Property<String> sourcePath
    final Property<String> generatedProjectName
    final Property<String> diffBinary
    final ListProperty<String> omitFiles

    CookieCutterCompareTaskExtension(ObjectFactory objects) {
        outputPath = objects.property(String)
        sourcePath = objects.property(String)
        diffBinary = objects.property(String)
        generatedProjectName = objects.property(String)
        omitFiles = objects.listProperty(String)
        // set defaults
        outputPath.set('cookiecutter')
        generatedProjectName.set('cookiecutter')
        diffBinary.set('diff')
        sourcePath.set('..')
    }
}

