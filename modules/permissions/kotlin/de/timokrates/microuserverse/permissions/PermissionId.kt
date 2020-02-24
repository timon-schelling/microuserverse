package de.timokrates.microuserverse.permissions

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.internal.StringSerializer

@Serializable(PermissionId.Serializer::class)
data class PermissionId(val value: String) {
    object Serializer : KSerializer<PermissionId> {
        override val descriptor: SerialDescriptor get() = StringDescriptor
        override fun deserialize(decoder: Decoder): PermissionId = PermissionId(StringSerializer.deserialize(decoder))
        override fun serialize(encoder: Encoder, obj: PermissionId) = StringSerializer.serialize(encoder, obj.value)
    }
}
