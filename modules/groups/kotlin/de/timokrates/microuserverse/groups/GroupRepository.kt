package de.timokrates.microuserverse.groups

interface GroupRepository {
    fun add(id: GroupId): Group
    fun remove(id: GroupId): Group?
    fun find(id: GroupId): Group?
}
