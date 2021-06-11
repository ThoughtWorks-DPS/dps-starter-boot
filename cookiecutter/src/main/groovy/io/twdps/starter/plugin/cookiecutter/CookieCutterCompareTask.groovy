package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CookieCutterCompareTask extends DefaultTask {
    @Internal
    Logger log = LoggerFactory.getLogger(CookieCutterCompareTask.class)

    @Input
    final Property<String> sourcePath = project.objects.property(String)
    @Input
    final Property<String> outputPath = project.objects.property(String)
    @Input
    final Property<String> generatedProjectName = project.objects.property(String)
    @Input
    final Property<String> binary = project.objects.property(String)
    @Input
    final Property<Long> taskTimeout = project.objects.property(Long)
    @Input
    final ListProperty<String> omitFiles = project.objects.listProperty(String)
    @Input
    final ListProperty<String> stdOmitFiles = project.objects.listProperty(String)
    @Internal
    final Provider<String> fullOutputPath = outputPath.map { "${project.buildDir}/${it}/${generatedProjectName.get()}" }
    @Internal
    final Provider<String> fullInputPath = sourcePath.map { "${project.projectDir}/${it}" }

    CookieCutterCompareTask() {
        outputPath.convention('cookiecutter')
        sourcePath.convention('cookiecutter')
        binary.convention('diff')
        taskTimeout.convention(10000L)
        stdOmitFiles.addAll('.git',
                '.idea',
                '.gradle',
                'build',
                'reports',
                'template',
                '.circleci',
                '.pre-commit-config.yaml',
                'catalog-info.yaml',
                'mkdocs.yml',
                'copy-plugin-examples.sh',
                'alter-path.sh',
                'apply-sed.sh',
                'service.sed',
                'tmp',
                'verify-generated-proj.sh',
                'out')
        doLast {
            log.debug("l:task:generatedProjectName [{}]", generatedProjectName.get())
            log.debug("l:task:outputPath [{}]", outputPath.get())
            log.debug("l:task:sourcePath [{}]", sourcePath.get())
            log.debug("l:task:taskTimeout [{}]", taskTimeout.get())
            log.debug("l:task:stdOmitFiles [{}]", stdOmitFiles.get().size())
            log.debug("l:task:omitFiles [{}]", omitFiles.get().size())

            StringBuilder omit = new StringBuilder()
            omitFiles.get().each { p -> omit.append(' -x ').append(p) }
            stdOmitFiles.get().each { p -> omit.append(' -x ').append(p) }
            def cmdLine = "${binary.get()} -r  ${omit.toString()} ${fullOutputPath.get()} ${fullInputPath.get()} "
            log.debug("[{}]: {}", project.projectDir, cmdLine)
            def proc = cmdLine.execute(null, project.projectDir)

            StringBuilder sout = new StringBuilder()
            StringBuilder serr = new StringBuilder()
            //proc.consumeProcessOutput(sout, serr)
            proc.in.eachLine { line -> log.quiet(line) }
            proc.out.close()
            proc.waitFor()
            //proc.waitForOrKill(taskTimeout.get())
            log.quiet("Exit code: [{}]", proc.exitValue())
            //log.quiet("Std err: [{}]", proc.err.text)
            //log.quiet("Std out: [{}]", proc.in.text)
            return proc.exitValue()
        }
    }
}
