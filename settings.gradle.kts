import util.includeFromModuleDir

rootProject.name = "microuserverse"

includeFromModuleDir(":user")
includeFromModuleDir(":groups")
includeFromModuleDir(":permissions")

//todo remove
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id=="kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}
