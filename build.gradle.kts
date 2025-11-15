import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

description = "Fork of the inactive swingx-all library"

plugins {
    id("com.vanniktech.maven.publish") version "0.35.0"
    `module-lib`
}

group = "io.github.osobolev"
version = "1.7.2"

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("${project.group}", "${project.name}", "${project.version}")
    configure(JavaLibrary(
        javadocJar = JavadocJar.Javadoc(),
        sourcesJar = true
    ))
}

mavenPublishing.pom {
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
