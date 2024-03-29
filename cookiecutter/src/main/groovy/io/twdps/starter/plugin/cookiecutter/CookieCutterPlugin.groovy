package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.slf4j.LoggerFactory


class CookieCutterPlugin implements Plugin<Project> {
    def logger = LoggerFactory.getLogger(CookieCutterPlugin.class)
    void apply(Project project) {
        def extension = project.extensions.create('cookiecutter', CookieCutterPluginExtension)
        def verifyExtension = project.extensions.create('cookiecutterVerify', CookieCutterCompareTaskExtension)
        def buildExtension = project.extensions.create('cookiecutterBuild', CookieCutterLaunchBuildTaskExtension)
        def formatExtension = project.extensions.create('cookiecutterFormat', CookieCutterLaunchBuildTaskExtension)
        project.getTasks().register('clean', Exec)
                .configure {
            group = 'cookiecutter'
            description = "Clean up cookiecutter template output directory"
            executable('rm')
            args('-rf', "${project.buildDir}/${extension.outputPath.get()}")
        }

        project.getTasks().register('generateTemplates', CookieCutterMultiGenerateTask)
                .configure {
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

        project.getTasks().register('generateTemplate', CookieCutterGenerateTask)
                .configure {
            group = 'cookiecutter'
            description = "Generate a single template to an output directory"
            template = extension.template
            outputPath = extension.outputPath
            binary = extension.templateBinary
            context = extension.context
            taskTimeout = extension.taskTimeout
            outputs.upToDateWhen { false }
        }

        project.getTasks().register('compareTemplate', CookieCutterCompareTask)
                .configure {
            group = 'cookiecutter'
            description = "Compare a generated template with the original (non-template) source"
            outputPath = verifyExtension.outputPath
            sourcePath = verifyExtension.sourcePath
            generatedProjectName = verifyExtension.generatedProjectName
            omitFiles = verifyExtension.omitFiles
            binary = verifyExtension.diffBinary
            mustRunAfter('generateTemplate', 'generateTemplates', 'formatTemplate')
        }

        project.getTasks().register('buildTemplate', Exec)
                .configure {
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

        project.getTasks().register('formatTemplate', Exec)
                .configure {
                    group = 'cookiecutter'
                    description = "Format the generated template"
                    executable(formatExtension.buildBinary.get()) // set this upfront, update in closure
                    mustRunAfter('generateTemplate', 'generateTemplates')
                    doFirst {
                        logger.debug("f:dir [${project.buildDir}/${formatExtension.outputPath.get()}/${formatExtension.generatedProjectName.get()}]")
                        executable(formatExtension.buildBinary.get())
                        workingDir = "${project.buildDir}/${formatExtension.outputPath.get()}/${formatExtension.generatedProjectName.get()}"
                        args(formatExtension.args.get())
                    }
                }

/*
        project.getTasks().register('testBuildTemplate', CookieCutterLaunchBuildTask)
                .configure {
            group = 'cookiecutter'
            description = "Build the generated template"
            executable(buildExtension.buildBinary.get()) // set this upfront, update in closure
            outputPath = buildExtension.outputPath;
            binary = buildExtension.buildBinary
            argList = buildExtension.args
            generatedProjectName = verifyExtension.generatedProjectName;
        }
         */

        project.getTasks().register('testTemplate')
                .configure {
            group = 'cookiecutter'
            description = "Test one template"
            dependsOn('generateTemplate', 'buildTemplate')
        }
        project.getTasks().register('testTemplates')
                .configure {
            group = 'cookiecutter'
            description = "Test collection of templates"
            dependsOn('generateTemplates', 'buildTemplate')
        }
    }
}
