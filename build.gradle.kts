import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

description = "Fork of the inactive swingx-all library"

plugins {
    id("com.vanniktech.maven.publish") version "0.37.0"
    id("module-lib")
}

group = "io.github.osobolev"
version = "1.7.2"

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("${project.group}", "${project.name}", "${project.version}")
    configure(JavaLibrary(
        javadocJar = JavadocJar.Javadoc(),
        sourcesJar = SourcesJar.Sources()
    ))
}

mavenPublishing.pom {
    name = "swingx-all"
    description = "Fork of the inactive swingx-all library"
    url = "https://github.com/osobolev/swingx-all"
    licenses {
        license {
            name = "GNU General Lesser Public License (LGPL) version 3.0"
            url = "http://www.gnu.org/licenses/lgpl.html"
        }
    }
    developers {
        developer {
            name = "Oleg Sobolev"
            organizationUrl = "https://github.com/swingx-all"
        }
    }
    scm {
        connection = "scm:git:https://github.com/osobolev/swingx-all.git"
        developerConnection = "scm:git:https://github.com/osobolev/swingx-all.git"
        url = "https://github.com/osobolev/swingx-all"
    }
}
