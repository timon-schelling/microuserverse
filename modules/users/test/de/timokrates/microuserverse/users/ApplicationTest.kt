package de.timokrates.microuserverse.users

import de.timokrates.microuserverse.users.testutil.randomString
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
    fun `INT - all users`() = withTestApp {
        val users = mutableListOf<User>()
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                val user = User(UserId(userId)).also { users.add(it) }
                parse(User.serializer(), response.content!!) shouldBe user
            }
        }
        handleRequest(HttpMethod.Get, "/users") { }.apply {
            parse(ArrayListSerializer(User.serializer()), response.content!!).also {
                it shouldBeSameSizeAs users
            }.forEach {
                users.remove(it)
            }
        }
        users shouldHaveSize 0
    }

    @Test
    fun `INT - add user`() = withTestApp {
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
        }
    }

    @Test
    fun `INT - find user`() = withTestApp {
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            handleRequest(HttpMethod.Get, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
        }
    }

    @Test
    fun `INT - remove user`() = withTestApp {
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            handleRequest(HttpMethod.Get, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            handleRequest(HttpMethod.Delete, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            handleRequest(HttpMethod.Get, "/users/${userId}") { }.apply {
                response.content shouldBe null
            }
        }
    }

    @Test
    fun `INT - get all groups of user`() = withTestApp {
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            val groups = mutableListOf<Group>()
            repeat(10) {
                val groupId = randomString(32)
                handleRequest(HttpMethod.Post, "/users/${userId}/groups/${groupId}") { }.apply {
                    val group = Group(GroupId(groupId)).also { groups.add(it) }
                    parse(Group.serializer(), response.content!!) shouldBe group
                }
            }
            handleRequest(HttpMethod.Get, "/users/${userId}/groups") { }.apply {
                parse(ArrayListSerializer(Group.serializer()), response.content!!).also {
                    it shouldBeSameSizeAs groups
                }.forEach {
                    groups.remove(it)
                }
            }
            groups shouldHaveSize 0
        }
    }

    @Test
    fun `INT - add group to user`() = withTestApp {
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            repeat(10) {
                val groupId = randomString(32)
                handleRequest(HttpMethod.Post, "/users/${userId}/groups/${groupId}") { }.apply {
                    parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
                }
            }
        }
    }

    @Test
    fun `INT - find group of user`() = withTestApp {
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            repeat(10) {
                val groupId = randomString(32)
                handleRequest(HttpMethod.Post, "/users/${userId}/groups/${groupId}") { }.apply {
                    parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
                }
                handleRequest(HttpMethod.Get, "/users/${userId}/groups/${groupId}") { }.apply {
                    parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
                }
            }
        }
    }

    @Test
    fun `INT - remove group from user`() = withTestApp {
        repeat(10) {
            val userId = randomString(32)
            handleRequest(HttpMethod.Post, "/users/${userId}") { }.apply {
                parse(User.serializer(), response.content!!) shouldBe User(UserId(userId))
            }
            repeat(10) {
                val groupId = randomString(32)
                handleRequest(HttpMethod.Post, "/users/${userId}/groups/${groupId}") { }.apply {
                    parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
                }
                handleRequest(HttpMethod.Get, "/users/${userId}/groups/${groupId}") { }.apply {
                    parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
                }
                handleRequest(HttpMethod.Delete, "/users/${userId}/groups/${groupId}") { }.apply {
                    parse(Group.serializer(), response.content!!) shouldBe Group(GroupId(groupId))
                }
                handleRequest(HttpMethod.Get, "/users/${userId}/groups/${groupId}") { }.apply {
                    response.content shouldBe null
                }
            }
        }
    }
}