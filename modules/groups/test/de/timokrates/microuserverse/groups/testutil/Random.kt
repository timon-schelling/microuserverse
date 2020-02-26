package de.timokrates.microuserverse.groups.testutil

val ALPHANUMERIC_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

fun randomString(length: Int, chars: CharSequence = ALPHANUMERIC_CHARS): String {
    val randStr = StringBuilder()
    for (i in 0 until length) {
        randStr.append(chars.random())
    }
    return randStr.toString()
}

fun randomAlphanumericString(length: Int) = randomString(length, ALPHANUMERIC_CHARS)
