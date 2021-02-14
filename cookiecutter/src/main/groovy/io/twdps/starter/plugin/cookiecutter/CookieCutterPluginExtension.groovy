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
    final Property<Long> taskTimeout
    final ListProperty<String> context
    final ListProperty<String> omitCompare

    CookieCutterPluginExtension(ObjectFactory objects) {
        template = objects.property(String)
        outputPath = objects.property(String)
        inputPath = objects.property(String)
        templateBinary = objects.property(String)
        diffBinary = objects.property(String)
        templates = objects.listProperty(String)
        omitCompare = objects.listProperty(String)
        context = objects.listProperty(String)
        taskTimeout = objects.property(Long)
        // set defaults
        template.convention('.')
        outputPath.convention('cookiecutter')
        templateBinary.convention('cookiecutter')
        diffBinary.convention('diff')
        inputPath.convention('..')
        taskTimeout.convention(10000L)
    }
}

