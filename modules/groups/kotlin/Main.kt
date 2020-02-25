import de.timokrates.microuserverse.groups.main
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 21001) {
        main()
    }.start(wait = true)
}