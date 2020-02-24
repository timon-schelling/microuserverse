package de.timokrates.microuserverse.users

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.internal.StringSerializer

@Serializable(GroupId.Serializer::class)
data class GroupId(val value: String) {
    object Serializer : KSerializer<GroupId> {
        override val descriptor: SerialDescriptor get() = StringDescriptor
        override fun deserialize(decoder: Decoder): GroupId = GroupId(StringSerializer.deserialize(decoder))
        override fun serialize(encoder: Encoder, obj: GroupId) = StringSerializer.serialize(encoder, obj.value)
    }
}
