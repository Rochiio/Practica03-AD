package model.users

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import util.serializer.LocalDateTimeSerializer
import util.serializer.UUIDSerializer
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Employee(
    @BsonId
    var id: String = newId<Employee>().toString(),
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
    var orderList: MutableList<String>? = null,
    var machine : String? = null

)
