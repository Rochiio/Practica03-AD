package com.example.tennislabspring.model

import com.example.tennislabspring.model.orders.tasks.Purchase
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Serializable
@Document("product")
data class Product(
    @Id
    var id:String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var type: TypeProduct,
    var brand: String,
    var model: String,
    var price: Float,
    var stock: Int
){
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'purchase':?#{#self._id} }")
    var purchases: List<Purchase>? = null
}
