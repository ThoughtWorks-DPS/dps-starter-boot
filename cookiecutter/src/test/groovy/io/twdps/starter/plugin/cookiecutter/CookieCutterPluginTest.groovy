package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.tasks.Exec
import spock.lang.Specification

class CookieCutterPluginTest extends Specification {

    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.getPluginManager().apply('io.twdps.starter.plugin.cookiecutter')
    }
    def cookieCutterPluginAddsCleanTaskToProject() {
        when: def t = project.getTasks().getByName('clean')
        then: t instanceof Exec
    }
    def cookieCutterPluginAddsGenerateTemplateTaskToProject() {
        when: def t = project.getTasks().getByName('generateTemplate')
        then: t instanceof CookieCutterGenerateTask
    }
    def cookieCutterPluginAddsGenerateTemplatesTaskToProject() {
        when: def t = project.getTasks().getByName('generateTemplates')
        then: t instanceof CookieCutterMultiGenerateTask
    }
    def cookieCutterPluginAddsCompareTaskToProject() {
        when: def t = project.getTasks().getByName('generateTemplates')
        then: t instanceof CookieCutterMultiGenerateTask
    }
    def cookieCutterPluginAddsBuildTemplateTaskToProject() {
        when: def t = project.getTasks().getByName('buildTemplate')
        then: t instanceof Exec
    }
}
