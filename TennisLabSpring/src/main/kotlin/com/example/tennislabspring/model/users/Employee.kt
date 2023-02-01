package com.example.tennislabspring.model.users

import com.example.tennislabspring.model.orders.tasks.Stringing
import com.example.tennislabspring.model.orders.tasks.Task
import com.example.tennislabspring.serializer.LocalDateTimeSerializer
import com.example.tennislabspring.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDateTime
import java.util.*

@Serializable
@Document("employee")
data class Employee(
    @Id
    var id: String = ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var name: String,
    var surname:String,
    var email:String,
    var password:String,
    var available: Boolean,
    var isAdmin: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    var entryTime: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    var departureTime: LocalDateTime? = null,
    //Lista de pedidos máximo 2
    //TODO mirar
    @DocumentReference
    var orderList: MutableList<String>? = null,
    @DocumentReference
    var machine : String? = null
){
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'stringing':?#{#self._id} }")
    var stringings: List<Stringing>? = null

    @ReadOnlyProperty
    @DocumentReference(lookup = "{'task':?#{#self._id} }")
    var tasks: List<Task>? = null
}