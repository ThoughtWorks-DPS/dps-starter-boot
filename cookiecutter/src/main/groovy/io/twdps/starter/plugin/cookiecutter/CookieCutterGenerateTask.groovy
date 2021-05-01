package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CookieCutterGenerateTask extends DefaultTask {
    @Internal
    Logger log = LoggerFactory.getLogger(CookieCutterGenerateTask.class)

    @Input
    final Property<String> template = project.objects.property(String)
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
    @Internal
    final Provider<String> fullInputPath = template.map { "${project.projectDir}/${it}" }

    CookieCutterGenerateTask() {
        outputPath.convention('cookiecutter')
        binary.convention('cookiecutter')
        taskTimeout.convention(20000L)
        outputs.dir(fullOutputPath)
        inputs.dir(fullInputPath)
        doLast {
            log.debug("l:task:template [{}]", template.get())
            log.debug("l:task:outputPath [{}]", outputPath.get())
            log.debug("l:task:taskTimeout [{}]", taskTimeout.get())
            StringBuilder extra_context = new StringBuilder()
            context.get().each { p -> extra_context.append(p) }
            extra_context.append(" outputPath=").append(fullOutputPath.get())//.append("'")
            extra_context.append(" projectDir=").append(project.projectDir)//.append("'")
            def cmdLine = "${binary.get()} -f --no-input --verbose --debug-file /tmp/cc.out -o ${fullOutputPath.get()} ${template.get()} ${extra_context.toString()}"
            log.debug("l:task:exec [{}]: {}", project.projectDir, cmdLine)
            def proc = cmdLine.execute(null, project.projectDir)
            proc.in.eachLine { line -> log.debug(line) }
            proc.out.close()
            proc.waitForOrKill(taskTimeout.get())
            log.debug("Exit code: [{}]", proc.exitValue())
            return proc.exitValue()
        }
    }
}
