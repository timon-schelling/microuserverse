package de.timokrates.microuserverse.users

class ListUserRepository(
        private val users: MutableList<User> = mutableListOf()
) : UserRepository {

    override fun add(id: UserId): User = User(id).also { users.add(it) }

    override fun remove(id: UserId): User? = find(id).also { users.remove(it) }

    override fun find(id: UserId): User? = users.find { it.id == id }

    override fun iterator() = users.iterator()
}
