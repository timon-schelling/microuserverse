package de.timokrates.microuserverse.permissions

class ListPermissionRepository(
        private val permissions: MutableList<Permission> = mutableListOf()
) : PermissionRepository {

    override fun add(id: PermissionId): Permission = Permission(id).also { permissions.add(it) }

    override fun remove(id: PermissionId): Permission? = find(id).also { permissions.remove(it) }

    override fun find(id: PermissionId): Permission? = permissions.find { it.id == id }

    override fun iterator() = permissions.iterator()
}
