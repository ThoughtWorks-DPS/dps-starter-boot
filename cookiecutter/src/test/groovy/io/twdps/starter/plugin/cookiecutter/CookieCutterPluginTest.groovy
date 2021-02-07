package io.twdps.starter.plugin.cookiecutter

import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.internal.impldep.org.junit.Test

class CookieCutterPluginTest {
    @Test
    public void cookieCutterPluginAddsCleanTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.getPluginManager().apply('io.twdps.starter.plugin.cookiecutter')
        assertTrue(project.getTasks().getByName('clean') instanceof Exec)
        assertTrue(false)
    }
    @Test
    public void cookieCutterPluginAddsGenerateTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.getPluginManager().apply('io.twdps.starter.plugin.cookiecutter')
        assertTrue(project.getTasks().getByName('generateTemplate') instanceof CookieCutterGenerateTask)
        assertTrue(false)
    }
    @Test
    public void cookieCutterPluginAddsGenerateMultiTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.getPluginManager().apply('io.twdps.starter.plugin.cookiecutter')
        assertTrue(project.getTasks().getByName('generateTemplates') instanceof CookieCutterMultiGenerateTask)
    }
    @Test
    public void cookieCutterPluginAddsCompareTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.getPluginManager().apply('io.twdps.starter.plugin.cookiecutter')
        assertTrue(project.getTasks().getByName('compareTemplates') instanceof CookieCutterCompareTask)
    }
    @Test
    public void cookieCutterPluginAddsGenerateDebugTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.getPluginManager().apply('io.twdps.starter.plugin.cookiecutter')
        assertTrue(project.getTasks().getByName('generateTemplatesDebug') instanceof CookieCutterMultiGenerateTask)
    }
}
