package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

class CookieCutterPluginExtension {

    final Property<String> template
    final ListProperty<String> templates
    final Property<String> outputPath
    final Property<String> inputPath
    final Property<String> templateBinary
    final Property<String> diffBinary
    final ListProperty<String> omitCompare

    CookieCutterPluginExtension(ObjectFactory objects) {
        template = objects.property(String)
        outputPath = objects.property(String)
        inputPath = objects.property(String)
        templateBinary = objects.property(String)
        diffBinary = objects.property(String)
        templates = objects.listProperty(String)
        omitCompare = objects.listProperty(String)
        // set defaults
        template.set('.')
        outputPath.set('cookiecutter')
        templateBinary.set('cookiecutter')
        diffBinary.set('diff')
        inputPath.set('..')
    }
}

