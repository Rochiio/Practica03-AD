package model.users

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import util.serializer.LocalDateTimeSerializer
import util.serializer.UUIDSerializer
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Employee(
    @BsonId @Contextual
    var id: Id<Employee> = newId<Employee>(),
//   @Serializable(with = UUIDSerializer::class)
//    var uuid: UUID?,
    var name: String,
    var surname:String,
    var email:String,
    var password:String,
    var available: Boolean,
    var isAdmin: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    var entryTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    var departureTime: LocalDateTime,
    //Lista de pedidos máximo 2
    var orderList: List<String> //TODO cambiar a lista de pedidos (ObjectID)
)
