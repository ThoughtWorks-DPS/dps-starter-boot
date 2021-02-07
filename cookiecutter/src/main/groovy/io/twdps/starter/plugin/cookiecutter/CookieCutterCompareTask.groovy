package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

class CookieCutterCompareTask extends DefaultTask {

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
        outputPath.set('cookiecutter')
        sourcePath.set('cookiecutter')
        binary.set('diff')
        taskTimeout.set(10000L)
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
            println "l:task [${generatedProjectName.get()}]"
            println "l:task [${outputPath.get()}]"
            println "l:task [${sourcePath.get()}]"
            println "l:task [${taskTimeout.get()}]"
            println "l:task [${stdOmitFiles.get().size()}]"
            println "l:task [${omitFiles.get().size()}]"

            StringBuilder omit = new StringBuilder()
            omitFiles.get().each { p -> omit.append(' -x ').append(p) }
            stdOmitFiles.get().each { p -> omit.append(' -x ').append(p) }
            def cmdLine = "${binary.get()} -r  ${omit.toString()} ${fullOutputPath.get()} ${fullInputPath.get()} "
            println "[${project.projectDir}]: ${cmdLine}"
            def proc = cmdLine.execute(null, project.projectDir)

            StringBuilder sout = new StringBuilder()
            StringBuilder serr = new StringBuilder()
            //proc.consumeProcessOutput(sout, serr)
            proc.waitForOrKill(taskTimeout.get())
            println "Exit code: ${proc.exitValue()}"
            println "Std err: ${proc.err.text}" //sout.toString()
            println "Std out: ${proc.in.text}" //serr.toString()

        }
    }
}
