package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.GradleBuild
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// NOTE: This is currently unused, but remains as a potential implementation direction for
// kicking off a sub-build of the generated templates.  There may be cases where the generated
// code isn't a complete project, and might benefit from a dynamic multi-module build
class CookieCutterBuildTask extends GradleBuild {
    Logger log = LoggerFactory.getLogger(CookieCutterBuildTask.class)

    @Input
    final Property<String> outputPath = project.objects.property(String)
    @Input
    final Property<String> generatedProjectName = project.objects.property(String)
    @Internal
    final Provider<String> fullOutputPath = outputPath.map { "${project.buildDir}/${it}/${generatedProjectName.get()}" }

    CookieCutterBuildTask() {
        tasks = ["clean", "build", "test"]
        doLast {
            log.debug("l:task [{}]", generatedProjectName.get())
            log.debug("l:task [{}]", outputPath.get())
            log.debug("l:task [{}]", fullOutputPath.get())
            dir = "${fullOutputPath.get()}"
        }
    }
}
