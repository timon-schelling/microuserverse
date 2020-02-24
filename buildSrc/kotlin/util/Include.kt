package util

import org.gradle.api.initialization.Settings
import java.io.File

fun Settings.includeFrom(dir: String, name: String) {
    include("$name")
    val moculeDir = "./$dir/${(name.removePrefix(":")).replace(":", "/")}"
    project("$name").projectDir = File(moculeDir)
}

fun Settings.includeFromModuleDir(name: String) = includeFrom("modules", name)
fun Settings.includeModule(name: String) = includeFrom(".", name)
