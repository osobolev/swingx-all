description = "Fork of the inactive swingx-all library"

plugins {
    `java-library`
    `maven-publish`
    `signing`
}

group = "io.github.osobolev"
version = "1.7.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    compileOnly("org.kohsuke.metainf-services:metainf-services:1.8")
    annotationProcessor("org.kohsuke.metainf-services:metainf-services:1.8")
}

tasks {
    withType(JavaCompile::class) {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:deprecation,unchecked")
    }
    javadoc {
        options.encoding = "UTF-8"
        (options as? StandardJavadocDocletOptions)?.charSet("UTF-8")
        (options as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
        options.quiet()
    }
    jar {
        manifest.attributes["Implementation-Version"] = project.version
    }
}

val sonatypeUsername: String? by project
val sonatypePassword: String? by project

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("swingx-all")
                description.set("Fork of the inactive swingx-all library")
                url.set("https://github.com/osobolev/swingx-all")
                licenses {
                    license {
                        name.set("GNU General Lesser Public License (LGPL) version 3.0")
                        url.set("http://www.gnu.org/licenses/lgpl.html")
                    }
                }
                developers {
                    developer {
                        name.set("Oleg Sobolev")
                        organizationUrl.set("https://github.com/swingx-all")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/osobolev/swingx-all.git")
                    developerConnection.set("scm:git:https://github.com/osobolev/swingx-all.git")
                    url.set("https://github.com/osobolev/swingx-all")
                }
            }
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.named("clean").configure {
    doLast {
        project.delete("$projectDir/out")
    }
}
