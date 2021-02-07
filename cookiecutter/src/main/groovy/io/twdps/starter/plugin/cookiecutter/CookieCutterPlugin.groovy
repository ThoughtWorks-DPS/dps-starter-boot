package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec


class CookieCutterPlugin implements Plugin<Project> {
    void apply(Project project) {
        def extension = project.extensions.create('cookiecutter', CookieCutterPluginExtension)
        def verifyExtension = project.extensions.create('cookiecutterVerify', CookieCutterCompareTaskExtension)
        project.task('clean', type: Exec) {
            executable('rm')
            args('-rf', "${project.buildDir}/${extension.outputPath.get()}")
        }
        project.task('generateTemplatesDebug', type: CookieCutterMultiGenerateTask) {
            doFirst {
                println "f:template [${extension.template.get()}]"
                println "f:outputPath [${extension.outputPath.get()}]"
                println "f:binary [${extension.templateBinary.get()}]"
                extension.templates.each { p ->
                    println "f:templates [${p.get()}]"
                }
            }
            doLast {
                println "l:template [${extension.template.get()}]"
                println "l:outputPath [${extension.outputPath.get()}]"
                println "l:binary [${extension.templateBinary.get()}]"
                extension.templates.each { p ->
                    println "l:templates [${p.get()}]"
                }
            }
            println "template [${extension.template.get()}]"
            println "outputPath [${extension.outputPath.get()}]"
            println "binary [${extension.templateBinary.get()}]"
            extension.templates.each { p ->
                println "templates [${p.get()}]"
            }
            templates = extension.templates;
            outputPath = extension.outputPath;
            binary = extension.templateBinary
        }
        project.task('generateTemplates', type: CookieCutterMultiGenerateTask) {
            templates = extension.templates;
            outputPath = extension.outputPath;
            binary = extension.templateBinary
        }
        project.task('generateTemplate', type: CookieCutterGenerateTask) {
            template = extension.template;
            outputPath = extension.outputPath;
            binary = extension.templateBinary
        }
        project.task('compareTemplate', type: CookieCutterCompareTask) {
            outputPath = verifyExtension.outputPath;
            sourcePath = verifyExtension.sourcePath;
            generatedProjectName = verifyExtension.generatedProjectName;
            omitFiles = verifyExtension.omitFiles
            binary = verifyExtension.diffBinary
        }
    }
}
