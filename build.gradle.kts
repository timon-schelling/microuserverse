import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.createFileIfNotExists
import util.createFolderIfNotExists
import util.formatDefault
import util.randomAlphanumericString

version = "0.0.1"

val versionName = "Microuserverse Sputnik 1"

plugins {
    idea
    kotlin("jvm") version Versions.kotlinStdlib
    id("kotlinx-serialization") version Versions.kotlinStdlib
}

allprojects {

    version = rootProject.version

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("kotlinx-serialization")
    }

    configureRepositories()

    sourceSets.forEach {
        when (it.name) {
            "main" -> {
                it.resources.srcDirs("resources")
            }
            "test" -> {
                it.resources.srcDirs("testResources")
            }
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    kotlin {
        sourceSets.forEach {
            when (it.name) {
                "main" -> {
                    it.kotlin.srcDirs("kotlin")
                }
                "test" -> {
                    it.kotlin.srcDirs("test")
                }
            }
        }
    }

    dependencies {
        compile(Deps.kotlinStdlib)
        compile(Deps.kotlinStdlibJdk8)
        subprojects.forEach {
            project(it.path)
            add("compile", project(it.path))
            add("archives", project(it.path))
        }
    }

    val compileKotlin: KotlinCompile by tasks
    compileKotlin.apply {}
}

subprojects {
    val jar: Jar by tasks
    jar.apply {
        val jarName = "${rootProject.name}${project.path.replace(":", "-")}-${project.version}.jar"
        archiveFileName.set(jarName)
    }

    val test: Test by tasks
    test.apply {
        subprojects.forEach {
            dependsOn(it.tasks.findByPath("test") ?: return@forEach)
        }
    }

    dependencies {
        testCompile("io.kotlintest:kotlintest-runner-junit5:3.3.2")
        testCompile("io.ktor:ktor-server-test-host:${Versions.ktor}")
    }

    test.apply {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

}

val install = task("install") {
    group = "install"
    dependsOn("copyDistIntoRun")
}

val copyDistIntoRun = task("copyDistIntoRun", Sync::class) {
    group = "install"
    dependsOn("distribute")

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from("$buildDir/dist")
    subprojects.forEach {
        from("${it.buildDir}/dist")
    }
    into("run")
}

val distribute = task("distribute") {
    group = "distribute"
    dependsOn("build")
    dependsOn("copyAllProjectJarsIntoLibs")
    dependsOn("copyLibsIntoDist")
    dependsOn("copyStaticIntoDist")
    dependsOn("copySubProjectDist")

    doFirst {
        File("$buildDir/dist").createFolderIfNotExists()
    }
}

val build by tasks
build.apply {

}

val jar: Jar by tasks
jar.apply {
    archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
}

val test: Test by tasks
test.apply {
    subprojects.forEach {
        dependsOn(it.tasks.findByPath("test") ?: return@forEach)
    }
}

val copyAllProjectJarsIntoLibs = task("copyAllProjectJarsIntoLibs", Copy::class) {
    group = "distribute"
    shouldRunAfter("build")

    includeEmptyDirs = true
    subprojects.forEach { from("${it.buildDir.absolutePath}/libs") }
    subprojects.forEach {
        from(it.configurations.archives.map {
            it.asFileTree
        })
        from(it.configurations.compile.map {
            it.asFileTree
        })
    }
    into("$buildDir/libs")
}

val copyLibsIntoDist = task("copyLibsIntoDist", Copy::class) {
    group = "distribute"
    shouldRunAfter("copyAllProjectJarsIntoLibs")

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from("$buildDir/libs")
    into("$buildDir/dist/lib")
}

val copyStaticIntoDist = task("copyStaticIntoDist", Copy::class) {
    group = "distribute"

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from("static")
    subprojects.forEach {
        from("${it.projectDir}/static")
    }
    into("$buildDir/dist")
}

val copySubProjectDist = task("copySubProjectDist", Copy::class) {
    group = "distribute"

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    subprojects.forEach {
        from("${it.buildDir}/dist")
    }
    into("$buildDir/dist")
}

val clean by tasks
clean.apply {
    doFirst {
        delete {
            delete("run")
        }
    }
}
