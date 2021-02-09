package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

class CookieCutterLaunchBuildTask extends Exec {

    @Input
    final Property<String> outputPath = project.objects.property(String)
    @Input
    final Property<String> generatedProjectName = project.objects.property(String)
    @Input
    final Property<String> binary = project.objects.property(String)
    @Input
    final ListProperty<String> argList = project.objects.listProperty(String)
    @Internal
    final Provider<String> fullOutputPath = outputPath.map { "${project.buildDir}/${it}/${generatedProjectName.get()}" }

    // NOTE:  This doesn't actually work.  Preferably we would use this class, but for some
    // mysterious reason, it is not possible to subclass the Exec task and get all the
    // doFirst/doLast closures to run before the task executes
    CookieCutterLaunchBuildTask() {
//        executable(binary.get())
        doFirst {
            println "f:task [${generatedProjectName.get()}]"
            println "f:task [${outputPath.get()}]"
            println "f:task [${binary.get()}]"
            println "f:task [${fullOutputPath.get()}]"
            executable(binary.get())
            workingDir = fullOutputPath.get()
            args(argList.get())
        }
    }
}
