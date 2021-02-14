package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.GradleBuild
import org.slf4j.LoggerFactory


class CookieCutterPlugin implements Plugin<Project> {
    def logger = LoggerFactory.getLogger(CookieCutterPlugin.class)
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

        project.task('generateTemplates', type: CookieCutterMultiGenerateTask) {
            group = 'cookiecutter'
            description = "Generate multiple templates to the same output directory"
            doFirst {
                logger.debug("f:gt:template [{}]", extension.template.get())
                logger.debug("f:gt:outputPath [{}]", extension.outputPath.get())
                logger.debug("f:gt:binary [{}]", extension.templateBinary.get())
            }
            logger.debug("c:gt:template [{}]", extension.template.get())
            logger.debug("c:gt:outputPath [{}]", extension.outputPath.get())
            logger.debug("c:gt:binary [{}]", extension.templateBinary.get())
            templates = extension.templates
            outputPath = extension.outputPath
            binary = extension.templateBinary
            context = extension.context
            taskTimeout = extension.taskTimeout
            doLast {
                logger.debug("l:gt:template [{}]", extension.template.get())
                logger.debug("l:gt:outputPath [{}]", extension.outputPath.get())
                logger.debug("l:gt:binary [{}]", extension.templateBinary.get())
            }
        }

        project.task('generateTemplate', type: CookieCutterGenerateTask) {
            group = 'cookiecutter'
            description = "Generate a single template to an output directory"
            template = extension.template
            outputPath = extension.outputPath
            binary = extension.templateBinary
            context = extension.context
            taskTimeout = extension.taskTimeout
        }

        project.task('compareTemplate', type: CookieCutterCompareTask) {
            group = 'cookiecutter'
            description = "Compare a generated template with the original (non-template) source"
            outputPath = verifyExtension.outputPath
            sourcePath = verifyExtension.sourcePath
            generatedProjectName = verifyExtension.generatedProjectName
            omitFiles = verifyExtension.omitFiles
            binary = verifyExtension.diffBinary
            mustRunAfter('generateTemplate', 'generateTemplates')
        }

        project.task('buildTemplate', type: Exec) {
            group = 'cookiecutter'
            description = "Build the generated template"
            executable(buildExtension.buildBinary.get()) // set this upfront, update in closure
            mustRunAfter('generateTemplate', 'generateTemplates')
            doFirst {
                logger.debug("f:dir [${project.buildDir}/${buildExtension.outputPath.get()}/${buildExtension.generatedProjectName.get()}]")
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

        project.task('testTemplate') {
            group = 'cookiecutter'
            description = "Test one template"
            dependsOn('generateTemplate', 'buildTemplate')
        }
        project.task('testTemplates') {
            group = 'cookiecutter'
            description = "Test collection of templates"
            dependsOn('generateTemplates', 'buildTemplate')
        }
    }
}
