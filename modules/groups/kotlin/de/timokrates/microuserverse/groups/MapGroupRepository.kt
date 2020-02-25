package de.timokrates.microuserverse.groups

class MapGroupRepository(
        private val groups: MutableMap<Group, MutableList<Permission>> = mutableMapOf()
) : GroupRepository {

    override fun add(id: GroupId): Group = Group(id).also { groups[it] = mutableListOf() }

    override fun remove(id: GroupId): Group? = find(id).also { groups.remove(it) }

    override fun find(id: GroupId): Group? = groups.map { it.key }.find { it.id == id }

    override fun iterator() = groups.map { it.key }.iterator()

    override fun permissions(id: GroupId): Iterable<Permission>? {
        return groups.filter { it.key.id == id }.map { it.value }.firstOrNull()?.asIterable()
    }

    override fun addPermission(id: GroupId, permissionId: PermissionId): Permission? {
        val permissions = groups.filter { it.key.id == id }.map { it.value }.firstOrNull() ?: return null
        return permissions.find { it.id == permissionId } ?: Permission(permissionId).also { permissions.add(it) }
    }

    override fun removePermission(id: GroupId, permissionId: PermissionId): Permission? {
        val permissions = groups.filter { it.key.id == id }.map { it.value }.firstOrNull() ?: return null
        return (permissions.find { it.id == permissionId } ?: return null).also { permissions.remove(it) }
    }

    override fun findPermission(id: GroupId, permissionId: PermissionId): Permission? {
        val permissions = groups.filter { it.key.id == id }.map { it.value }.firstOrNull() ?: return null
        return permissions.find { it.id == permissionId }
    }
}
