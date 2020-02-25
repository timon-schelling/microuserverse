package de.timokrates.microuserverse.groups

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
    val repository = MapGroupRepository()

    routing {
        get("/health") { call.respond(HttpStatusCode.OK) }
        route("/groups") {
            get {
                call.respondSerializedJson(ArrayListSerializer(Group.serializer()), repository.map { it })
            }
            route("/{group}") {
                get {
                    val id = call.parameters["group"] ?: return@get
                    val group = repository.find(GroupId(id)) ?: return@get
                    call.respondSerializedJson(Group.serializer(), group)
                }
                post {
                    val id = call.parameters["group"] ?: return@post
                    val group = repository.find(GroupId(id)) ?: repository.add(GroupId(id))
                    call.respondSerializedJson(Group.serializer(), group)
                }
                delete {
                    val id = call.parameters["group"] ?: return@delete
                    val group = repository.remove(GroupId(id)) ?: return@delete
                    call.respondSerializedJson(Group.serializer(), group)
                }
                route("/permissions") {
                    get {
                        val id = call.parameters["group"] ?: return@get
                        val permissions = repository.permissions(GroupId(id))?.map { it } ?: return@get
                        call.respondSerializedJson(ArrayListSerializer(Permission.serializer()), permissions)
                    }
                    route("/{permission}") {
                        get {
                            val id = call.parameters["group"] ?: return@get
                            val permissionId = call.parameters["permission"] ?: return@get
                            val permission = repository.findPermission(GroupId(id), PermissionId(permissionId)) ?: return@get
                            call.respondSerializedJson(Permission.serializer(), permission)
                        }
                        post {
                            val id = call.parameters["group"] ?: return@post
                            val permissionId = call.parameters["permission"] ?: return@post
                            val permission = repository.addPermission(GroupId(id), PermissionId(permissionId)) ?: return@post
                            call.respondSerializedJson(Permission.serializer(), permission)
                        }
                        delete {
                            val id = call.parameters["group"] ?: return@delete
                            val permissionId = call.parameters["permission"] ?: return@delete
                            val permission = repository.removePermission(GroupId(id), PermissionId(permissionId)) ?: return@delete
                            call.respondSerializedJson(Permission.serializer(), permission)
                        }
                    }
                }
            }
        }
    }
}
