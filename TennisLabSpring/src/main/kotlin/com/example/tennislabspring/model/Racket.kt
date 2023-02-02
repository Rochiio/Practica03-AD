package com.example.tennislabspring.model

import com.example.tennislabspring.model.orders.tasks.Customization
import com.example.tennislabspring.model.users.Customer
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.util.*

@Serializable
@Document("racket")
data class Racket(
    @Id
    var id: String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var brand: String,
    var model: String,
    var maneuverability: Float,
    var balance: Float,
    var rigidity: Float
){
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'customer':?#{#self._id} }")
    var customer: Customer? = null

    @ReadOnlyProperty
    @DocumentReference(lookup = "{'customization':?#{#self._id} }")
    var customizations: List<Customization>? = null

}
