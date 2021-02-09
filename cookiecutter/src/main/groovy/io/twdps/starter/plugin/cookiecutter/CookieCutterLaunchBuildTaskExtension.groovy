package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

class CookieCutterLaunchBuildTaskExtension {

    final Property<String> outputPath
    final Property<String> generatedProjectName
    final Property<String> buildBinary
    final ListProperty<String> args

    CookieCutterLaunchBuildTaskExtension(ObjectFactory objects) {
        outputPath = objects.property(String)
        buildBinary = objects.property(String)
        generatedProjectName = objects.property(String)
        args = objects.listProperty(String)
        // set defaults
        outputPath.set('cookiecutter')
        generatedProjectName.set('cookiecutter')
        buildBinary.set('./gradlew')
    }
}

