package de.timokrates.microuserverse.permissions

import de.timokrates.microuserverse.permissions.testutil.randomString
import io.kotlintest.matchers.collections.shouldBeSameSizeAs
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
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
    fun `INT - all permissions`() = withTestApp {
        val permissions = mutableListOf<Permission>()
        repeat(10) {
            val permissionId = randomString(32)
            handleRequest(HttpMethod.Post, "/permissions/${permissionId}") { }.apply {
                val permission = Permission(PermissionId(permissionId)).also { permissions.add(it) }
                parse(Permission.serializer(), response.content!!) shouldBe permission
            }
        }
        handleRequest(HttpMethod.Get, "/permissions") { }.apply {
            parse(ArrayListSerializer(Permission.serializer()), response.content!!).also {
                it shouldBeSameSizeAs permissions
            }.forEach {
                permissions.remove(it)
            }
        }
        permissions shouldHaveSize 0
    }

    @Test
    fun `INT - add permission`() = withTestApp {
        repeat(10) {
            val permissionId = randomString(32)
            handleRequest(HttpMethod.Post, "/permissions/${permissionId}") { }.apply {
                parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
            }
        }
    }

    @Test
    fun `INT - find permission`() = withTestApp {
        repeat(10) {
            val permissionId = randomString(32)
            handleRequest(HttpMethod.Post, "/permissions/${permissionId}") { }.apply {
                parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
            }
            handleRequest(HttpMethod.Get, "/permissions/${permissionId}") { }.apply {
                parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
            }
        }
    }

    @Test
    fun `INT - remove permission`() = withTestApp {
        repeat(10) {
            val permissionId = randomString(32)
            handleRequest(HttpMethod.Post, "/permissions/${permissionId}") { }.apply {
                parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
            }
            handleRequest(HttpMethod.Get, "/permissions/${permissionId}") { }.apply {
                parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
            }
            handleRequest(HttpMethod.Delete, "/permissions/${permissionId}") { }.apply {
                parse(Permission.serializer(), response.content!!) shouldBe Permission(PermissionId(permissionId))
            }
            handleRequest(HttpMethod.Get, "/permissions/${permissionId}") { }.apply {
                response.content shouldBe null
            }
        }
    }
}