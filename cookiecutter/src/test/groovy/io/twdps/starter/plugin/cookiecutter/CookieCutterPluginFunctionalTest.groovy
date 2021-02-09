package io.twdps.starter.plugin.cookiecutter

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Stepwise

import java.nio.file.Files
import java.nio.file.Path

@Stepwise
class CookieCutterPluginFunctionalTest extends Specification {
    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()
    File settingsFile
    File buildFile

    String templatePaths = "{{cookiecutter.PROJECT_NAME}}/{{cookiecutter.PKG_TL_NAME}}/{{cookiecutter.PKG_ORG_NAME}}/{{cookiecutter.PKG_GROUP_NAME}}"
    String originalPaths = "original/io/twdps/starter"

    def writeCookieCutter(File output) {
        output << """
{
  "PROJECT_NAME" : "original",
  "GITHUB_ORG_NAME": "ThoughtWorks-DPS",
  "PACKAGE_NAME": "{{cookiecutter.PROJECT_NAME|lower|replace(' ', '-')|replace('_', '-')}}",
  "PROJECT_DESCRIPTION": "Java API Starter from Template",
  "PROJECT_OWNER": "starter-java@example.com",
  "DOCKER_REGISTRY" : "docker.pkg.github.com/thoughtworks-dps/{{cookiecutter.PACKAGE_NAME}}",
  "PKG_TL_NAME": "io",
  "PKG_ORG_NAME": "twdps",
  "PKG_GROUP_NAME": "starter",
  "BASE_PACKAGE": "{{cookiecutter.PKG_TL_NAME}}.{{cookiecutter.PKG_ORG_NAME}}.{{cookiecutter.PKG_GROUP_NAME}}",
  "SERVICE_NAME": "Example",
  "PKG_SERVICE_NAME": "{{cookiecutter.SERVICE_NAME|lower}}",
  "SERVICE_URL": "{{cookiecutter.SERVICE_NAME|lower}}",
  "RESOURCE_NAME": "Foo",
  "RESOURCE_TABLE_NAME": "{{cookiecutter.RESOURCE_NAME|lower}}",
  "RESOURCE_NAME_PLURAL": "{{cookiecutter.RESOURCE_NAME}}s",
  "RESOURCE_URL": "{{cookiecutter.RESOURCE_NAME_PLURAL|lower}}",
  "PKG_RESOURCE_NAME": "{{cookiecutter.RESOURCE_NAME|lower}}",
  "SUB_RESOURCE_NAME": "Sub{{cookiecutter.RESOURCE_NAME}}",
  "SUB_RESOURCE_NAME_PLURAL": "{{cookiecutter.SUB_RESOURCE_NAME}}s",
  "SUB_RESOURCE_URL": "{{cookiecutter.SUB_RESOURCE_NAME_PLURAL|lower}}",
  "PKG_SUB_RESOURCE_NAME": "{{cookiecutter.SUB_RESOURCE_NAME|lower}}",
  "_copy_without_render": [
    ".circleci/config.yml",
    "secrethub.env",
    "helm/templates/*",
    "helmchart/values.yaml",
    "helmchart/templates/*",
    "tpl/*"
  ]
}
"""
    }

    def writeTemplateService(File output) {
        output << """
package {{cookiecutter.PKG_TL_NAME}}.{{cookiecutter.PKG_ORG_NAME}}.{{cookiecutter.PKG_GROUP_NAME}}.service.spi.{{cookiecutter.PKG_RESOURCE_NAME}};

import {{cookiecutter.PKG_TL_NAME}}.{{cookiecutter.PKG_ORG_NAME}}.{{cookiecutter.PKG_GROUP_NAME}}.model.{{cookiecutter.PKG_RESOURCE_NAME}}.{{cookiecutter.RESOURCE_NAME}}Model;

import java.util.List;
import java.util.Optional;

public interface {{cookiecutter.RESOURCE_NAME}}Service {
  {{cookiecutter.RESOURCE_NAME}}Model add({{cookiecutter.RESOURCE_NAME}}Model account);
  List<{{cookiecutter.RESOURCE_NAME}}Model> findByLastName(String lastName);
  Optional<{{cookiecutter.RESOURCE_NAME}}Model> findByUserName(String userName);
  Optional<{{cookiecutter.RESOURCE_NAME}}Model> findById(String id);
  List<{{cookiecutter.RESOURCE_NAME}}Model> findAll();
  Optional<{{cookiecutter.RESOURCE_NAME}}Model> updateById(String id, {{cookiecutter.RESOURCE_NAME}}Model record);
  Optional<{{cookiecutter.RESOURCE_NAME}}Model> deleteById(String id);
}
"""
    }

    def writeTemplateModel(File output) {
        output << """
package {{cookiecutter.PKG_TL_NAME}}.{{cookiecutter.PKG_ORG_NAME}}.{{cookiecutter.PKG_GROUP_NAME}}.model.{{cookiecutter.PKG_RESOURCE_NAME}};

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class {{cookiecutter.RESOURCE_NAME}}Model {

  @NonNull private final String userName;
  @NonNull private final String firstName;
  @NonNull private final String lastName;

  /** Create object from json.
   *
   * @param userName  username of {{cookiecutter.RESOURCE_NAME}} holder
   * @param firstName firstname of {{cookiecutter.RESOURCE_NAME}} holder
   * @param lastName lastname of {{cookiecutter.RESOURCE_NAME}} holder
   */
  @JsonCreator
  public {{cookiecutter.RESOURCE_NAME}}Model(
      @JsonProperty("userName") String userName,
      @JsonProperty("firstName") String firstName,
      @JsonProperty("lastName") String lastName) {
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
"""
    }

    def writeFooService(File output) {
        output << """
package io.twdps.starter.service.spi.foo;

import io.twdps.starter.model.foo.FooModel;

import java.util.List;
import java.util.Optional;

public interface FooService {
  FooModel add(FooModel account);
  List<FooModel> findByLastName(String lastName);
  Optional<FooModel> findByUserName(String userName);
  Optional<FooModel> findById(String id);
  List<FooModel> findAll();
  Optional<FooModel> updateById(String id, FooModel record);
  Optional<FooModel> deleteById(String id);
}
"""
    }

    def writeFooModel(File output) {
        output << """
package io.twdps.starter.model.foo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FooModel {

  @NonNull private final String userName;
  @NonNull private final String firstName;
  @NonNull private final String lastName;

  /** Create object from json.
   *
   * @param userName  username of Foo holder
   * @param firstName firstname of Foo holder
   * @param lastName lastname of Foo holder
   */
  @JsonCreator
  public FooModel(
      @JsonProperty("userName") String userName,
      @JsonProperty("firstName") String firstName,
      @JsonProperty("lastName") String lastName) {
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
"""
    }

    def writeBuildFile(File output) {
        output << """
        
plugins { 
    id 'io.twdps.starter.plugin.cookiecutter' 
} 

cookiecutter { template = 'template' }

cookiecutterVerify {
    generatedProjectName = 'original'
    sourcePath = 'original'
}

cookiecutterBuild {
//    args = [ "clean", "build", "test" ]
    args = ['-altr']
    buildBinary = 'ls'
    generatedProjectName = 'original'
}
"""
    }

    def writeSettingsFile(File output) {
        output << "rootProject.name = 'test-cookiecutter'"
    }

    File makePath(File parent, String path) {
        File result = new File(parent, path)
        Path p = Files.createDirectories(result.toPath())
        return result;
    }

    File makeOriginalFiles(File parent) {
        File starter = makePath(parent, originalPaths);
        File service = makePath(starter, "service/foo")
        writeFooService(new File(service, "FooService.java"))
        File model = makePath(starter, "model/foo")
        writeFooModel(new File(model, "FooModel.java"))
    }

    File makeTemplateFiles(File parent) {
        File template = makePath(parent, "template");
        File cookiecutter = new File(template, "cookiecutter.json")
        println cookiecutter.toPath().toAbsolutePath()
        writeCookieCutter(cookiecutter)
        File starter = makePath(template, templatePaths);
        File service = makePath(starter, "service/{{cookiecutter.PKG_RESOURCE_NAME}}")
        writeTemplateService(new File(service, "{{cookiecutter.RESOURCE_NAME}}Service.java"))
        File model = makePath(starter, "model/{{cookiecutter.PKG_RESOURCE_NAME}}")
        writeTemplateModel(new File(model, "{{cookiecutter.RESOURCE_NAME}}Model.java"))
    }

    def setup() {
        settingsFile = testProjectDir.newFile('settings.gradle')
        buildFile = testProjectDir.newFile('build.gradle')
    }

    /* NOTE: Currently this doesn't work in CircleCI (likely because of lacking 'diff' binary)
    def "can find diffs in generated template"() {
        given:
        writeSettingsFile(settingsFile)
        writeBuildFile(buildFile)
        //makeOriginalFiles(testProjectDir.root)
        makeTemplateFiles(testProjectDir.root)

        when:
        def result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir.root)
                .withArguments('generateTemplate', 'compareTemplate')
                .build()

        then:
        result.task(":generateTemplate").outcome == TaskOutcome.SUCCESS
        result.task(":compareTemplate").outcome == TaskOutcome.SUCCESS
    }
     */

    def "can execute template task"() {
        given:
        writeSettingsFile(settingsFile)
        writeBuildFile(buildFile)
        makeOriginalFiles(testProjectDir.root)
        makeTemplateFiles(testProjectDir.root)

        when:
        def result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir.root)
                .withArguments('generateTemplate')
                .build()
        File buildDir = new File(testProjectDir.root, 'build/cookiecutter')
        File output = new File(testProjectDir.root, "build/cookiecutter/original")
        File model = new File(testProjectDir.root, "build/cookiecutter/original/io/twdps/starter/model/foo/FooModel.java")

        then:
        buildDir.isDirectory() == true
        output.isDirectory() == true
        model.isFile() == true
        result.task(":generateTemplate").outcome == TaskOutcome.SUCCESS

    }

    /* NOTE: have not succeeded in getting a build to work, the test case would need
     * a whole gradlew/gradle-wrapper.jar included, and copying files in gradle for test-cases
     * is not clear (to me... yet)
     * /
    def "can build generated template"() {
        given:
        writeSettingsFile(settingsFile)
        writeBuildFile(buildFile)
        makeOriginalFiles(testProjectDir.root)
        makeTemplateFiles(testProjectDir.root)

        when:
        def result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir.root)
                .withArguments('generateTemplate', 'buildTemplate')
                .build()
        File buildDir = new File(testProjectDir.root, 'build/cookiecutter')
        File output = new File(testProjectDir.root, "build/cookiecutter/original")
        File model = new File(testProjectDir.root, "build/cookiecutter/original/io/twdps/starter/model/foo/FooModel.java")
        println "Files in ${buildDir.getAbsolutePath()}"
        buildDir.list().each {f -> println f }

        then:
        buildDir.isDirectory() == true
        output.isDirectory() == true
        model.isFile() == true
        result.task(":generateTemplate").outcome == TaskOutcome.SUCCESS
        result.task(":buildTemplate").outcome == TaskOutcome.SUCCESS
    }
    */

}
