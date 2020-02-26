package de.timokrates.microuserverse.users

interface UserRepository : Iterable<User> {
    fun add(id: UserId): User
    fun remove(id: UserId): User?
    fun find(id: UserId): User?
    fun groups(id: UserId): Iterable<Group>?
    fun addGroup(id: UserId, groupId: GroupId): Group?
    fun removeGroup(id: UserId, groupId: GroupId): Group?
    fun findGroup(id: UserId, groupId: GroupId): Group?
}
