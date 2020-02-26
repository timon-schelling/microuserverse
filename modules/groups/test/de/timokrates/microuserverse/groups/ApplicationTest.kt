package de.timokrates.microuserverse.groups

import de.timokrates.microuserverse.groups.testutil.randomString
import io.kotlintest.matchers.collections.shouldBeSameSizeAs
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.json.Json.Companion.parse

class ApplicationTest : AnnotationSpec() {

    private fun withTestApp(block: TestApplicationEngine.() -> Unit) {
        withTestApplication({ main() }, block)
    }

    @Test
    fun `INT - all groups`() = withTestApp {
        val groups = mutableListOf<Group>()
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                val group = Group(GroupId(groupId)).also { groups.add(it) }
                parse(Group.serializer(), response.content!!) shouldBe group
            }
        }
        handleRequest(HttpMethod.Get, "/groups") { }.apply {
            parse(ArrayListSerializer(Group.serializer()), response.content!!).also {
                it shouldBeSameSizeAs groups
            }.forEach {
                groups.remove(it)
            }
        }
        groups shouldHaveSize 0
    }

    @Test
    fun `INT - add group`() = withTestApp {
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
        }
    }

    @Test
    fun `INT - find group`() = withTestApp {
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            handleRequest(HttpMethod.Get, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
        }
    }

    @Test
    fun `INT - remove group`() = withTestApp {
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            handleRequest(HttpMethod.Get, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            handleRequest(HttpMethod.Delete, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            handleRequest(HttpMethod.Get, "/groups/${groupId}") { }.apply {
                response.content shouldBe null
            }
        }
    }

    @Test
    fun `INT - get all permissions of group`() = withTestApp {
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            val permissions = mutableListOf<Permission>()
            repeat(10) {
                val permissionId = randomString(32)
                handleRequest(HttpMethod.Post, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    val permission = Permission(PermissionId(permissionId)).also { permissions.add(it) }
                    parse(Permission.serializer(), response.content!!) shouldBe permission
                }
            }
            handleRequest(HttpMethod.Get, "/groups/${groupId}/permissions") { }.apply {
                parse(ArrayListSerializer(Permission.serializer()), response.content!!).also {
                    it shouldBeSameSizeAs permissions
                }.forEach {
                    permissions.remove(it)
                }
            }
            permissions shouldHaveSize 0
        }
    }

    @Test
    fun `INT - add permission to group`() = withTestApp {
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            repeat(10) {
                val permissionId = randomString(32)
                handleRequest(HttpMethod.Post, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
                }
            }
        }
    }

    @Test
    fun `INT - find permission of group`() = withTestApp {
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            repeat(10) {
                val permissionId = randomString(32)
                handleRequest(HttpMethod.Post, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
                }
                handleRequest(HttpMethod.Get, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
                }
            }
        }
    }

    @Test
    fun `INT - remove permission from group`() = withTestApp {
        repeat(10) {
            val groupId = randomString(32)
            handleRequest(HttpMethod.Post, "/groups/${groupId}") { }.apply {
                parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
            }
            repeat(10) {
                val permissionId = randomString(32)
                handleRequest(HttpMethod.Post, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
                }
                handleRequest(HttpMethod.Get, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
                }
                handleRequest(HttpMethod.Delete, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
                }
                handleRequest(HttpMethod.Get, "/groups/${groupId}/permissions/${permissionId}") { }.apply {
                    response.content shouldBe null
                }
            }
        }
    }
}