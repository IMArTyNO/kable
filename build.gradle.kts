buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.atomicfu) apply false
    alias(libs.plugins.validator)
    id("maven-publish")
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("dokkaHtmlMultiModule"))
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    tasks.withType<Test>().configureEach {
        testLogging {
            events("started", "passed", "skipped", "failed", "standardOut", "standardError")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showExceptions = true
            showStackTraces = true
            showCauses = true
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("kable") {
                groupId = "com.github.IMArTyNO" //your git id
                artifactId = "kable" //your-repository
                version = "0.20.1" // As same as the Tag

                artifact("$buildDir/outputs/aar/${artifactId}-release.aar")

                pom {
                    withXml {
                        val dependenciesNode = asNode().appendNode("dependencies")
                        configurations.getByName("implementation") {
                            dependencies.forEach {
                                val dependencyNode = dependenciesNode.appendNode("dependency")
                                dependencyNode.appendNode(groupId, it.group)
                                dependencyNode.appendNode(artifactId, it.name)
                                dependencyNode.appendNode(version, it.version)
                            }
                        }
                    }
                }
            }
        }
    }
}
