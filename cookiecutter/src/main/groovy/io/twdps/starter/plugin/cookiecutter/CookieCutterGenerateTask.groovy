package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory

class CookieCutterGenerateTask extends DefaultTask {

    @Input
    final Property<String> template = project.objects.property(String)
    @Input
    final Property<String> outputPath = project.objects.property(String)
    @Input
    final Property<String> binary = project.objects.property(String)
    @Input
    final Property<Long> taskTimeout = project.objects.property(Long)
    @Internal
    final Provider<String> fullOutputPath = outputPath.map { "${project.buildDir}/${it}" }
    @Internal
    final Provider<String> fullInputPath = template.map { "${project.projectDir}/${it}" }

    CookieCutterGenerateTask() {
        outputPath.set('cookiecutter')
        binary.set('cookiecutter')
        taskTimeout.set(10000L)
        outputs.dir(fullOutputPath)
        inputs.dir(fullInputPath)
        doLast {
            println "l:task [${template.get()}]"
            println "l:task [${outputPath.get()}]"
            println "l:task [${taskTimeout.get()}]"
            def proc = "${binary.get()} -f --no-input -o ${fullOutputPath.get()} ${template.get()}".execute(null, project.projectDir)
            proc.waitForOrKill(taskTimeout.get())
        }
    }
}
