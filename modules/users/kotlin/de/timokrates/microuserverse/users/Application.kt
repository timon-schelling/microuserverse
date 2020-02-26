package de.timokrates.microuserverse.users

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
    val repository = MapUserRepository()

    routing {
        get("/health") { call.respond(HttpStatusCode.OK) }
        route("/users") {
            get {
                call.respondSerializedJson(ArrayListSerializer(User.serializer()), repository.map { it })
            }
            route("/{user}") {
                get {
                    val id = call.parameters["user"] ?: return@get
                    val user = repository.find(UserId(id)) ?: return@get
                    call.respondSerializedJson(User.serializer(), user)
                }
                post {
                    val id = call.parameters["user"] ?: return@post
                    val user = repository.find(UserId(id)) ?: repository.add(UserId(id))
                    call.respondSerializedJson(User.serializer(), user)
                }
                delete {
                    val id = call.parameters["user"] ?: return@delete
                    val user = repository.remove(UserId(id)) ?: return@delete
                    call.respondSerializedJson(User.serializer(), user)
                }
                route("/groups") {
                    get {
                        val id = call.parameters["user"] ?: return@get
                        val groups = repository.groups(UserId(id))?.map { it } ?: return@get
                        call.respondSerializedJson(ArrayListSerializer(Group.serializer()), groups)
                    }
                    route("/{group}") {
                        get {
                            val id = call.parameters["user"] ?: return@get
                            val groupId = call.parameters["group"] ?: return@get
                            val group = repository.findGroup(UserId(id), GroupId(groupId)) ?: return@get
                            call.respondSerializedJson(Group.serializer(), group)
                        }
                        post {
                            val id = call.parameters["user"] ?: return@post
                            val groupId = call.parameters["group"] ?: return@post
                            val group = repository.addGroup(UserId(id), GroupId(groupId)) ?: return@post
                            call.respondSerializedJson(Group.serializer(), group)
                        }
                        delete {
                            val id = call.parameters["user"] ?: return@delete
                            val groupId = call.parameters["group"] ?: return@delete
                            val group = repository.removeGroup(UserId(id), GroupId(groupId)) ?: return@delete
                            call.respondSerializedJson(Group.serializer(), group)
                        }
                    }
                }
            }
        }
    }
}
