package com.example.tennislabspring.model.orders.tasks

import com.example.tennislabspring.model.Racket
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Serializable
@Document("customization")
data class Customization(
    @Id
    var id:String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var weight : Int,
    var balance : Float,
    var stiffness : Int,
    var price : Long,
    @DocumentReference
    var racket_id : Racket
)
