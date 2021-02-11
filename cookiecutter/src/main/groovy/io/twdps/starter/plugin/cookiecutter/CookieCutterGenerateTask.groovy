package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CookieCutterGenerateTask extends DefaultTask {
    Logger log = LoggerFactory.getLogger(CookieCutterGenerateTask.class)

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
        outputPath.convention('cookiecutter')
        binary.convention('cookiecutter')
        taskTimeout.convention(10000L)
        outputs.dir(fullOutputPath)
        inputs.dir(fullInputPath)
        doLast {
            log.debug("l:task:template [{}]", template.get())
            log.debug("l:task:outputPath [{}]", outputPath.get())
            log.debug("l:task:taskTimeout [{}]", taskTimeout.get())
            def cmdLine = "${binary.get()} -f --no-input -o ${fullOutputPath.get()} ${template.get()}"
            log.debug("l:task:exec [{}]: {}", project.projectDir, cmdLine)
            def proc = cmdLine.execute(null, project.projectDir)
            proc.waitForOrKill(taskTimeout.get())
        }
    }
}
