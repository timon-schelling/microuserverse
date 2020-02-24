package de.timokrates.microuserverse.permissions

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.json.Json

suspend fun <T> ApplicationCall.respondSerializedJson(serializer: KSerializer<T>, obj: T) {
    respondText(Json.stringify(serializer, obj), ContentType.Application.Json)
}

fun Application.main() {
    val repository = ListPermissionRepository()
    routing {
        get("/health") { call.respond(HttpStatusCode.OK) }
        route("/permissions") {
            get {
                call.respondSerializedJson(ArrayListSerializer(Permission.serializer()), repository.map { it })
            }
            route("/{permission}") {
                get {
                    val id = call.parameters["permission"] ?: return@get
                    val permission = repository.find(PermissionId(id)) ?: return@get
                    call.respondSerializedJson(Permission.serializer(), permission)
                }
                post {
                    val id = call.parameters["permission"] ?: return@post
                    val permission = repository.find(PermissionId(id)) ?: repository.add(PermissionId(id))
                    call.respondSerializedJson(Permission.serializer(), permission)
                }
                delete {
                    val id = call.parameters["permission"] ?: return@delete
                    val permission = repository.remove(PermissionId(id)) ?: return@delete
                    call.respondSerializedJson(Permission.serializer(), permission)
                }
            }
        }
    }
}
