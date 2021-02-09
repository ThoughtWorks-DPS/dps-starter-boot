package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.GradleBuild


class CookieCutterPlugin implements Plugin<Project> {
    void apply(Project project) {
        def extension = project.extensions.create('cookiecutter', CookieCutterPluginExtension)
        def verifyExtension = project.extensions.create('cookiecutterVerify', CookieCutterCompareTaskExtension)
        def buildExtension = project.extensions.create('cookiecutterBuild', CookieCutterLaunchBuildTaskExtension)
        project.task('clean', type: Exec) {
            group = 'cookiecutter'
            description = "Clean up oookiecutter template output directory"
            executable('rm')
            args('-rf', "${project.buildDir}/${extension.outputPath.get()}")
        }
        project.task('generateTemplatesDebug', type: CookieCutterMultiGenerateTask) {
            doFirst {
                println "f:gtd:template [${extension.template.get()}]"
                println "f:gtd:outputPath [${extension.outputPath.get()}]"
                println "f:gtd:binary [${extension.templateBinary.get()}]"
                extension.templates.each { p ->
                    println "f:templates [${p.get()}]"
                }
            }
            doLast {
                println "l:gtd:template [${extension.template.get()}]"
                println "l:gtd:outputPath [${extension.outputPath.get()}]"
                println "l:gtd:binary [${extension.templateBinary.get()}]"
                extension.templates.each { p ->
                    println "l:templates [${p.get()}]"
                }
            }
            group = 'cookiecutter'
            description = "Clean up oookiecutter template output directory"
            println "c:gtd:template [${extension.template.get()}]"
            println "c:gtd:outputPath [${extension.outputPath.get()}]"
            println "c:gtd:binary [${extension.templateBinary.get()}]"
            extension.templates.each { p ->
                println "templates [${p.get()}]"
            }
            templates = extension.templates;
            outputPath = extension.outputPath;
            binary = extension.templateBinary
        }
        project.task('generateTemplates', type: CookieCutterMultiGenerateTask) {
            group = 'cookiecutter'
            description = "Generate multiple templates to the same output directory"
            templates = extension.templates;
            outputPath = extension.outputPath;
            binary = extension.templateBinary
        }
        project.task('generateTemplate', type: CookieCutterGenerateTask) {
            group = 'cookiecutter'
            description = "Generate a single template to an output directory"
            template = extension.template;
            outputPath = extension.outputPath;
            binary = extension.templateBinary
        }
        project.task('compareTemplate', type: CookieCutterCompareTask) {
            group = 'cookiecutter'
            description = "Compare a generated template with the original (non-template) source"
            outputPath = verifyExtension.outputPath;
            sourcePath = verifyExtension.sourcePath;
            generatedProjectName = verifyExtension.generatedProjectName;
            omitFiles = verifyExtension.omitFiles
            binary = verifyExtension.diffBinary
        }
        project.task('buildTemplate', type: Exec) {
            group = 'cookiecutter'
            description = "Build the generated template"
            executable(buildExtension.buildBinary.get()) // set this upfront, update in closure
            doFirst {
                println "f:dir [${project.buildDir}/${buildExtension.outputPath.get()}/${buildExtension.generatedProjectName.get()}]"
                executable(buildExtension.buildBinary.get())
                workingDir = "${project.buildDir}/${buildExtension.outputPath.get()}/${buildExtension.generatedProjectName.get()}"
                args(buildExtension.args.get())
            }
        }
        /*
        project.task('testBuildTemplate', type: CookieCutterLaunchBuildTask) {
            group = 'cookiecutter'
            description = "Build the generated template"
            executable(buildExtension.buildBinary.get()) // set this upfront, update in closure
            outputPath = buildExtension.outputPath;
            binary = buildExtension.buildBinary
            argList = buildExtension.args
            generatedProjectName = verifyExtension.generatedProjectName;
        }
         */
        project.task('test') {
            group = 'cookiecutter'
            description = "Test template"
            dependsOn('generateTemplate', 'buildTemplate')
        }
    }
}
