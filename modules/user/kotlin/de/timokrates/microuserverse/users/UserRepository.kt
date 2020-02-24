package de.timokrates.microuserverse.users

interface UserRepository : Iterable<User> {
    fun add(id: UserId): User
    fun remove(id: UserId): User?
    fun find(id: UserId): User?
    fun groups(id: UserId): Iterator<GroupId>?
    fun addGroup(id: UserId, groupId: GroupId): GroupId?
    fun findGroup(id: UserId, groupId: GroupId): GroupId?
    fun removeGroup(id: UserId, groupId: GroupId): GroupId?
}
