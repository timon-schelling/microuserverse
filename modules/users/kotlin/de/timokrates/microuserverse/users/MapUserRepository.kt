package de.timokrates.microuserverse.users

class MapUserRepository(
        private val users: MutableMap<User, MutableList<Group>> = mutableMapOf()
) : UserRepository {

    override fun add(id: UserId): User = User(id).also { users[it] = mutableListOf() }

    override fun remove(id: UserId): User? = find(id).also { users.remove(it) }

    override fun find(id: UserId): User? = users.map { it.key }.find { it.id == id }

    override fun iterator() = users.map { it.key }.iterator()

    override fun groups(id: UserId): Iterable<Group>? {
        return users.filter { it.key.id == id }.map { it.value }.firstOrNull()?.asIterable()
    }

    override fun addGroup(id: UserId, groupId: GroupId): Group? {
        val groups = users.filter { it.key.id == id }.map { it.value }.firstOrNull() ?: return null
        return groups.find { it.id == groupId } ?: Group(groupId).also { groups.add(it) }
    }

    override fun removeGroup(id: UserId, groupId: GroupId): Group? {
        val groups = users.filter { it.key.id == id }.map { it.value }.firstOrNull() ?: return null
        return (groups.find { it.id == groupId } ?: return null).also { groups.remove(it) }
    }

    override fun findGroup(id: UserId, groupId: GroupId): Group? {
        val groups = users.filter { it.key.id == id }.map { it.value }.firstOrNull() ?: return null
        return groups.find { it.id == groupId }
    }
}
