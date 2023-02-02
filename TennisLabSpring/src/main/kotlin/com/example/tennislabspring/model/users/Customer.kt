package com.example.tennislabspring.model.users

import com.example.tennislabspring.model.Racket
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import com.example.tennislabspring.serializer.UUIDSerializer
import java.util.*

@Serializable
@Document("customer")
data class Customer(
    @Id
    var id:String= ObjectId.get().toString(),
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var nId : Int = 0,
    var name: String,
    var username:String,
    var email:String,
    var password:String,
    var available: Boolean,
    //Lista de pedidos del cliente
    @DocumentReference
    var orderList: List<String>?,
    @DocumentReference
    var tennisRacketsList: List<Racket>?
)
