package de.timokrates.microuserverse.groups

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun Application.main() {
    routing {
        get("/health") { call.respond(HttpStatusCode.OK) }
        get("/") {

        }
        get("/{id}") {

        }
        post("/{id}") {

        }
        delete("/{id}") {

        }
    }
}
