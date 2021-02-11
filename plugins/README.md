# Gradle Mixins

NOTE: This readme is generated from the plugin source files.
Do not edit directly.

The mixins are meant to provide snippets of Gradle configuration based on specific functional groupings.
The groups are identified as starter scripts, and roughly grouped:

* java - basic configuration around a typical build objective
* metrics - build timing configuration
* std - top-level configs based on target type

The `std` level of configuration is organized by artifact type.
This is the only level meant to organize or include other configs.
The design is meant to be easily overridden by teams that wish to depart from the standard configs.
Teams are able to just copy the `std` level conventions to their project and override those they wish to change.
Items they don't need to change can continue to be referred in their local configs.

The intention is that the `starter-boot` package can externalize these mixins as plugins so the team can continue to refer to those mixins that do not need to change.
To that end, do not include or build upon other convention files, except at the `std` level.

I think the only exception to this rule is `starter.java.style-conventions`, which includes `starter.java.checktyle-conventions`.
This was only allowed because `style-conventions` is aggregating `checkstyle` and `spotless`.
If 'spotless' gets more complicated, then these two should be split and propagated upwards instead of being aggregated under `style-conventions`.



## starter.java.checkstyle-conventions.gradle

```groovy
/**
 * Setting for running checkstyle, pulls configuration from the checkstyle jar.
 */

plugins {
    id 'checkstyle'
}

configurations {
    checkstyleRules
}

dependencies {
    checkstyleRules platform('io.twdps.starter:checkstyle-bom')
    checkstyleRules 'io.twdps.starter:checkstyle'
}

checkstyle {
    toolVersion "${checkstyle_version}"
//    configFile = rootProject.file('settings/checkstyle/checkstyle.xml')
    config project.resources.text.fromArchiveEntry(configurations.checkstyleRules, 'settings/checkstyle/checkstyle.xml')
    configProperties = [
            'checkstyle.cache.file': "${buildDir}/checkstyle.cache",
    ]
    ignoreFailures = true
    showViolations = true

}

checkstyleMain {
    source = "src/main/java"
}
checkstyleTest {
    source = "src/test/java"
}

```

## starter.java.config-conventions.gradle

```groovy
/**
 * Provides configurations for platform (BOM) dependencies, to ensure non-api/runtime configurations are set properly.
 */

configurations {
    springBom
    compileOnly.extendsFrom(springBom)
    annotationProcessor.extendsFrom(springBom)
    implementation.extendsFrom(springBom)
    testAnnotationProcessor.extendsFrom(springBom)

    annotationBom
    implementation.extendsFrom(annotationBom)
    annotationProcessor.extendsFrom(annotationBom)
    testAnnotationProcessor.extendsFrom(annotationBom)

    checkstyleRules
}

```

## starter.java.container-conventions.gradle

```groovy
/**
 * Provides docker container settings
 */

plugins {
    id 'com.palantir.docker'
    id 'com.palantir.docker-run'
}

ext {
    dockerRegistry =  project.hasProperty("dockerRegistry") ? "${project.dockerRegistry}" : "${group}"
    dockerImageVersion = project.hasProperty("buildNumber") ? "${project.version}-${project.buildNumber}" : project.version
}

docker {
    dependsOn(assemble)
    name "${dockerRegistry}/${rootProject.name}"
    tag "Build", "${dockerRegistry}/${rootProject.name}:${dockerImageVersion}"
    tag "Latest", "${dockerRegistry}/${rootProject.name}:latest"
    noCache true
    files "build/libs/${bootJar.archiveFileName.get()}", 'bin'
    buildArgs([JAR_FILE: bootJar.archiveFileName.get()])
}

dockerRun {
    name project.name
    image "${dockerRegistry}/${rootProject.name}"
    ports '8080:8080'
    env 'SECRETHUB_HELLO': (System.getenv('SECRETHUB_HELLO') == null
            ? 'override-me'
            : System.getenv('SECRETHUB_HELLO'))
}

task dockerPrune(type: Exec) {
    dependsOn('dockerStop', 'dockerRemoveContainer')
    dockerRemoveContainer.mustRunAfter('dockerStop')
    commandLine './scripts/docker-prune.sh'
}

task dockerStart(type: GradleBuild) {
    tasks = ["dockerPrune","clean", "dockerClean", "docker", "dockerRun"]
}
```

## starter.java.coordinate-conventions.gradle

```groovy
/**
 * Provides shortcuts for overriding group and version.
 * NOTE: Most likely obsolete, in favor of specifying group directly in the gradle.properties file, and using axion to supply version based on git tags.
 */

/*
group = "${module_group}"
version getTagOrDefault("${module_version}")

public static String getTagOrDefault(String defaultValue) {
    String ref = System.getenv('GITHUB_REF')

    if (ref && ref.startsWith('refs/tags/')) {
        return ref.substring('refs/tags/'.length())
    }

    return defaultValue
}
*/
```

## starter.java.deps-build-conventions.gradle

```groovy
/**
 * Provides a set of common dependencies for typical build.
 * Includes proper dependencies for lombok and mapstruct annotation processing.
 */

sourceCompatibility = '11'

dependencies {
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'

    implementation "org.springdoc:springdoc-openapi-ui"
    implementation "org.springdoc:springdoc-openapi-webmvc-core"
    //implementation "org.springdoc:springdoc-openapi-security"
    implementation "org.springdoc:springdoc-openapi-data-rest"
    implementation 'org.mapstruct:mapstruct'

    compileOnly 'org.projectlombok:lombok'

    annotationProcessor 'org.projectlombok:lombok'
    annotationProcessor 'org.mapstruct:mapstruct-processor'

}

```

## starter.java.deps-integration-conventions.gradle

```groovy
/**
 * Provides a set of common dependencies for typical integration test.
 */

sourceCompatibility = '11'


dependencies {

    integrationTestImplementation 'org.assertj:assertj-core'
    integrationTestImplementation 'org.junit.jupiter:junit-jupiter-api'
    integrationTestImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group:'org.junit.vintage', module: 'junit-vintage-engine'
    }

    integrationTestRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'
}

```

## starter.java.deps-plugin-conventions.gradle

```groovy
/**
 * Provides a set of common dependencies for typical gradle plugin unit test .
 */

sourceCompatibility = '11'


dependencies {
    testImplementation('org.spockframework:spock-core') {
        exclude module: 'groovy-all'
    }
}

```

## starter.java.deps-plugin-integration-conventions.gradle

```groovy
/**
 * Provides a set of common dependencies for typical gradle plugin integration test .
 */

sourceCompatibility = '11'

dependencies {
    integrationTestImplementation('org.spockframework:spock-core') {
        exclude module: 'groovy-all'
    }
    integrationTestImplementation gradleTestKit()
}

```

## starter.java.deps-test-conventions.gradle

```groovy
/**
 * Provides a set of common dependencies for typical unit testing.
 */

sourceCompatibility = '11'

dependencies {
    testCompile 'org.mockito:mockito-core'
    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'

    testImplementation 'com.tngtech.archunit:archunit'
    testImplementation 'org.assertj:assertj-core'
    testImplementation 'org.junit.jupiter:junit-jupiter-api'
    //since this is a spring boot starter, assume spring at all levels
    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group:'org.junit.vintage', module: 'junit-vintage-engine'
    }
    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'

}

```

## starter.java.doc-springdoc-conventions.gradle

```groovy
/**
 * plugins supporting generation of OpenAPI docs from code
 */

plugins {
    id "com.github.johnrengelman.processes"
    id "org.springdoc.openapi-gradle-plugin"
}

```

## starter.java.doc-swagger-conventions.gradle

```groovy
/**
 * Swaggerhub configurations
 */

plugins {
    id "io.swagger.swaggerhub"
}

```

## starter.java.open-tracing-common-conventions.gradle

```groovy
/**
 * Typical dependencies to implement open tracing.
 */

dependencies {
    // Tracing support ==========================================================
    api 'io.opentracing.brave:brave-opentracing'
    api 'io.opentracing:opentracing-api'
    api 'io.zipkin.reporter2:zipkin-reporter'
    api 'io.zipkin.reporter2:zipkin-sender-okhttp3'
}
```

## starter.java.property-conventions.gradle

```groovy
/**
 * Utility function for choosing between a team-defined configuration and a default core-define value.

 * @param value variable (or null)
 * @param defaultValue  return value if null
 * @return one or the other value
 */
public static String getValueOrDefault(String value, String defaultValue) {

    return !value ? defaultValue : value;
}
```

## starter.java.publish-bootjar-conventions.gradle

```groovy
/**
 * Configuration for publishing fat jar for spring-boot application
 */

plugins {
    id 'maven-publish'
}

publishing {
    publications {
        bootJarArtifact(MavenPublication) {
            artifact bootJar
        }
    }
}
```

## starter.java.publish-jar-conventions.gradle

```groovy
/**
 * Configurations for publishing jar files
 */

plugins {
    id 'maven-publish'
}

publishing {
    publications {
        libJarArtifact(MavenPublication) {
            from components.java
        }
    }
}
```

## starter.java.publish-pom-conventions.gradle

```groovy
/**
 * Configurations for publishing BOM packages (java-platform).
 */

plugins {
    id 'maven-publish'
}

publishing {
    publications {
        platformJarArtifact(MavenPublication) {
            from components.javaPlatform
        }
    }
}

```

## starter.java.publish-repo-conventions.gradle

```groovy
/**
 * Definition of publishing repository for any packages
 */

plugins {
    id 'maven-publish'
}

publishing {
    repositories {
        maven {
            name = "githubPackages"
            // change to point to your repo, e.g. http://my.org/repo
            def releasesRepoUrl = "${mavenRepository}"
            def snapshotsRepoUrl = "${mavenSnapshotRepository}"
            url = version.endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl
            credentials(PasswordCredentials)
        }
    }
}
```

## starter.java.release-conventions.gradle

```groovy
/**
 * Configurations for axion-release-plugin.
 */

plugins {
    id 'pl.allegro.tech.build.axion-release'
}

scmVersion {

    repository {
        // doc: Repository
        type = 'git' // type of repository
        directory = project.rootProject.file('./') // repository location
        remote = 'origin' // remote name

        // doc: Authorization
        customKey = getEnvOrDefault('GIT_AUTH_KEY', "customKeyDefault")
        customKeyPassword = getEnvOrDefault('GIT_SECRET_KEY', "secretKeyDefault") // key password
    }

    // doc: Dry run
//    localOnly = true // never connect to remote

    // doc: Uncommitted changes
    ignoreUncommittedChanges = false // should uncommitted changes force version bump

    // doc: Version / Tag with highest version
    useHighestVersion = false // Defaults as false, setting to true will find the highest visible version in the commit tree

    // doc: Version / Sanitization
    sanitizeVersion = true // should created version be sanitized, true by default

    // doc: Basic usage / Basic configuration
//    foldersToExclude = ['gradle'] // ignore changes in these subdirs when calculating changes to parent

    tag { // doc: Version / Parsing
//        prefix = 'tag-prefix' // prefix to be used, 'release' by default
//        branchPrefix = [ // set different prefix per branch
//                         'legacy/.*' : 'legacy'
//        ]

//        versionSeparator = '-' // separator between prefix and version number, '-' by default
//        serialize = { tag, version -> ... } // creates tag name from raw version
//        deserialize = { tag, position, tagName -> ... } // reads raw version from tag
//        initialVersion = { tag, position -> ... } // returns initial version if none found, 0.1.0 by default
    }

    nextVersion { // doc: Next version markers
        suffix = 'SNAPSHOT' // tag suffix
        separator = '-' // separator between version and suffix
//        serializer = { nextVersionConfig, version -> ... } // append suffix to version tag
//        deserializer = { nextVersionConfig, position -> ... } // strip suffix off version tag
    }

    // doc: Version / Decorating
//    versionCreator { version, position -> ... } // creates version visible for Gradle from raw version and current position in scm
//    versionCreator 'versionWithBranch' // use one of predefined version creators
//    branchVersionCreator = [ // use different creator per branch
//                             'main/.*': 'default',
//                             'feature/.*': 'versionWithBranch'
//    ]

    // doc: Version / Incrementing
//    versionIncrementer {context, config -> ...} // closure that increments a version from the raw version, current position in scm and config
    versionIncrementer 'incrementPatch' // use one of predefined version incrementing rules
//    branchVersionIncrementer = [ // use different incrementer per branch
//                                 'main/.*': 'incrementMinor'
//                                 'feature/.*': 'incrementMinor'
//                                 'release.*/.*': 'incrementPatch'
//    ]

    // doc: Pre/post release hooks
//    createReleaseCommit true // should create empty commit to annotate release in commit history, false by default
//    releaseCommitMessage { version, position -> ... } // custom commit message if commits are created

    // doc: Pre-release checks
    checks {
        uncommittedChanges = true // permanently disable uncommitted changes check
        aheadOfRemote = false // permanently disable ahead of remote check
        snapshotDependencies = true // ensure no components depend on snapshot releases
    }
}

allprojects {
    project.version = scmVersion.version
}

public static String getEnvOrDefault(String tagName, String defaultValue) {
    String ref = System.getenv(tagName)
    return !ref ? defaultValue : ref;
}
```

## starter.java.repo-altsource-conventions.gradle

```groovy
/**
 * Configurations for specifying a configurable repository (`mavenRepository`, `MAVEN_REPO_USERNAME`, `MAVEN_REPO_PASSWORD`)
 */

repositories {
    maven {
        url findProperty('mavenRepository')
        credentials {
            username = System.getenv('MAVEN_REPO_USERNAME')
            password = System.getenv('MAVEN_REPO_PASSWORD')
        }
    }
}
```

## starter.java.repo-default-conventions.gradle

```groovy
/**
 * Configurations for specifying standard defaults (local, mavenCentral, JCenter)
 */

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}
```

## starter.java.repo-local-conventions.gradle

```groovy
/**
 * Configurations for specifying only local maven `~/.m2` repository
 */

repositories {
    mavenLocal()
}
```

## starter.java.repo-starter-conventions.gradle

```groovy
/**
 * Configurations for specifying starter-bom Github Packages repository
 */

repositories {
    maven {
        name = "starterBootPkgs"
        url = uri("https://maven.pkg.github.com/ThoughtWorks-DPS/dps-starter-boot")
        credentials {
            String result = "unknown"
            result = System.getenv("STARTERBOOTPKGS_USERNAME")
            username = !result ? "STARTERBOOTPKGS_USERNAME_FIXME" : result;
            result = System.getenv("STARTERBOOTPKGS_TOKEN")
            password = !result ? "STARTERBOOTPKGS_TOKEN_FIXME" : result;
        }
    }
}
```

## starter.java.spotless-conventions.gradle

```groovy
/**
 * Configuration for spotless code formatting
 */

plugins {
    id "com.diffplug.spotless"
}

spotless {
    java {
        googleJavaFormat()
    }
}
```

## starter.java.style-conventions.gradle

```groovy
/**
 * Configuration for checkstyle code analysis
 */

plugins {
    id 'starter.java.checkstyle-conventions'
//    id 'starter.java.spotless-conventions'
}

```

## starter.java.test-conventions.gradle

```groovy
/**
 * Configuration for test task
 */

plugins {
    id 'java'
}

test {
    useJUnitPlatform {
        //excludeEngines 'junit-vintage'
    }
    testLogging {
        showStandardStreams = true
        events "passed", "skipped", "failed"
    }
}


```

## starter.java.test-gatling-conventions.gradle

```groovy
/**
 * Gatling configuration for running stress tests
 */

plugins {
    id 'io.gatling.gradle'
}

sourceSets {
    gatling {
        scala.srcDirs = ["src/gatling/scala"]
        resources.srcDirs = ["src/gatling/resources"]
    }
}
```

## starter.java.test-integration-conventions.gradle

```groovy
/**
 * Configurations for testing
 */

plugins {
    id 'starter.java.property-conventions'
}

sourceSets {
    integrationTest {
        java {
            compileClasspath += main.output
            compileClasspath += test.output
            runtimeClasspath += main.output
            runtimeClasspath += test.output
            srcDir file('src/integration/java')
        }
        resources.srcDir file('src/integration/resources')
    }
}

configurations {
    integrationTestImplementation.extendsFrom implementation
    integrationTestRuntimeOnly.extendsFrom runtimeOnly
}

task integrationTest(type: Test) {
    description = 'Runs integration tests.'
    group = 'verification'

    testClassesDirs = sourceSets.integrationTest.output.classesDirs
    classpath = sourceSets.integrationTest.runtimeClasspath
    outputs.upToDateWhen { false }
    mustRunAfter test
    useJUnitPlatform {
        excludeEngines 'junit-vintage'
    }
}

check.dependsOn integrationTest


```

## starter.java.test-jacoco-conventions.gradle

```groovy
/**
 * Configuration for jacoco
 */

plugins {
    id 'java'
    id 'jacoco'
    id 'starter.java.property-conventions'
}

jacoco {
    toolVersion = jacoco_version
    reportsDir = file("$buildDir/jacoco")
}

test {
    finalizedBy jacocoTestReport
    finalizedBy jacocoTestCoverageVerification
}


jacocoTestReport {
    reports {
        html.enabled true
        xml.enabled true
        csv.enabled false
    }

    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: ['**/*MapperImpl.*', '**/*Application.*'] )
        }))
    }

    dependsOn test
}

jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = jacoco_enforce_violations
            limit {
                minimum = jacoco_minimum_coverage
            }
            afterEvaluate {
                classDirectories.setFrom(files(classDirectories.files.collect {
                    fileTree(dir: it, exclude: ['**/*MapperImpl.*', '**/*Application.*'] )
                }))
            }
        }
    }
}



```

## starter.java.test-unit-conventions.gradle

```groovy
plugins {
    id 'starter.java.test-conventions'
}

```

## starter.java.versions-conventions.gradle

```groovy
/**
 * Configuration for ben-manes/gradle-versions-plugin and patrikerdes/gradle-use-latest-versions-plugin.
 *
 * These plugins will display a list of all the (direct) dependencies in your project, along with the version.
 * It will also determine if a newer version of the package is available, depending on the rules you set up.
 * For example, the default configuration specifies that if the current package is stable, then it will not suggest non-stable version updates.
 *
 * The gradle-use-latest-versions-plugin will use the information provided by the versions plugin to make changes to`build.gradle` and `gradle.properties` files to update dependency versions.
 */

plugins {
    id("com.github.ben-manes.versions")
    id("se.patrikerdes.use-latest-versions")
}

def isNonStable = { String version ->
    def stableKeyword = ['RELEASE', 'FINAL', 'GA'].any { it -> version.toUpperCase().contains(it) }
    def regex = /^[0-9,.v-]+(-r)?$/
    return !stableKeyword && !(version ==~ regex)
}

def isSnapshot = { String version ->
    return ['SNAPSHOT'].any { it -> version.toUpperCase().contains(it) }
}

def isReleaseCandidate = { String version ->
    return ['RC', 'rc'].any { it -> version.toUpperCase().contains(it) }
}

dependencyUpdates {
    checkForGradleUpdate = true
    checkConstraints = true

    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(project.version)) {
                    reject('Release candidate')
                }
            }
        }
    }
}

useLatestVersions {
    // A whitelist of dependencies to update, in the format of group:name
    // Equal to command line: --update-dependency=[values]
    updateWhitelist = []
    // A blacklist of dependencies to update, in the format of group:name
    // Equal to command line: --ignore-dependency=[values]
    //
    // NOTE: This is the list of spring-defined dependencies currently used in
    // starter.java.build-conventions
    updateBlacklist = [
            'com.fasterxml.jackson.datatype:jackson-datatype-jsr310',
            'org.assertj:assertj-core',
            'org.junit.jupiter:junit-jupiter-api',
            'org.junit.jupiter:junit-jupiter-engine',
            'org.mapstruct:mapstruct',
            'org.mapstruct:mapstruct-processor',
            'org.mockito:mockito-core',
            'org.projectlombok:lombok',
            'org.springframework.boot:spring-boot-starter-test'
    ]
    // When enabled, root project gradle.properties will also be populated with
    // versions from subprojects in multi-project build
    // Equal to command line: --update-root-properties
    updateRootProperties = false
    // List of root project files to update when updateRootProperties is enabled.
    // `build.gradle` is not an acceptable entry here as it breaks other expected
    // functionality. Version variables in `build.gradle` need to be moved into
    // a separate file which can be listed here.
    // Equal to command line: --root-version-files=[values]
    rootVersionFiles = ['gradle.properties']
}
```

## starter.metrics.build-time-tracker-conventions.gradle

```groovy
/**
 * Configuration for tracking how long a build takes, using `net.rdrei.android.buildtimetracker`
 */

plugins {
    id "net.rdrei.android.buildtimetracker"
}

buildtimetracker {
    reporters {
        csv {
            output "build/times.csv"
            append true
            header false
        }

        summary {
            ordered false
            threshold 50
            barstyle "unicode"
        }

        csvSummary {
            csv "build/times.csv"
        }
    }
}

```

## starter.metrics.talaiot-conventions.gradle

```groovy
/**
 * Configuration for tracking how long a build takes, using talaiot.
 * NOTE: No configuration set for shipping metrics to an aggregator, but the capability exists.
 */

plugins {
    id "com.cdsap.talaiot"
    id "com.cdsap.talaiot.plugin.base"
}

talaiot {
    metrics {
        // You can add your own custom Metric objects:
        customMetrics(
//                MyCustomMetric(),
        // Including some of the provided metrics, individually.
                HostnameMetric()
        )

        // Or define build or task metrics directly:
        customBuildMetrics(
                kotlinVersion: $kotlinVersion,
                javaVersion: $javaVersion
        )
//        customTaskMetrics(
//                customProperty: $value
//        )
    }

    filter {
        tasks {
//        excludes = arrayOf("preDebugBuild", "processDebugResources")
        }
        modules {
//        excludes = arrayOf(":app")
        }
        threshold {
//        minExecutionTime = 10
        }
        build {
            success = true
//          requestedTasks {
//            includes = arrayOf(":app:assemble.*")
//            excludes = arrayOf(":app:generate.*")
//          }
        }
    }

    ignoreWhen {
//        envName = "CI"
//        envValue = "true"
    }

    publishers {
//        influxDbPublisher {
//            dbName = "tracking"
//            url = "http://localhost:8086"
//            taskMetricName = "task"
//            buildMetricName = "build"
//        },
        jsonPublisher = true
        timelinePublisher = true
        taskDependencyPublisher {
            html = true
        }

    }

}
```

## starter.std.java.application-conventions.gradle

```groovy
/**
 * Top-level configuration of all the typical standard configurations for a Spring Boot application package.
 */

plugins {
    // Apply the java Plugin to add support for Java.
    id 'java'
    id "org.ajoberstar.grgit"
    // Apply the application plugin to add support for building a CLI application in Java.
    id 'application'
    id "org.springframework.boot" apply true
    id 'starter.java.deps-build-conventions'
    id 'starter.java.deps-test-conventions'
    id 'starter.java.container-conventions'
    id 'starter.java.style-conventions'
    id 'starter.java.doc-springdoc-conventions'
    id 'starter.java.test-conventions'
    id 'starter.java.test-integration-conventions'
    id 'starter.java.test-jacoco-conventions'
    id 'starter.java.test-gatling-conventions'
    id 'starter.java.deps-integration-conventions'
    id 'starter.java.publish-repo-conventions'
    id 'starter.java.publish-bootjar-conventions'
    id 'starter.java.versions-conventions'
}

dependencies {
    testImplementation 'org.mock-server:mockserver-netty'
    // open tracing testing/mock support
    testImplementation 'io.opentracing:opentracing-mock'
    testImplementation 'com.tngtech.archunit:archunit'
}
```

## starter.std.java.bom-conventions.gradle

```groovy
/**
 * Top-level configuration of all the typical standard configurations for a Bill of Materials package
 */

plugins {
    id 'java-platform'
    id 'starter.java.publish-repo-conventions'
    id 'starter.java.publish-pom-conventions'
    id 'starter.java.versions-conventions'
}


```

## starter.std.java.cli-conventions.gradle

```groovy
/**
 * Top-level configuration of all the typical standard configurations for a java cli (untested)
 */

plugins {
    // Apply the java Plugin to add support for Java.
    id 'java'
    id "org.ajoberstar.grgit"
    // Apply the application plugin to add support for building a CLI application in Java.
    id 'application'
    id 'starter.java.deps-build-conventions'
    id 'starter.java.deps-test-conventions'
    id 'starter.java.container-conventions'
    id 'starter.java.style-conventions'
    id 'starter.java.test-conventions'
    id 'starter.java.test-jacoco-conventions'
    id 'starter.java.test-unit-conventions'
    id 'starter.java.publish-repo-conventions'
    id 'starter.java.publish-jar-conventions'
    id 'starter.java.versions-conventions'
}

```

## starter.std.java.library-conventions.gradle

```groovy
/**
 * Top-level configuration of all the typical standard configurations for a normal Java jar.
 */

plugins {
    // Apply the java Plugin to add support for Java.
    id 'java'
    id 'java-library'
    id "org.ajoberstar.grgit"
    id 'starter.java.open-tracing-common-conventions'
    id 'starter.java.deps-build-conventions'
    id 'starter.java.deps-test-conventions'
    id 'starter.java.style-conventions'
    id 'starter.java.doc-swagger-conventions'
    id 'starter.java.test-conventions'
    id 'starter.java.test-unit-conventions'
    id 'starter.java.test-jacoco-conventions'
    id 'starter.java.publish-repo-conventions'
    id 'starter.java.publish-jar-conventions'
    id 'starter.java.versions-conventions'
}

```

## starter.std.java.plugin-conventions.gradle

```groovy
/**
 * Top-level configuration of all the typical standard configurations for a gradle plugin
 */

plugins {
    // Apply the java Plugin to add support for Java.
    id 'groovy'
    id 'java'
    id 'java-library'
    id 'java-gradle-plugin'
    id "org.ajoberstar.grgit"
    id 'starter.java.config-conventions'
    id 'starter.java.style-conventions'
    id 'starter.java.test-jacoco-conventions'
    id 'starter.java.test-unit-conventions'
    id 'starter.java.deps-plugin-conventions'
    id 'starter.java.deps-test-conventions'
    id 'starter.java.publish-repo-conventions'
    id 'starter.java.versions-conventions'
}

```
