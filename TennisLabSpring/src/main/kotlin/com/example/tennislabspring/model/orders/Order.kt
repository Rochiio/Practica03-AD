package com.example.tennislabspring.model.orders

import com.example.tennislabspring.model.Status
import com.example.tennislabspring.model.orders.tasks.Task
import com.example.tennislabspring.model.users.Customer
import com.example.tennislabspring.serializer.LocalDateSerializer
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDate
import java.util.*

@Serializable
@Document("order")
data class Order(
    @Id
    var id:String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var state: Status,
    @Serializable(with = LocalDateSerializer::class)
    val entryDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class)
    var exitDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class)
    var finalDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class)
    val maxDate: LocalDate?,
    var totalPrice: Float,
    val client: Customer,
    @DocumentReference
    var tasks: List<Task>?
)
