plugins {
    id("base-lib")
}

val java9 by sourceSets.creating {
    java.srcDir("src9")
    java.srcDir("src")
}

tasks.named("compileJava9Java", JavaCompile::class).configure {
    options.release.set(9)
    options.compilerArgs.add("-Xlint:deprecation,-opens")
}

tasks.jar {
    from(java9.output) {
        into("META-INF/versions/9")
        include("module-info.class")
    }
    manifest.attributes["Multi-Release"] = true
}
