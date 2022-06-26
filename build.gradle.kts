import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    jacoco
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

dependencies {
    subprojects.forEach { implementation(project(it.path)) }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.gradle.jacoco")
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.gradle.signing")

    group = "com.abroadbent"
    version = "0.2.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib", version = "1.7.0"))

        testImplementation("io.kotest:kotest-runner-junit5:5.3.1")
    }

    jacoco {
        toolVersion = "0.8.7"
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }

        compileTestKotlin {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }

        jacocoTestReport {
            dependsOn(test)
            reports {
                xml.apply {
                    required.set(true)
                    outputLocation.set(file("$buildDir/reports/jacoco/report.xml"))
                }
                csv.required.set(false)
                html.required.set(true)
            }
        }

        test {
            useJUnitPlatform()
            testLogging {
                events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
            }
            finalizedBy(jacocoTestReport)
        }

        artifacts {
            archives(kotlinSourcesJar)
            archives(jar)
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                group = this@subprojects.group
                artifactId = this@subprojects.name
                version = this@subprojects.version as String

                from(components["java"])

                pom {
                    name.set(this@subprojects.name)
                    description.set("JSON DSL for Jackson and Gson")
                    packaging = "jar"
                    url.set("https://github.com/AlexBroadbent/json-dsl")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("AlexBroadbent")
                            name.set("Alexander Broadbent")
                            email.set("hello@abroadbent.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:ssh://github.com/AlexBroadbent/json-dsl.git")
                        developerConnection.set("scm:git:ssh://github.com/AlexBroadbent/json-dsl.git")
                        url.set("https://github.com/AlexBroadbent/json-dsl")
                    }
                }
            }
        }

        val NEXUS_USERNAME: String by properties
        val NEXUS_PASSWORD: String by properties
        repositories {
            maven {
                credentials {
                    username = NEXUS_USERNAME
                    password = NEXUS_PASSWORD
                }
                name = this@subprojects.name
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
        }
    }

    signing {
        sign(publishing.publications)
    }
}
