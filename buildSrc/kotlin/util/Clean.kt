package util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

fun Project.deleteOutOnClean() {
    tasks {
        "clean" {
            doFirst {
                delete("$projectDir/out")
            }
        }
    }
}
