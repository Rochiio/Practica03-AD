package com.example.tennislabspring.model.orders.tasks

import com.example.tennislabspring.model.Product
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Serializable
@Document("purchase")
data class Purchase(
    @Id
    var id:String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var price: Float,
    @DocumentReference
    var product: List<Product>?
)
