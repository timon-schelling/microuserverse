package de.timokrates.microuserverse.users

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.internal.StringSerializer

@Serializable(UserId.Serializer::class)
data class UserId(val value: String) {
    object Serializer : KSerializer<UserId> {
        override val descriptor: SerialDescriptor get() = StringDescriptor
        override fun deserialize(decoder: Decoder): UserId = UserId(StringSerializer.deserialize(decoder))
        override fun serialize(encoder: Encoder, obj: UserId) = StringSerializer.serialize(encoder, obj.value)
    }
}
