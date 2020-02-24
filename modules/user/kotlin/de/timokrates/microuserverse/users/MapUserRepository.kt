package de.timokrates.microuserverse.users

class MapUserRepository(
        private val users: MutableMap<User, MutableList<GroupId>> = mutableMapOf()
) : UserRepository {

    override fun add(id: UserId): User = User(id).also { users[it] = mutableListOf() }

    override fun remove(id: UserId): User? = find(id).also { users.remove(it) }

    override fun find(id: UserId): User? = users.map { it.key }.find { it.id == id }

    override fun groups(id: UserId): Iterator<GroupId>? {
        return users.filter { it.key.id == id }.map { it.value }.firstOrNull()?.iterator()
    }

    override fun addGroup(id: UserId, groupId: GroupId): GroupId? {
        val groups = users.filter { it.key.id == id }.map { it.value }.firstOrNull() ?: return null
        groups.add(groupId)
        return groupId
    }

    override fun findGroup(id: UserId, groupId: GroupId): GroupId? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeGroup(id: UserId, groupId: GroupId): GroupId? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun iterator() = users.map { it.key }.iterator()
}
