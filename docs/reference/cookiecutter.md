# Gradle Cookiecutter Plugins

Provides Gradle Plugins to aid in the generation and testing of templates.

## Overview

The Cookiecutter plugins provide tasks to generate, build, and compare templates.

## generateTemplate

The `generateTemplate` task will generate the template.
The template will be output in the `project.buildDir`/`outputPath` folder.
The output folder path will be created if it does not exist.

|Property|Default|Description|
|--------|-------|-----------|
|template|.|Path to template to execute|
|templates| |List of templates to generate|
|outputPath|cookiecutter|Path under `project.buildDir` for generated content|
|inputPath|..|Path for input data|
|templateBinary|cookiecutter|Template generation binary|
|diffBinary|diff|Binary of diff utility|
|taskTimeout|10000|Timeout in seconds for template generation process to complete successfully|
|context| |List of `property=value` arguments for Cookiecutter command to override settings in `cookiecutter.json`|
|omitCompare| |List of files to omit from the comparison|

## generateTemplates

The `generateTemplates` task uses the same configuration properties as `generateTemplate` task.
However, it will generate multiple templates, based on the names in the `templates` list.

## buildTemplate

The `buildTemplate` task will build the generated content.

|Property|Default|Description|
|--------|-------|-----------|
|outputPath|cookiecutter|Path under `project.buildDir` for generated content|
|generatedProjectName|cookiecutter|Name of the generated project|
|buildBinary|./gradlew|The command to build the generated code|
|args| |List of arguments to pass to the `buildBinary`|

## compareTemplate

The `compareTemplate` task will compare the generated content with original source content.
The comparison is recursive, and will traverse both trees making comparisons (or noting existence) along the tree traversal.

|Property|Default|Description|
|--------|-------|-----------|
|outputPath|cookiecutter|Path under `project.buildDir` for generated content|
|sourcePath|..|Path to original comparison source|
|generatedProjectName|cookiecutter|Name of the generated project|
|diffBinary|diff|Binary of diff utility|
|omitFiles| |List of files to omit from the comparison|

The `compareTemplate` task also omits the following files, in addition to the files specified in the `omitFiles` configuration property.

* .circleci
* .git
* .gradle
* .idea
* .pre-commit-config.yaml
* alter-path.sh
* apply-sed.sh
* build
* catalog-info.yaml
* copy-plugin-examples.sh
* mkdocs.yml
* out
* reports
* service.sed
* template
* tmp
* verify-generated-proj.sh

## testTemplate

The `testTemplate` task is a shortcut that will run `generateTemplate` and `buildTemplate` tasks in succession.

