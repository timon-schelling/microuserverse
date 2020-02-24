package util

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSetOutput
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.the
import java.io.File

inline fun <T : Any> Project.withJavaPlugin(crossinline body: () -> T?): T? {
    var res: T? = null
    pluginManager.withPlugin("java") {
        res = body()
    }
    return res
}

fun Project.javaPluginConvention(): JavaPluginConvention = the()


fun File.createFileIfNotExists(): Boolean {
    if (exists()) return true

    parentFile?.mkdirs()
    createNewFile()
    return exists()
}

fun File.createFolderIfNotExists(): Boolean {
    if (exists()) return true

    parentFile?.mkdirs()
    mkdir()
    return exists()
}

fun File.setupAsMutableFile(): Boolean {
    if (!isFile && !canRead() && !canWrite())  return false

    return createFileIfNotExists()
}

val ALPHANUMERIC_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

fun randomString(length: Int, chars: CharSequence = ALPHANUMERIC_CHARS): String {
    val randStr = StringBuilder()
    for (i in 0 until length) {
        randStr.append(chars.random())
    }
    return randStr.toString()
}

fun randomAlphanumericString(length: Int) = randomString(length, ALPHANUMERIC_CHARS)

val DEFAULT_DATE_TIME_FORMAT by lazy { DateFormat("yyyy.MM.dd-HH:mm:ss:SSS") }

fun DateTime.formatDefault() = format(DEFAULT_DATE_TIME_FORMAT)
