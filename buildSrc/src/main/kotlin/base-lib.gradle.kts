plugins {
    `java-library`
}

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sourceSets {
    main {
        java.srcDir("src")
        resources.srcDir("resources")
    }
    test {
        java.srcDir("unit")
        resources.srcDir("unitResources")
    }
    create("manual") {
        java.srcDir("test")
        resources.srcDir("testResources")
    }
}

configurations["manualImplementation"].extendsFrom(configurations["implementation"])
configurations["manualRuntimeOnly"].extendsFrom(configurations["runtimeOnly"])
configurations["manualCompileOnly"].extendsFrom(configurations["compileOnly"])

dependencies {
    "manualImplementation"(sourceSets["main"].output)
}

tasks {
    withType(JavaCompile::class).configureEach {
        options.encoding = "UTF-8"
        options.release.set(8)
        options.compilerArgs.add("-Xlint:deprecation")
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

tasks.clean {
    delete("$projectDir/out")
}
