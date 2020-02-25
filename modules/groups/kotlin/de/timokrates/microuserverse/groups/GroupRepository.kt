package de.timokrates.microuserverse.groups

import de.timokrates.microuserverse.groups.Group
import de.timokrates.microuserverse.groups.GroupId
import de.timokrates.microuserverse.groups.Permission
import de.timokrates.microuserverse.groups.PermissionId

interface GroupRepository : Iterable<Group> {
    fun add(id: GroupId): Group
    fun remove(id: GroupId): Group?
    fun find(id: GroupId): Group?
    fun permissions(id: GroupId): Iterable<Permission>?
    fun addPermission(id: GroupId, permissionId: PermissionId): Permission?
    fun removePermission(id: GroupId, permissionId: PermissionId): Permission?
    fun findPermission(id: GroupId, permissionId: PermissionId): Permission?
}
