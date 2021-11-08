plugins {
    id("base-lib")
}

val dummies = layout.buildDirectory.dir("dummies")

val java9 by sourceSets.creating {
    java.srcDir("src9")
    java.srcDir("src")
    java.srcDir(dummies)
}

fun generateDummies(root: File, dir: File) {
    dir.listFiles()?.forEach {
        if (it.isDirectory() && !"META-INF".equals(it.name)) {
            val rel = it.relativeTo(root)
            val pack = rel.toString().replace(File.separatorChar, '.')
            val target = dummies.get().asFile.resolve(rel)
            target.mkdirs()
            target.resolve("_Dummy_.java").writeText("package $pack; public class _Dummy_ {}")
            generateDummies(root, it)
        }
    }
}

tasks.register("generateDummies") {
    inputs.files(fileTree("resources"))
    outputs.dir(dummies)
    doLast {
        val resourceRoot = file("resources")
        generateDummies(resourceRoot, resourceRoot)
    }
}

tasks.named("compileJava9Java", JavaCompile::class).configure {
    dependsOn("generateDummies")
    options.release.set(9)
    modularity.inferModulePath.set(true)
    options.compilerArgs.add("-Xlint:-module,-requires-transitive-automatic")
}

val java9CompileOnly = configurations["java9CompileOnly"]
java9CompileOnly.extendsFrom(configurations.api.get(), configurations.implementation.get(), configurations.compileOnly.get())

tasks {
    jar {
        from(java9.output) {
            into("META-INF/versions/9")
            include("module-info.class")
        }
        manifest.attributes["Multi-Release"] = true
    }
}
