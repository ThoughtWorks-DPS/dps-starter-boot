package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

class CookieCutterMultiGenerateTask extends DefaultTask {

    @Input
    final ListProperty<String> templates = project.objects.listProperty(String)
    @Input
    final Property<String> outputPath = project.objects.property(String)
    @Input
    final Property<String> binary = project.objects.property(String)
    @Input
    final Property<Long> taskTimeout = project.objects.property(Long)

    CookieCutterMultiGenerateTask() {
        outputPath.set('cookiecutter')
        binary.set('cookiecutter')
        taskTimeout.set(10000L)
//        outputs.dir("${project.buildDir}/${outputPath.get()}")
        doLast {
            println "l:mtask [${templates.get().size()}]"
            println "l:mtask [${outputPath.get()}]"
            println "l:mtask [${taskTimeout.get()}]"
            templates.get().each { p ->
                def proc = "${binary.get()} -f --no-input -o ${project.buildDir}/${outputPath.get()} ${p}".execute(null, project.projectDir)
                proc.waitForOrKill(taskTimeout.get())
            }
        }
    }
}

