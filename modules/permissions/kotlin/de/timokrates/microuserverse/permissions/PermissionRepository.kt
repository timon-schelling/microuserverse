package de.timokrates.microuserverse.permissions

interface PermissionRepository : Iterable<Permission> {
    fun add(id: PermissionId): Permission
    fun remove(id: PermissionId): Permission?
    fun find(id: PermissionId): Permission?
}
