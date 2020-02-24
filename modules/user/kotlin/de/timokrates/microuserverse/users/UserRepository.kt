package de.timokrates.microuserverse.users

interface UserRepository : Iterable<User> {
    fun add(id: UserId): User
    fun remove(id: UserId): User?
    fun find(id: UserId): User?
}
