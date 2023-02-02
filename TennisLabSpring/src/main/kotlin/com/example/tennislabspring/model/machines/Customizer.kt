package com.example.tennislabspring.model.machines

import com.example.tennislabspring.serializer.LocalDateSerializer
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Serializable
@Document("customizer")
data class Customizer(
    @Id
    var id:String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var brand: String,
    var model: String,
    @Serializable(with = LocalDateSerializer::class)
    val acquisitionDate: LocalDate,
    var available: Boolean,
    var maneuverability: Boolean,
    var balance: Boolean,
    var rigidity: Boolean
)
