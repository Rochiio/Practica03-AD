package com.example.tennislabspring.model.orders.tasks

import com.example.tennislabspring.model.Product
import com.example.tennislabspring.model.TypeTask
import com.example.tennislabspring.model.machines.Customizer
import com.example.tennislabspring.model.machines.Stringer
import com.example.tennislabspring.model.users.Employee
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Serializable
@Document("stringing")
data class Stringing(
    @Id
    var id:String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var hTension : Int,
    var vTension : Int,
    var vString : Product,
    var hString : Product,
    var nKnots : Int,
    var price : Long,
    var racketId : String
)
