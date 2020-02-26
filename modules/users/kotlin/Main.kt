@file:JvmName("Main")

import de.timokrates.microuserverse.users.main
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 21003) {
        main()
    }.start(wait = true)
}