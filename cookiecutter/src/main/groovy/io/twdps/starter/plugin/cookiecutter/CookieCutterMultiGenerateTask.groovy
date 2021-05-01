package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CookieCutterMultiGenerateTask extends DefaultTask {
    @Internal
    Logger log = LoggerFactory.getLogger(CookieCutterMultiGenerateTask.class)

    @Input
    final ListProperty<String> templates = project.objects.listProperty(String)
    @Input
    final Property<String> outputPath = project.objects.property(String)
    @Input
    final Property<String> binary = project.objects.property(String)
    @Input
    final Property<Long> taskTimeout = project.objects.property(Long)
    @Input
    final ListProperty<String> context = project.objects.listProperty(String)
    @Internal
    final Provider<String> fullOutputPath = outputPath.map { "${project.buildDir}/${it}" }

    CookieCutterMultiGenerateTask() {
        outputPath.convention('cookiecutter')
        binary.convention('cookiecutter')
        taskTimeout.convention(10000L)
//        outputs.dir("${project.buildDir}/${outputPath.get()}")
        doLast {
            log.debug("l:mtask:templates [{}]",templates.get().size())
            log.debug("l:mtask:outputPath [{}]", outputPath.get())
            log.debug("l:mtask:fullOutputPath [{}]", fullOutputPath.get())
            log.debug("l:mtask:taskTimeout [{}]", taskTimeout.get())
            StringBuilder extra_context = new StringBuilder()
            context.get().each { p -> extra_context.append(' ').append(p) }
            extra_context.append(" outputPath=").append(fullOutputPath.get())//.append("'")
            extra_context.append(" projectDir=").append(project.projectDir)//.append("'")
            def result = 0
            templates.get().each { p ->
                def cmdLine = "${binary.get()} -f --verbose --debug-file /tmp/cc.out --no-input -o ${fullOutputPath.get()} ${p}"// ${extra_context.toString()}"
                log.debug("l:mtask:exec [{}]: {}", project.projectDir, cmdLine)
                def proc = cmdLine.execute(null, project.projectDir)
                proc.waitForOrKill(taskTimeout.get())
                result |= proc.exitValue()
            }
            return result
        }
    }
}

